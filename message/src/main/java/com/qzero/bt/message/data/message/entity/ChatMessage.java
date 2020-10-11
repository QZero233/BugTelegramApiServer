package com.qzero.bt.message.data.message.entity;

import com.qzero.bt.common.view.ParameterObject;

import javax.persistence.*;
import java.util.Arrays;

@ParameterObject(name = "ChatMessage")
@Table(name = "messages")
@Entity
public class ChatMessage {

    @Id
    @Basic
    @Column(name = "messageId")
    private String messageId;

    @Basic
    @Column(name = "senderUserName")
    private String senderUserName;

    @Basic
    @Column(name = "sessionId")
    private String sessionId;

    @Transient
    private byte[] content;

    @Basic
    @Column(name = "sendTime")
    private long sendTime;

    @Basic
    @Column(name = "messageStatus")
    private String messageStatus;

    public ChatMessage() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId='" + messageId + '\'' +
                ", senderUserName='" + senderUserName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", content=" + Arrays.toString(content) +
                ", sendTime=" + sendTime +
                ", messageStatus='" + messageStatus + '\'' +
                '}';
    }
}
