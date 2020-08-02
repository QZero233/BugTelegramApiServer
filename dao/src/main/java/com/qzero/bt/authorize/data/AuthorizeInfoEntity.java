package com.qzero.bt.authorize.data;


import javax.persistence.*;

@Entity
@Table(name = "authorizeInfo")
public class AuthorizeInfoEntity {

    /**
     * The name of the user
     * It's the identity of a user
     */
    @Id
    @Basic
    @Column(name = "userName")
    private String userName;

    /**
     * Hash value(lowercase) of code
     * Code is the most important,which can get access to all privileges of the account
     */
    @Basic
    @Column(name = "codeHash")
    private String codeHash;

    /**
     * Hash value(lowercase) of password
     * Password is used for authorizing in other apps
     */
    @Basic
    @Column(name = "passwordHash")
    private String passwordHash;

    /**
     * The status of the account
     */
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserInfoEntity userInfoEntity;

    public AuthorizeInfoEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
    }

    public void setUserInfoEntity(UserInfoEntity userInfoEntity) {
        this.userInfoEntity = userInfoEntity;
    }

    @Override
    public String toString() {
        return "AuthorizeInfoEntity{" +
                "userName='" + userName + '\'' +
                ", codeHash='" + codeHash + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", userInfoEntity=" + userInfoEntity +
                '}';
    }
}
