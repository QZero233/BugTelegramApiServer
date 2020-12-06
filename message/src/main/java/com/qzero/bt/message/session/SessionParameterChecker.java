package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSessionParameter;

import java.util.Map;

public interface SessionParameterChecker {

    boolean checkCompulsory(Map<String, ChatSessionParameter> parameterMap);
    boolean checkOperationPermission(String sessionId, String parameterName,
                                            ChatMember operator, SessionParameterCheckManager.Operation operation) throws IllegalAccessException;

}
