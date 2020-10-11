package com.qzero.bt.message.data.session;

import com.qzero.bt.common.view.ParameterObject;

import javax.persistence.*;
import java.util.List;

@ParameterObject(name = "ChatSession")
@Table(name = "chat_session")
@Entity
public class ChatSession {

    @Id
    @Basic
    @Column(name = "sessionId")
    private String sessionId;

    @Basic
    @Column(name = "sessionName")
    private String sessionName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessionId")
    private List<ChatMember> chatMembers;

    public ChatSession() {
    }

    public ChatSession(String sessionId, String sessionName, List<ChatMember> chatMembers) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.chatMembers = chatMembers;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public List<ChatMember> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(List<ChatMember> chatMembers) {
        this.chatMembers = chatMembers;
    }

    @Override
    public String toString() {
        return "ChatSessionEntity{" +
                "sessionId='" + sessionId + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", chatMembers=" + chatMembers +
                '}';
    }
}
