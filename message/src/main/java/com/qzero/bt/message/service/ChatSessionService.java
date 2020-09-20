package com.qzero.bt.message.service;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.data.session.ChatMemberDao;
import com.qzero.bt.message.data.session.ChatSessionDao;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatSessionService {

    public static final int LEVEL_NORMAL=0;
    public static final int LEVEL_OPERATOR=1;
    public static final int LEVEL_OWNER=2;

    @Autowired
    private ChatSessionDao sessionRepository;

    @Autowired
    private ChatMemberDao memberRepository;

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
        if(!sessionRepository.existsById(chatMember.getSessionId()))
            throw new ResponsiveException(ErrorCodeList.CODE_MISSING_RESOURCE,"ChatSession does not exist");

        if(memberRepository.existsBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Can not add a member existing");

        memberRepository.save(chatMember);
    }

    public void removeChatMember(ChatMember chatMember){
        memberRepository.deleteBySessionIdAndUserName(chatMember.getSessionId(),chatMember.getUserName());
    }

    public boolean isMemberIn(String sessionId,String userName){
       return memberRepository.existsBySessionIdAndUserName(sessionId,userName);
    }

    public boolean isOperator(String sessionId,String userName){
        return memberRepository.existsBySessionIdAndUserNameAndLevelIsAfter(sessionId,userName,LEVEL_OPERATOR);
    }

    public List<ChatMember> findAllMembers(String sessionId){
        return memberRepository.findAllBySessionId(sessionId);
    }

    public List<String> findAllMemberNames(String sessionId){
        return memberRepository.findAllNameBySessionId(sessionId);
    }

}
