package com.qzero.bt.message.service;

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

    public void saveMessage(ChatMessage chatMessage) throws Exception {
        messageDao.save(chatMessage);
    }

    public ChatMessage getMessage(String messageId) throws Exception {
        return messageDao.getOne(messageId);
    }

    public void deleteMessage(String messageId) throws Exception {
        messageDao.deleteById(messageId);
    }

    public void updateMessageStatus(String messageId,String newStatus) throws Exception {
        ChatMessage message=messageDao.getOne(messageId);
        message.setMessageStatus(newStatus);
        messageDao.save(message);
    }

    public List<ChatMessage> getAllMessages(String sessionId) throws Exception {
        return messageDao.getMessagesBySessionId(sessionId);
    }

}
