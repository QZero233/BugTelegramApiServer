package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.ChatMember;

public interface SessionParameterChecker {

    boolean checkCompulsory(String sessionId);
    boolean checkOperationPermission(String sessionId, String parameterName,
                                            ChatMember operator, SessionParameterCheckManager.Operation operation);

}
