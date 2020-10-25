package com.qzero.bt.message.controller;

import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.message.entity.ChatMessage;
import com.qzero.bt.message.data.notice.NoticeDataType;
import com.qzero.bt.message.service.ChatSessionService;
import com.qzero.bt.message.service.MessageService;
import com.qzero.bt.message.service.NoticeService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatSessionService sessionService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @GetMapping("/{message_id}")
    public PackedObject getMessage(@PathVariable("message_id") String messageId) throws Exception {
        ChatMessage message=messageService.getMessage(messageId);
        message= (ChatMessage) Hibernate.unproxy(message);

        PackedObject packedObject=packedObjectFactory.getReturnValue(true,null);
        packedObject.addObject(message);
        return packedObject;
    }

    @PostMapping("/")
    public PackedObject saveMessage(@RequestBody PackedObject parameter,
                                    @RequestHeader("owner_user_name") String userName) throws Exception {

        ChatMessage message=parameter.parseObject(ChatMessage.class);
        message.setMessageId(UUIDUtils.getRandomUUID());
        message.setSenderUserName(userName);
        message.setSendTime(System.currentTimeMillis());
        messageService.saveMessage(message);

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_MESSAGE,memberNames,message.getMessageId(),null);

        return packedObjectFactory.getReturnValue(true,message.getMessageId());
    }

    @DeleteMapping("/{message_id}")
    public PackedObject deleteMessage(@PathVariable("message_id") String messageId) throws Exception {
        ChatMessage message=messageService.getMessage(messageId);
        //TODO CHECK PERMISSION

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_MESSAGE,memberNames,message.getMessageId(),"deleted");

        messageService.deleteMessage(messageId);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/{message_id}/status")
    public PackedObject updateMessageStatus(@PathVariable("message_id") String messageId,
                                            @RequestBody PackedObject parameter) throws Exception {
        ChatMessage message=messageService.getMessage(messageId);
        //TODO CHECK PERMISSION

        String newStatus=parameter.parseObject("messageStatus",String.class);

        messageService.updateMessageStatus(messageId,newStatus);

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_MESSAGE,memberNames,message.getMessageId(),null);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @GetMapping("/")
    public PackedObject getAllMessages(@RequestHeader("owner_user_name") String userName,
                                       @RequestParam("session_id") String sessionId) throws Exception {

        List<ChatMessage> messageList=messageService.getAllMessages(sessionId);
        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("messageList",messageList);
        return returnValue;

    }

}
