package com.qzero.bt.message.data.message;

import com.qzero.bt.message.data.message.entity.ChatMessage;

import java.util.List;

public interface MessageDao {

    String STATUS_UNREAD="unread";
    String STATUS_READ="read";

    ChatMessage getOne(String id) throws Exception;

    void save(ChatMessage chatMessage) throws Exception;

    void deleteById(String id) throws Exception;

    List<ChatMessage> getMessagesBySessionId(String sessionId) throws Exception ;

}
