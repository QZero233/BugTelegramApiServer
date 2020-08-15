package com.qzero.bt.authorize.service;

import com.qzero.bt.common.permission.PermissionNameList;
import com.qzero.bt.dao.AuthorizeInfoDao;
import com.qzero.bt.dao.TokenDao;
import com.qzero.bt.dao.UserInfoDao;
import com.qzero.bt.data.TokenEntity;
import com.qzero.bt.data.AuthorizeInfoEntity;
import com.qzero.bt.data.UserInfoEntity;
import com.qzero.bt.common.permission.PermissionCheck;
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
     * @param userToken tokenId and ownerUserName needed
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public List<TokenEntity> getTokenList(TokenEntity userToken){
        return tokenDao.getAllTokensByOwnerUserName(userToken.getOwnerUserName());
    }

    /**
     * Change password for a user
     * This action will delete all tokens the user used to have(those whose permission level less than global)
     * @param userToken tokenId and ownerUserName needed
     * @param authorizeInfoEntity  passwordHash needed
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public void changePassword(TokenEntity userToken, AuthorizeInfoEntity authorizeInfoEntity){
        AuthorizeInfoEntity authorizeInfoEntityFromDao=authorizeInfoDao.getAuthorizeInfoByName(userToken.getOwnerUserName());
        authorizeInfoEntityFromDao.setPasswordHash(authorizeInfoEntity.getPasswordHash());
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntityFromDao);

        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(userToken.getOwnerUserName());
    }

    /**
     * Freeze the account
     * It will also delete all tokens the user used to have(those whose permission level less than global)
     * @param tokenEntity tokenId and ownerUserName needed
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public void freezeAccount(TokenEntity tokenEntity){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getAuthorizeInfoByName(tokenEntity.getOwnerUserName());
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_FREEZING);
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntity);
        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(tokenEntity.getOwnerUserName());
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public void unfreezeAccount(TokenEntity tokenEntity){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getAuthorizeInfoByName(tokenEntity.getOwnerUserName());
        authorizeInfoEntity.setAuthorizeStatus(AuthorizeInfoEntity.STATUS_ALIVE);
        authorizeInfoDao.updateAuthorizeInfo(authorizeInfoEntity);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_READ_USER_INFO)
    public UserInfoEntity getUserInfo(TokenEntity tokenEntity){
        String userName=tokenEntity.getOwnerUserName();
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo(userName);
        return userInfoEntity;
    }

    @PermissionCheck(PermissionNameList.PERMISSION_UPDATE_USER_INFO)
    public void updateUserInfo(TokenEntity tokenEntity, UserInfoEntity userInfoEntity) {
        userInfoEntity.setUserName(tokenEntity.getOwnerUserName());
        userInfoDao.updateUserInfo(userInfoEntity);
    }

}
