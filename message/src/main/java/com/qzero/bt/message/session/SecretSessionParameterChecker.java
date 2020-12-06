package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSessionParameter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecretSessionParameterChecker extends NormalSessionParameterChecker {


    @Override
    public boolean checkCompulsory(Map<String, ChatSessionParameter> parameterMap) {
        if(!parameterMap.containsKey(ChatSessionParameter.NAME_SESSION_SECRET_KEY))
            throw new IllegalArgumentException("Session secret key can not be empty");

        return super.checkCompulsory(parameterMap);
    }

    @Override
    public boolean checkOperationPermission(String sessionId, String parameterName, ChatMember operator, SessionParameterCheckManager.Operation operation) throws IllegalAccessException {
        if(parameterName.equals(ChatSessionParameter.NAME_SESSION_SECRET_KEY))
            throw new IllegalAccessException("Secret key can not be edited or deleted");

        return super.checkOperationPermission(sessionId, parameterName, operator, operation);
    }
}
