package com.qzero.bt.message.data.message;

import com.qzero.bt.message.data.message.entity.ChatMessage;

public interface MessageContentManager {

    void saveMessageContent(ChatMessage chatMessage) throws Exception;

    void deleteMessageContent(String messageId) throws Exception;

    byte[] getMessageContent(String messageId) throws Exception;

}
