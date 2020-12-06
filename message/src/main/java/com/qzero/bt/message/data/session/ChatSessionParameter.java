package com.qzero.bt.message.data.session;

import com.qzero.bt.common.view.ParameterObject;

import javax.persistence.*;

@Entity
@Table(name = "chat_session_parameter")
@ParameterObject(name = "ChatSessionParameter")
public class ChatSessionParameter {

    public static final String NAME_SESSION_NAME="sessionName";
    public static final String NAME_SESSION_TYPE="sessionType";
    public static final String NAME_SESSION_SECRET_KEY="sessionSecretKey";

    public static final String SESSION_TYPE_NORMAL="normal";
    public static final String SESSION_TYPE_SECRET="secret";
    public static final String SESSION_TYPE_PERSONAL="personal";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parameterId")
    private Long parameterId;

    @Basic
    @Column(name = "sessionId")
    private String sessionId;

    @Basic
    @Column(name = "parameterName")
    private String parameterName;

    @Basic
    @Column(name = "parameterValue")
    private String parameterValue;

    public ChatSessionParameter() {
    }

    public ChatSessionParameter(Long parameterId, String sessionId, String parameterName, String parameterValue) {
        this.parameterId = parameterId;
        this.sessionId = sessionId;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return "ChatSessionParameter{" +
                "parameterId=" + parameterId +
                ", sessionId='" + sessionId + '\'' +
                ", parameterName='" + parameterName + '\'' +
                ", parameterValue='" + parameterValue + '\'' +
                '}';
    }
}
