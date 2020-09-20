package com.qzero.bt.authorize.service;

import com.qzero.bt.common.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.common.authorize.dao.TokenDao;
import com.qzero.bt.common.authorize.dao.UserInfoDao;
import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.common.authorize.data.TokenEntity;
import com.qzero.bt.common.authorize.data.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class AccountModifyService {

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private AuthorizeInfoDao authorizeInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    /**
     * Get all tokens belonging to the user
     */

    public List<TokenEntity> getTokenList(String userName){
        return tokenDao.getAllTokensByOwnerUserName(userName);
    }

    /**
     * Change password for a user
     * This action will delete all tokens the user used to have(those whose permission level less than global)
     */

    public void changePassword(String userName,String newPasswordHash){
        AuthorizeInfoEntity authorizeInfoEntityFromDao=authorizeInfoDao.getAuthorizeInfoByName(userName);
        authorizeInfoEntityFromDao.setPasswordHash(newPasswordHash);
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntityFromDao);

        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(userName);
    }

    /**
     * Freeze the account
     * It will also delete all tokens the user used to have(those whose permission level less than global)
     */

    public void freezeAccount(String userName){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getAuthorizeInfoByName(userName);
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_FREEZING);
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntity);
        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(userName);
    }


    public void unfreezeAccount(String userName){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getAuthorizeInfoByName(userName);
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_ALIVE);
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntity);
    }


    public UserInfoEntity getUserInfo(String userName){
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo(userName);
        return userInfoEntity;
    }


    public void updateUserInfo(String userName, UserInfoEntity userInfoEntity) {
        userInfoEntity.setUserName(userName);
        userInfoDao.updateUserInfo(userInfoEntity);
    }

}
