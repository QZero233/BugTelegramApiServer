package com.qzero.bt.message.controller;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.service.MessageService;
import com.qzero.bt.message.service.NoticeService;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
import com.qzero.bt.message.service.ChatSessionService;
import com.qzero.bt.message.data.notice.NoticeDataType;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/chat_session")
@RestController
public class ChatSessionController {

    @Autowired
    private ChatSessionService sessionService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private IPackedObjectFactory packedObjectFactory;

    @PostMapping("/")
    public PackedObject createChatSession(@RequestHeader("owner_user_name") String userName,
                                          @RequestBody PackedObject parameter){

        ChatSession chatSession =parameter.parseObject(ChatSession.class);

        String sessionId= UUIDUtils.getRandomUUID();
        chatSession.setSessionId(sessionId);

        chatSession.setChatMembers(Arrays.asList(new ChatMember(sessionId,userName, ChatMember.LEVEL_OWNER)));

        sessionService.createSession(chatSession);

        noticeService.addNotice(NoticeDataType.TYPE_SESSION,userName,sessionId);
        noticeService.remindTargetUser(userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(chatSession);
        return returnValue;
    }

    @GetMapping("/{session_id}")
    public PackedObject getChatSession(@RequestHeader("owner_user_name") String userName,
                                       @PathVariable("session_id")String sessionId) throws ResponsiveException {

        if(!sessionService.isMemberIn(sessionId,userName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the members");

        ChatSession chatSession =sessionService.findSession(sessionId);

        chatSession = (ChatSession) Hibernate.unproxy(chatSession);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(chatSession);
        return returnValue;
    }

    @PutMapping("/{session_id}/members")
    public PackedObject addChatMember(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId,
                                      @RequestBody PackedObject parameter) throws ResponsiveException {
        if(!sessionService.isOperator(sessionId,userName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        ChatMember chatMember=parameter.parseObject(ChatMember.class);
        chatMember.setSessionId(sessionId);

        if(chatMember.getLevel()>=ChatMember.LEVEL_OWNER)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Level can not greater than operator");

        sessionService.addChatMember(chatMember);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_SESSION,memberNames,sessionId,null);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/{session_id}/members/{member_user_name}")
    public PackedObject removeChatMember(@RequestHeader("owner_user_name") String userName,
                                         @PathVariable("session_id")String sessionId,
                                         @PathVariable("member_user_name")String memberUserName) throws ResponsiveException {

        //You can quit a session with this method
        if(!memberUserName.equals(userName) && !sessionService.isOperator(sessionId,userName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        sessionService.removeChatMember(new ChatMember(sessionId,memberUserName,0));

        noticeService.addDeleteNotice(NoticeDataType.TYPE_SESSION,memberUserName,sessionId);
        noticeService.remindTargetUser(memberUserName);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_SESSION,memberNames,sessionId,null);


        return packedObjectFactory.getReturnValue(true,null);
    }


    @PutMapping("/{session_id}/members/{member_user_name}")
    public PackedObject updateChatMember(@RequestHeader("owner_user_name") String userName,
                                         @PathVariable("session_id")String sessionId,
                                         @PathVariable("member_user_name")String memberUserName,
                                         @RequestBody PackedObject parameter) throws ResponsiveException {

        //TODO NOTICE
        ChatMember chatMember=parameter.parseObject(ChatMember.class);
        chatMember.setSessionId(sessionId);
        chatMember.setUserName(memberUserName);

        if(chatMember.getLevel()>=ChatMember.LEVEL_OWNER)
            throw new ResponsiveException(ErrorCodeList.CODE_BAD_REQUEST_PARAMETER,"Level can not greater than operator");

        sessionService.updateChatMember(chatMember);
        return packedObjectFactory.getReturnValue(true,null);
    }


    @DeleteMapping("/{session_id}")
    public PackedObject deleteSession(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId) throws Exception {

        sessionService.deleteSession(sessionId);
        messageService.deleteAllMessageBySessionId(sessionId);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeToGroupOfUserAndRemind(NoticeDataType.TYPE_SESSION,memberNames,sessionId,"deleted");

        return packedObjectFactory.getReturnValue(true,null);
    }


    @GetMapping("/")
    public PackedObject getAllSessions(@RequestHeader("owner_user_name") String userName){
        List<ChatSession> chatSessionList=sessionService.getAllSessions(userName);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject("ChatSessionList",chatSessionList);
        return result;
    }

    @PutMapping("/{session_id}")
    public PackedObject updateSessionInfo(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId,
                                      @RequestBody PackedObject parameter){

        //TODO NOTICE
        ChatSession session=parameter.parseObject(ChatSession.class);
        session.setSessionId(sessionId);
        sessionService.updateSessionInfo(session);
        return packedObjectFactory.getReturnValue(true,null);
    }

}
