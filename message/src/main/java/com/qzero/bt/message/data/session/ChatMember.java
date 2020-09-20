package com.qzero.bt.message.data.session;

import javax.persistence.*;

@Table(name = "chat_members")
@Entity
public class ChatMember {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "chatMemberId")
    private int chatMemberId;

    @Basic
    @Column(name = "sessionId")
    private String sessionId;

    @Basic
    @Column(name = "userName")
    private String userName;

    @Basic
    @Column(name = "level")
    private int level;

    public ChatMember() {
    }

    public ChatMember(String sessionId, String userName, int level) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.level = level;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "sessionId='" + sessionId + '\'' +
                ", userName='" + userName + '\'' +
                ", level=" + level +
                '}';
    }
}
