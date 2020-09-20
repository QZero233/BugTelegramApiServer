package com.qzero.bt.admin.data;

import com.qzero.bt.common.authorize.data.UserInfoEntity;

public class UserInfoForAdmin {

    private UserInfoEntity userInfoEntity;
    private int authorizeStatus;

    public UserInfoForAdmin() {
    }

    public UserInfoForAdmin(UserInfoEntity userInfoEntity, int authorizeStatus) {
        this.userInfoEntity = userInfoEntity;
        this.authorizeStatus = authorizeStatus;
    }

    public UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
    }

    public void setUserInfoEntity(UserInfoEntity userInfoEntity) {
        this.userInfoEntity = userInfoEntity;
    }

    public int getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(int authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    @Override
    public String toString() {
        return "UserInfoForAdmin{" +
                "userInfoEntity=" + userInfoEntity +
                ", authorizeStatus=" + authorizeStatus +
                '}';
    }
}
