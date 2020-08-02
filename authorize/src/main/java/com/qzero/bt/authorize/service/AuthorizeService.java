package com.qzero.bt.authorize.service;

import com.qzero.bt.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.authorize.dao.TokenDao;
import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.authorize.data.TokenEntity;
import com.qzero.bt.authorize.data.UserInfoEntity;
import com.qzero.bt.authorize.exception.ErrorCodeList;
import com.qzero.bt.authorize.exception.ResponsiveException;
import com.qzero.bt.authorize.permission.PermissionCheck;
import com.qzero.bt.authorize.permission.PermissionNameList;
import com.qzero.bt.authorize.utils.UUIDUtils;
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
        AuthorizeInfoEntity authorizeInfoEntityFromDao;
        if(authorizeInfoEntity.getCodeHash()==null){
            authorizeInfoEntityFromDao = authorizeInfoDao.getUserByNameAndPassword(authorizeInfoEntity);

            //Use password,only application token
            tokenEntity.setPermissionLevel(TokenEntity.PERMISSION_LEVEL_APPLICATION);

            //And if the account is freezing,deny
            if(authorizeInfoEntityFromDao !=null &&
                    authorizeInfoEntityFromDao.getUserInfoEntity().getAccountStatus()== UserInfoEntity.STATUS_FREEZING){
                throw new ResponsiveException(ErrorCodeList.CODE_ACCOUNT_FROZEN,"The account is freezing!\nLogin refused!");
            }
        }else{
            authorizeInfoEntityFromDao = authorizeInfoDao.getUserByNameAndCode(authorizeInfoEntity);

            //Using code can gain global permission
            tokenEntity.setPermissionLevel(TokenEntity.PERMISSION_LEVEL_GLOBAL);
        }

        if(authorizeInfoEntityFromDao ==null)
            throw new ResponsiveException(ErrorCodeList.CODE_WRONG_LOGIN_INFO,"Your login info is wrong,login denied");

        tokenEntity.setTokenId(UUIDUtils.getRandomUUID());
        tokenEntity.setOwnerUserName(authorizeInfoEntityFromDao.getUserName());
        tokenEntity.setGenerateTime(System.currentTimeMillis());
        tokenDao.addToken(tokenEntity);
        return tokenEntity;
    }

    /**
     * Get token by tokenId
     * @param tokenEntity tokenId and userName needed
     * @return The token,null if not exists
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_TOKEN)
    public TokenEntity getTokenById(TokenEntity tokenEntity){
        return tokenDao.getTokenById(tokenEntity);
    }

    /**
     * Log out
     * @param tokenEntity tokenId needed
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_TOKEN)
    public void logout(TokenEntity tokenEntity){
        tokenDao.deleteToken(tokenEntity);
    }

}
