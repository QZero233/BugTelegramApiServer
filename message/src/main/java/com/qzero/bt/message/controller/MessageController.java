package com.qzero.bt.message.controller;

import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.message.entity.ChatMessage;
import com.qzero.bt.message.notice.action.MessageNoticeAction;
import com.qzero.bt.message.notice.action.NoticeAction;
import com.qzero.bt.message.notice.action.ParameterBuilder;
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

        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.ADD_MESSAGE,message.getMessageId(),null,userName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,message.getMessageId());
    }

    @DeleteMapping("/{message_id}")
    public PackedObject deleteMessage(@PathVariable("message_id") String messageId,
                                      @RequestHeader("owner_user_name") String userName) throws Exception {
        ChatMessage message=messageService.getMessage(messageId);
        //TODO CHECK PERMISSION

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.DELETE_MESSAGE,message.getMessageId(),null,userName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        messageService.deleteMessage(messageId);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/{message_id}/status")
    public PackedObject updateMessageStatus(@PathVariable("message_id") String messageId,
                                            @RequestHeader("owner_user_name") String userName,
                                            @RequestParam("status")String status) throws Exception {
        ChatMessage message=messageService.getMessage(messageId);
        //TODO CHECK PERMISSION

        messageService.updateMessageStatus(messageId,status);

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.UPDATE_MESSAGE_STATUS,message.getMessageId(),
                new ParameterBuilder().addParameter("newStatus",status).build(),userName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

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
