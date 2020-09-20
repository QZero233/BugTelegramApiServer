package com.qzero.bt.message.data.message.impl;

import com.qzero.bt.message.data.message.MessageContentManager;
import com.qzero.bt.message.data.message.entity.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Component
public class MessageContentManagerImpl implements MessageContentManager {

    private static final String CONTENT_DIR="data/messages/";

    static {
        new File(CONTENT_DIR).mkdirs();
    }

    @Override
    public void saveMessageContent(ChatMessage chatMessage) throws Exception {
        File file=new File(CONTENT_DIR, chatMessage.getMessageId());
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        fileOutputStream.write(chatMessage.getContent());
        fileOutputStream.close();
    }

    @Override
    public void deleteMessageContent(String messageId) {
        File file=new File(CONTENT_DIR,messageId);
        if(file.exists())
            file.delete();
    }

    @Override
    public byte[] getMessageContent(String messageId) throws  Exception {
        File file=new File(CONTENT_DIR,messageId);
        FileInputStream fileInputStream=new FileInputStream(file);
        return StreamUtils.copyToByteArray(fileInputStream);
    }
}
