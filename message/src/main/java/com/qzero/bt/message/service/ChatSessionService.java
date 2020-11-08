package com.qzero.bt.message.service;

import com.qzero.bt.common.authorize.dao.UserInfoDao;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.data.session.ChatMemberDao;
import com.qzero.bt.message.data.session.ChatSessionDao;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
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
    private UserInfoDao userInfoDao;

    public void createSession(ChatSession chatSession){
        sessionRepository.save(chatSession);
    }

    public void deleteSession(String sessionId){
        sessionRepository.deleteById(sessionId);
    }

    public ChatSession findSession(String sessionId){
        return sessionRepository.getOne(sessionId);
    }

    public void addChatMember(ChatMember chatMember) throws ResponsiveException {
        if(userInfoDao.getUserInfo(chatMember.getUserName())==null)
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"Target user does not exist");

        if(!sessionRepository.existsById(chatMember.getSessionId()))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"ChatSession does not exist");

        if(memberRepository.existsBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Can not add a member existing");

        memberRepository.save(chatMember);
    }

    public void removeChatMember(ChatMember chatMember){
        memberRepository.deleteBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName());
    }

    public void updateChatMember(ChatMember chatMember) throws ResponsiveException {
        ChatMember origin=memberRepository.findBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName());
        if(origin==null)
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"Chat member does not exist");

        if(origin.getLevel()==ChatMember.LEVEL_OWNER && chatMember.getLevel()<ChatMember.LEVEL_OWNER)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Level can not be less than owner");

        memberRepository.save(chatMember);
    }

    public boolean isMemberIn(String sessionId,String userName){
       return memberRepository.existsBySessionIdAndUserName(sessionId,userName);
    }

    public boolean isOperator(String sessionId,String userName){
        return memberRepository.existsBySessionIdAndUserNameAndLevelIsGreaterThanEqual(sessionId,userName,ChatMember.LEVEL_OPERATOR);
    }

    public List<ChatMember> findAllMembers(String sessionId){
        return memberRepository.findAllBySessionId(sessionId);
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

    public void updateSessionInfo(ChatSession session){
        ChatSession origin=sessionRepository.getOne(session.getSessionId());
        origin.setSessionName(session.getSessionName());
        sessionRepository.save(origin);
    }

}
