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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessionId")
    private List<ChatMember> chatMembers;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessionId")
    private List<ChatSessionParameter> sessionParameters;

    public ChatSession() {
    }

    public ChatSession(String sessionId, List<ChatMember> chatMembers, List<ChatSessionParameter> sessionParameters) {
        this.sessionId = sessionId;
        this.chatMembers = chatMembers;
        this.sessionParameters = sessionParameters;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatMember> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(List<ChatMember> chatMembers) {
        this.chatMembers = chatMembers;
    }

    public List<ChatSessionParameter> getSessionParameters() {
        return sessionParameters;
    }

    public void setSessionParameters(List<ChatSessionParameter> sessionParameters) {
        this.sessionParameters = sessionParameters;
    }

    @Override
    public String toString() {
        return "ChatSession{" +
                "sessionId='" + sessionId + '\'' +
                ", chatMembers=" + chatMembers +
                ", sessionParameters=" + sessionParameters +
                '}';
    }
}
