package com.qzero.bt.message.service;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.data.session.ChatMemberDao;
import com.qzero.bt.message.data.session.ChatSessionDao;
import com.qzero.bt.message.data.message.MessageDao;
import com.qzero.bt.message.data.message.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChatMemberDao memberDao;

    public void saveMessage(ChatMessage chatMessage) throws Exception {
        if(!memberDao.existsBySessionIdAndUserName(chatMessage.getSessionId(), chatMessage.getSenderUserName()))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        messageDao.save(chatMessage);
    }

    public ChatMessage getMessage(String messageId,String operatorName) throws Exception {
        ChatMessage message=messageDao.getOne(messageId);

        if(!memberDao.existsBySessionIdAndUserName(message.getSessionId(), operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        return message;
    }

    public void deleteMessage(String messageId,String operatorName) throws Exception {
        ChatMessage message=messageDao.getOne(messageId);

        if(!memberDao.existsBySessionIdAndUserName(message.getSessionId(), operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        messageDao.deleteById(messageId);
    }

    public void updateMessageStatus(String messageId,String newStatus,String operatorName) throws Exception {
        ChatMessage message=messageDao.getOne(messageId);

        if(!memberDao.existsBySessionIdAndUserName(message.getSessionId(), operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        message.setMessageStatus(newStatus);
        messageDao.save(message);
    }

    public List<ChatMessage> getAllMessages(String sessionId,String operatorName) throws Exception {
        if(!memberDao.existsBySessionIdAndUserName(sessionId, operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        return messageDao.getMessagesBySessionId(sessionId);
    }

    public void deleteAllMessageBySessionId(String sessionId,String operatorName) throws Exception{
        if(!memberDao.existsBySessionIdAndUserName(sessionId, operatorName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the session members");

        messageDao.deleteAllMessagesBySessionId(sessionId);
    }

}
