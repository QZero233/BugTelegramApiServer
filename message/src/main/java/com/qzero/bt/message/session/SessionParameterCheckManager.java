package com.qzero.bt.message.session;

import com.qzero.bt.message.data.session.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SessionParameterCheckManager {

    public enum Operation{
        OPERATION_UPDATE,
        OPERATION_INSERT,
        OPERATION_DELETE
    }

    private Map<String,SessionParameterChecker> parameterCheckerMap=new HashMap<>();

    @Autowired
    private ChatSessionParameterDao parameterDao;

    @Autowired
    private ChatMemberDao memberDao;

    public SessionParameterCheckManager() {
        parameterCheckerMap.put(ChatSessionParameter.SESSION_TYPE_NORMAL,new NormalSessionParameterChecker());
        parameterCheckerMap.put(ChatSessionParameter.SESSION_TYPE_PERSONAL,new PersonalSessionParameterChecker());
        parameterCheckerMap.put(ChatSessionParameter.SESSION_TYPE_SECRET,new SecretSessionParameterChecker());
    }

    public boolean checkCompulsoryParameter(ChatSession session){
        //Name and type is essential for whatever type of session
        Map<String,ChatSessionParameter> parameterMap=new HashMap<>();

        List<ChatSessionParameter> parameterList=session.getSessionParameters();
        if(parameterList==null || parameterList.isEmpty())
            return false;

        for(ChatSessionParameter parameter:parameterList){
            parameterMap.put(parameter.getParameterName(),parameter);
        }

        if(!parameterMap.containsKey(ChatSessionParameter.NAME_SESSION_NAME) ||
        !parameterMap.containsKey(ChatSessionParameter.NAME_SESSION_TYPE))
            return false;

        String type=parameterMap.get(ChatSessionParameter.NAME_SESSION_TYPE).getParameterValue();

        SessionParameterChecker checker=parameterCheckerMap.get(type);
        if(checker==null)
            throw new IllegalArgumentException("Unknown session type called "+type);

        return checker.checkCompulsory(parameterMap);
    }

    public boolean checkOperationPermission(String sessionId, String parameterName,
                                            String operatorUserName, Operation operation) throws IllegalAccessException {

        //Name and type is necessary
        if(parameterName.equals(ChatSessionParameter.NAME_SESSION_TYPE))
            throw new IllegalAccessException("Session type can not be edited");

        if(parameterName.equals(ChatSessionParameter.NAME_SESSION_NAME) &&
        operation== Operation.OPERATION_DELETE)
            throw new IllegalAccessException("Session name can not be edited");

        if(operation== Operation.OPERATION_INSERT &&
                parameterDao.existsBySessionIdAndAndParameterName(sessionId, parameterName))
            //Check if the same parameter exists
            throw new IllegalArgumentException(String.format("Session parameter named %s already exists, you can not insert it again", parameterName));

        ChatSessionParameter typeParameter=parameterDao.findBySessionIdAndAndParameterName(sessionId,ChatSessionParameter.NAME_SESSION_TYPE);
        String type=typeParameter.getParameterValue();

        SessionParameterChecker checker=parameterCheckerMap.get(type);
        if(checker==null)
            throw new IllegalArgumentException("Unknown session type called "+type);

        ChatMember operator=memberDao.findBySessionIdAndUserName(sessionId,operatorUserName);
        return  checker.checkOperationPermission(sessionId, parameterName, operator, operation);
    }

}
