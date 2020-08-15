package com.qzero.bt.data;


import javax.persistence.*;

@Entity
@Table(name = "authorizeInfo")
public class AuthorizeInfoEntity {

    public static final int STATUS_ALIVE=0;
    public static final int STATUS_FREEZING=1;

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
     * The authorize status of the account
     * It can determine whether login action is approved or not
     */
    @Basic
    @Column(name = "authorizeStatus")
    private int authorizeStatus;

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

    public int getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(int authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    @Override
    public String toString() {
        return "AuthorizeInfoEntity{" +
                "userName='" + userName + '\'' +
                ", codeHash='" + codeHash + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", authorizeStatus=" + authorizeStatus +
                '}';
    }
}
