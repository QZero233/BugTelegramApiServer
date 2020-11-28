package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSessionParameter;

public class NormalSessionParameterChecker implements SessionParameterChecker {

    @Override
    public boolean checkCompulsory(String sessionId) {
        return true;
    }

    @Override
    public boolean checkOperationPermission(String sessionId, String parameterName, ChatMember operator, SessionParameterCheckManager.Operation operation) {
        switch (parameterName){
            case ChatSessionParameter.NAME_SESSION_NAME:
                return operator.getLevel()>=ChatMember.LEVEL_OPERATOR;
            default:
                //Everyone can delete a out-dating parameter
                if(operation== SessionParameterCheckManager.Operation.OPERATION_DELETE)
                    return true;
                else
                    throw new IllegalArgumentException("Unknown session parameter named "+parameterName);
        }
    }
}
