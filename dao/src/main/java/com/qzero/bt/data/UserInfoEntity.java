package com.qzero.bt.data;

import javax.persistence.*;

@Entity
@Table(name = "userInfo")
public class UserInfoEntity {

    public static final int STATUS_OFFLINE=0;
    public static final int STATUS_ONLINE=1;
    public static final int STATUS_BUSY=2;
    public static final int STATUS_LEAVING=3;

    public static final int GROUP_USER=0;
    public static final int GROUP_ADMIN=1;
    public static final int GROUP_SYSTEM_ADMIN=2;


    /**
     * The username
     */
    @Id
    @Basic
    @Column(name = "userName")
    private String userName;


    /**
     * The account status
     * It can be seen by everyone
     * It can be Busy,Leaving etc.
     */
    @Basic
    @Column(name = "accountStatus")
    private int accountStatus;

    /**
     * The motto of the account
     * It can also be seen by eneryone
     */
    @Basic
    @Column(name = "motto")
    private String motto;

    /**
     * The group level which the account belongs to
     */
    @Basic
    @Column(name = "groupLevel")
    private int groupLevel;

    public UserInfoEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "userName='" + userName + '\'' +
                ", accountStatus=" + accountStatus +
                ", motto='" + motto + '\'' +
                ", groupLevel=" + groupLevel +
                '}';
    }
}
