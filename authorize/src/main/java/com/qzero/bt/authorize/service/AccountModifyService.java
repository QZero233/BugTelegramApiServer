package com.qzero.bt.authorize.service;

import com.qzero.bt.authorize.permission.PermissionNameList;
import com.qzero.bt.authorize.dao.AuthorizeInfoDao;
import com.qzero.bt.authorize.dao.TokenDao;
import com.qzero.bt.authorize.dao.UserInfoDao;
import com.qzero.bt.authorize.data.TokenEntity;
import com.qzero.bt.authorize.data.AuthorizeInfoEntity;
import com.qzero.bt.authorize.data.UserInfoEntity;
import com.qzero.bt.authorize.permission.PermissionCheck;
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
        authorizeInfoEntity.setUserName(userToken.getOwnerUserName());
        authorizeInfoDao.updatePassword(authorizeInfoEntity);
        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(userToken.getOwnerUserName());
    }

    /**
     * Freeze the account
     * It will also delete all tokens the user used to have(those whose permission level less than global)
     * @param tokenEntity tokenId and ownerUserName needed
     */
    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public void freezeAccount(TokenEntity tokenEntity){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getUserByName(tokenEntity.getOwnerUserName());
        authorizeInfoEntity.getUserInfoEntity().setAccountStatus(UserInfoEntity.STATUS_FREEZING);
        authorizeInfoDao.updateUserInfo(authorizeInfoEntity);
        tokenDao.deleteAllTokensLessThanGlobalByOwnerUserName(tokenEntity.getOwnerUserName());
    }

    @PermissionCheck(PermissionNameList.PERMISSION_MODIFY_ACCOUNT)
    public void unfreezeAccount(TokenEntity tokenEntity){
        AuthorizeInfoEntity authorizeInfoEntity = authorizeInfoDao.getUserByName(tokenEntity.getOwnerUserName());
        authorizeInfoEntity.getUserInfoEntity().setAccountStatus(UserInfoEntity.STATUS_ALIVE);
        authorizeInfoDao.updateUserInfo(authorizeInfoEntity);
    }

    @PermissionCheck(PermissionNameList.PERMISSION_READ_USER_INFO)
    public UserInfoEntity getUserInfo(TokenEntity tokenEntity){
        String userName=tokenEntity.getOwnerUserName();
        UserInfoEntity userInfoEntity=userInfoDao.getUserInfo(userName);
        return userInfoEntity;
    }

}
