package com.qzero.bt.message.data.message.impl;

import com.qzero.bt.message.data.message.MessageContentManager;
import com.qzero.bt.message.data.message.MessageDao;
import com.qzero.bt.message.data.message.MessageRepository;
import com.qzero.bt.message.data.message.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private MessageContentManager contentManager;

    @Override
    public ChatMessage getOne(String id) throws Exception {
        ChatMessage chatMessage =repository.getOne(id);
        byte[] content=contentManager.getMessageContent(id);
        chatMessage.setContent(content);
        return chatMessage;
    }

    @Override
    public void save(ChatMessage chatMessage) throws Exception {
        repository.save(chatMessage);
        contentManager.saveMessageContent(chatMessage);
    }

    @Override
    public void deleteById(String id) throws Exception {
        repository.deleteById(id);
        contentManager.deleteMessageContent(id);
    }

    @Override
    public List<ChatMessage> getMessagesBySessionId(String sessionId) throws Exception {
        List<ChatMessage> messageList=repository.findBySessionId(sessionId);
        for(ChatMessage message:messageList){
            byte[] content=contentManager.getMessageContent(message.getMessageId());
            message.setContent(content);
        }
        return messageList;
    }

    @Override
    public void deleteAllMessagesBySessionId(String sessionId) throws Exception {
        List<ChatMessage> messageList=repository.findBySessionId(sessionId);
        for(ChatMessage message:messageList){
            repository.delete(message);
            contentManager.deleteMessageContent(message.getMessageId());
        }
    }
}
