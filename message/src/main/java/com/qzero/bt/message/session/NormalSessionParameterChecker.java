package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSessionParameter;

import java.util.Map;

public class NormalSessionParameterChecker implements SessionParameterChecker {

    @Override
    public boolean checkCompulsory(Map<String, ChatSessionParameter> parameterMap) {
        return true;
    }

    @Override
    public boolean checkOperationPermission(String sessionId, String parameterName, ChatMember operator, SessionParameterCheckManager.Operation operation) throws IllegalAccessException {
        switch (parameterName){
            case ChatSessionParameter.NAME_SESSION_NAME:
                if(operator.getLevel()<ChatMember.LEVEL_OPERATOR)
                    throw new IllegalAccessException("You have no permission");
                return true;
            default:
                //Everyone can delete a out-dating parameter
                if(operation== SessionParameterCheckManager.Operation.OPERATION_DELETE)
                    return true;
                else
                    throw new IllegalArgumentException("Unknown session parameter named "+parameterName);
        }
    }
}
