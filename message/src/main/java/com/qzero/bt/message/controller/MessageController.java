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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public PackedObject getMessage(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("message_id") String messageId) throws Exception {
        ChatMessage message=messageService.getMessage(messageId, userDetails.getUsername());
        message= (ChatMessage) Hibernate.unproxy(message);

        PackedObject packedObject=packedObjectFactory.getReturnValue(true,null);
        packedObject.addObject(message);
        return packedObject;
    }

    @PostMapping("/")
    public PackedObject saveMessage(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody PackedObject parameter) throws Exception {

        ChatMessage message=parameter.parseObject(ChatMessage.class);
        message.setMessageId(UUIDUtils.getRandomUUID());
        message.setSenderUserName(userDetails.getUsername());
        message.setSendTime(System.currentTimeMillis());
        messageService.saveMessage(message);

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());

        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.ADD_MESSAGE,
                message.getMessageId(),null, userDetails.getUsername());
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,message.getMessageId());
    }

    @DeleteMapping("/{message_id}")
    public PackedObject deleteMessage(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("message_id") String messageId) throws Exception {
        ChatMessage message=messageService.getMessage(messageId, userDetails.getUsername());

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.DELETE_MESSAGE,
                message.getMessageId(),null, userDetails.getUsername());
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        messageService.deleteMessage(messageId, userDetails.getUsername());

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PutMapping("/{message_id}/status")
    public PackedObject updateMessageStatus(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("message_id") String messageId,
                                            @RequestParam("status")String status) throws Exception {
        ChatMessage message=messageService.getMessage(messageId,userDetails.getUsername());

        messageService.updateMessageStatus(messageId,status,userDetails.getUsername());

        List<String> memberNames=sessionService.findAllMemberNames(message.getSessionId());
        MessageNoticeAction noticeAction=new MessageNoticeAction(MessageNoticeAction.ActionType.UPDATE_MESSAGE_STATUS,message.getMessageId(),
                new ParameterBuilder().addParameter("newStatus",status).build(),userDetails.getUsername());
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @GetMapping("/")
    public PackedObject getAllMessages(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam("session_id") String sessionId) throws Exception {

        List<ChatMessage> messageList=messageService.getAllMessages(sessionId, userDetails.getUsername());
        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject("messageList",messageList);
        return returnValue;

    }

}
