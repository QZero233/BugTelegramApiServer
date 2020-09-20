package com.qzero.bt.authorize.service;

import com.qzero.bt.common.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.common.authorize.dao.TokenDao;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorizeService {

    @Autowired
    private AuthorizeInfoDao authorizeInfoDao;

    @Autowired
    private TokenDao tokenDao;

    /**
     * Login with either password or code
     * If codeHash does not exist,passwordHash will be used
     * @param authorizeInfoEntity userName,passwordHash(or codeHash) needed
     * @param tokenEntity token containing some application info such as applicationId
     * @return The token if it succeeded,or it will be null
     */
    public TokenEntity login(AuthorizeInfoEntity authorizeInfoEntity, TokenEntity tokenEntity) throws ResponsiveException {
        AuthorizeInfoEntity authorizeInfoEntityFromDao=authorizeInfoDao.getAuthorizeInfoByName(authorizeInfoEntity.getUserName());

        if(authorizeInfoEntity.getCodeHash()==null){
            //Check password
            if(!authorizeInfoEntityFromDao.getPasswordHash().equals(authorizeInfoEntity.getPasswordHash())){
                throw new ResponsiveException(ErrorCodeList.CODE_WRONG_LOGIN_INFO,"Your login info is wrong,login denied");
            }

            //Use password,only application token
            tokenEntity.setPermissionLevel(TokenEntity.PERMISSION_LEVEL_APPLICATION);

            //And if the account is freezing,deny
            if(authorizeInfoEntityFromDao !=null &&
                    authorizeInfoEntityFromDao.getAuthorizeStatus()== AuthorizeInfoEntity.STATUS_FREEZING){
                throw new ResponsiveException(ErrorCodeList.CODE_ACCOUNT_FROZEN,"The account is freezing!\nLogin refused!");
            }
        }else{
            //Check code
            if(!authorizeInfoEntityFromDao.getCodeHash().equals(authorizeInfoEntity.getCodeHash())){
                throw new ResponsiveException(ErrorCodeList.CODE_WRONG_LOGIN_INFO,"Your login info is wrong,login denied");
            }

            //Using code can gain global permission
            //And there no need checking account status
            //Since frozen account can login with code
            tokenEntity.setPermissionLevel(TokenEntity.PERMISSION_LEVEL_GLOBAL);
        }

        tokenEntity.setTokenId(UUIDUtils.getRandomUUID());
        tokenEntity.setOwnerUserName(authorizeInfoEntityFromDao.getUserName());
        tokenEntity.setGenerateTime(System.currentTimeMillis());
        tokenDao.addToken(tokenEntity);
        return tokenEntity;
    }

    /**
     * Get token by tokenId
     * @return The token,null if not exists
     */

    public TokenEntity getTokenById(String tokenId){
        return tokenDao.getTokenById(tokenId);
    }

    /**
     * Log out
     * @param tokenEntity tokenId needed
     */

    public void logout(TokenEntity tokenEntity){
        tokenDao.deleteToken(tokenEntity);
    }

    public int getAuthorizeStatus(String userName){
        return authorizeInfoDao.getAuthorizeInfoByName(userName).getAuthorizeStatus();
    }

}
