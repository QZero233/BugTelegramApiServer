package com.qzero.bt.message.service;

import com.qzero.bt.common.authorize.dao.UserInfoRepository;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.data.session.*;
import com.qzero.bt.message.session.SessionParameterCheckManager;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChatSessionService {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private ChatSessionDao sessionRepository;

    @Autowired
    private ChatMemberDao memberRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ChatSessionParameterDao parameterDao;

    @Autowired
    private SessionParameterCheckManager parameterCheckManager;

    public void createSession(ChatSession chatSession) throws ResponsiveException {
        if(!parameterCheckManager.checkCompulsoryParameter(chatSession))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Session parameter is illegal");

        sessionRepository.save(chatSession);
    }

    public void deleteSession(String sessionId,String operatorName) throws ResponsiveException {
        if(!memberRepository.existsBySessionIdAndUserNameAndLevelIsGreaterThanEqual(sessionId,operatorName,ChatMember.LEVEL_OWNER))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not the session owner");
        sessionRepository.deleteById(sessionId);
    }

    public ChatSession findSession(String sessionId,String operatorName) throws ResponsiveException {
        if(!memberRepository.existsBySessionIdAndUserName(sessionId,operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the members");
        return sessionRepository.getOne(sessionId);
    }

    public void addChatMember(ChatMember chatMember,String operatorName) throws ResponsiveException {
        if(!isOperator(chatMember.getSessionId(),operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not the session operator");

        if(!userInfoRepository.existsById(chatMember.getUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"Target user does not exist");

        if(!sessionRepository.existsById(chatMember.getSessionId()))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"ChatSession does not exist");

        if(memberRepository.existsBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Can not add a member existing");

        memberRepository.save(chatMember);
    }

    public void removeChatMember(ChatMember chatMember,String operatorName) throws ResponsiveException {
        //You can quit a session with this method
        if(!chatMember.equals(operatorName) && !isOperator(chatMember.getSessionId(),operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not the session operator");
        memberRepository.deleteBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName());
    }

    public void updateChatMemberLevel(ChatMember chatMember,String operatorName) throws ResponsiveException {
        if(!isOperator(chatMember.getSessionId(),operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not the session operator");

        ChatMember origin=memberRepository.findBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName());
        if(origin==null)
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"Chat member does not exist");

        if(origin.getLevel()==ChatMember.LEVEL_OWNER && chatMember.getLevel()<ChatMember.LEVEL_OWNER)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Level can not be less than owner");

        if(origin.getLevel()<ChatMember.LEVEL_OWNER && chatMember.getLevel()>=ChatMember.LEVEL_OWNER)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Level can not greater than operator");

        origin.setLevel(chatMember.getLevel());

        memberRepository.save(origin);
    }

    public boolean isOperator(String sessionId,String userName){
        return memberRepository.existsBySessionIdAndUserNameAndLevelIsGreaterThanEqual(sessionId,userName,ChatMember.LEVEL_OPERATOR);
    }

    public List<String> findAllMemberNames(String sessionId){
        return memberRepository.findAllNameBySessionId(sessionId);
    }

    public List<ChatSession> getAllSessions(String userName){
        List<String> sessionIdList=memberRepository.findAllSessionIdByName(userName);

        List<ChatSession> result=new ArrayList<>();
        for(String sessionId:sessionIdList){
            ChatSession session=sessionRepository.getOne(sessionId);
            session= (ChatSession) Hibernate.unproxy(session);
            result.add(session);
        }

        return result;
    }

    public void updateSessionParameter(String sessionId,String parameterName,String parameterValue,String operatorName) throws IllegalAccessException, ResponsiveException {
        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,
                operatorName, SessionParameterCheckManager.Operation.OPERATION_UPDATE))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You have no access to the parameter named "+parameterName);

        ChatSessionParameter parameter=parameterDao.findBySessionIdAndAndParameterName(sessionId,parameterName);
        parameter.setParameterValue(parameterValue);
        parameterDao.save(parameter);
    }

    public void deleteSessionParameter(String sessionId,String parameterName,String operatorName) throws ResponsiveException, IllegalAccessException {
        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,
                operatorName, SessionParameterCheckManager.Operation.OPERATION_DELETE))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You have no access to the parameter named "+parameterName);

        ChatSessionParameter parameter=parameterDao.findBySessionIdAndAndParameterName(sessionId,parameterName);
        parameterDao.delete(parameter);
    }

    public void addSessionParameter(String sessionId,String parameterName,String parameterValue,String operatorName) throws ResponsiveException, IllegalAccessException {
        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,
                operatorName, SessionParameterCheckManager.Operation.OPERATION_INSERT))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You have no access to the parameter named "+parameterName);


        ChatSessionParameter parameter=new ChatSessionParameter(null,sessionId,parameterName,parameterValue);
        parameter.setSessionId(sessionId);
        parameterDao.save(parameter);
    }

}
