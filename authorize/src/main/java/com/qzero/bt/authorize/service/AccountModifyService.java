package com.qzero.bt.authorize.service;

import com.qzero.bt.common.authorize.dao.AuthorizeInfoRepository;
import com.qzero.bt.common.authorize.dao.TokenRepository;
import com.qzero.bt.common.authorize.dao.UserInfoRepository;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class AccountModifyService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthorizeInfoRepository authorizeInfoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * Get all tokens belonging to the user
     */

    public List<TokenEntity> getTokenList(String userName){
        return tokenRepository.getAllByOwnerUserName(userName);
    }

    /**
     * Change password for a user
     * This action will delete all tokens the user used to have(those whose permission level less than global)
     */

    public void changePassword(String userName,String newPasswordHash){
        AuthorizeInfoEntity authorizeInfoEntityFromDao=authorizeInfoRepository.getOne(userName);
        authorizeInfoEntityFromDao.setPasswordHash(newPasswordHash);
        authorizeInfoRepository.save(authorizeInfoEntityFromDao);

        tokenRepository.deleteByOwnerUserName(userName);
    }

    /**
     * Freeze the account
     * It will also delete all tokens the user used to have(those whose permission level less than global)
     */

    public void freezeAccount(String userName){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoRepository.getOne(userName);
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_FREEZING);
        authorizeInfoRepository.save(authorizeInfoEntity);

        tokenRepository.deleteByOwnerUserName(userName);
    }


    public void unfreezeAccount(String userName){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoRepository.getOne(userName);
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_ALIVE);
        authorizeInfoRepository.save(authorizeInfoEntity);
    }


    public UserInfoEntity getUserInfo(String userName){
        UserInfoEntity userInfoEntity=userInfoRepository.getOne(userName);
        return userInfoEntity;
    }


    public void updateUserInfo(UserInfoEntity userInfoEntity) throws ResponsiveException {
        UserInfoEntity origin=userInfoRepository.getOne(userInfoEntity.getUserName());
        if(origin.getGroupLevel()!=userInfoEntity.getGroupLevel()){
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You can not update your group level");
        }
        userInfoRepository.save(userInfoEntity);
    }

}
