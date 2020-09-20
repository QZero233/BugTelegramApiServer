package com.qzero.bt.message.test;

import com.qzero.bt.message.MessageApplication;
import com.qzero.bt.message.data.session.ChatSessionDao;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageApplication.class)
@Transactional
@Rollback(value = false)
public class DataTest {

    @Autowired
    private ChatSessionDao chatRepository;

    private static final String SESSION_ID="testSessionId";

    private Logger log= LoggerFactory.getLogger(getClass());

    @Test
    public void testSaveChatSession(){
        String sessionId= SESSION_ID;

        ChatSession chatSession =new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setSessionName("My session");

        ChatMember qzero=new ChatMember(sessionId,"qzero",2);
        ChatMember minister=new ChatMember(sessionId,"minister",1);
        chatSession.setChatMembers(Arrays.asList(qzero,minister));

        chatRepository.save(chatSession);
    }

    @Test
    public void testQueryChatSession(){
        ChatSession chatSession =chatRepository.findById(SESSION_ID).get();

        log.debug(chatSession +"");
    }


}
