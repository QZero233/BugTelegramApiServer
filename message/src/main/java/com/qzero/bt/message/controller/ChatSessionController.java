package com.qzero.bt.message.controller;

import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
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
    private IPackedObjectFactory packedObjectFactory;

    @PostMapping("/")
    public PackedObject createChatSession(@RequestHeader("owner_user_name") String userName,
                                          @RequestBody PackedObject parameter){

        ChatSession chatSession =parameter.parseObject(ChatSession.class);

        String sessionId= UUIDUtils.getRandomUUID();
        chatSession.setSessionId(sessionId);

        chatSession.setChatMembers(Arrays.asList(new ChatMember(sessionId,userName, sessionService.LEVEL_OWNER)));

        sessionService.createSession(chatSession);

        noticeService.addNotice(NoticeDataType.TYPE_SESSION,userName,sessionId);

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

    @PutMapping("/{session_id}/members/")
    public PackedObject addChatMember(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId,
                                      @RequestBody PackedObject parameter) throws ResponsiveException {
//TODO CHECK IF THE USER EXISTS
        if(!sessionService.isOperator(userName,sessionId))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        ChatMember chatMember=parameter.parseObject(ChatMember.class);
        chatMember.setSessionId(sessionId);

        if(chatMember.getLevel()>=ChatSessionService.LEVEL_OWNER)
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

        if(!sessionService.isOperator(userName,sessionId))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        sessionService.removeChatMember(new ChatMember(sessionId,memberUserName,0));

        noticeService.addDeleteNotice(NoticeDataType.TYPE_SESSION,memberUserName,sessionId);

        return packedObjectFactory.getReturnValue(true,null);
    }

}