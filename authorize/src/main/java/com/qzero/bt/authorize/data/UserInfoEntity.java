package com.qzero.bt.authorize.data;

import javax.persistence.*;

@Entity
@Table(name = "userInfo")
public class UserInfoEntity {

    public static final int STATUS_ALIVE=0;
    public static final int STATUS_FREEZING=1;

    @Id
    @Basic
    @Column(name = "userName")
    private String userName;

    @Basic
    @Column(name = "accountAuthorizeStatus")
    private int accountAuthorizeStatus;

    @Basic
    @Column(name = "accountStatus")
    private int accountStatus;

    @Basic
    @Column(name = "motto")
    private String motto;

    public UserInfoEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAccountAuthorizeStatus() {
        return accountAuthorizeStatus;
    }

    public void setAccountAuthorizeStatus(int accountAuthorizeStatus) {
        this.accountAuthorizeStatus = accountAuthorizeStatus;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "userName='" + userName + '\'' +
                ", accountAuthorizeStatus=" + accountAuthorizeStatus +
                ", accountStatus=" + accountStatus +
                ", motto='" + motto + '\'' +
                '}';
    }
}
