package com.qzero.bt.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
import com.qzero.bt.message.notice.action.ParameterBuilder;
import com.qzero.bt.message.notice.action.SessionNoticeAction;
import com.qzero.bt.message.service.ChatSessionService;
import com.qzero.bt.message.service.MessageService;
import com.qzero.bt.message.service.NoticeService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public PackedObject createChatSession(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody PackedObject parameter) throws JsonProcessingException, ResponsiveException {

        ChatSession chatSession =parameter.parseObject(ChatSession.class);

        String sessionId= UUIDUtils.getRandomUUID();
        chatSession.setSessionId(sessionId);

        chatSession.setChatMembers(Arrays.asList(new ChatMember(sessionId,userDetails.getUsername(), ChatMember.LEVEL_OWNER)));

        sessionService.createSession(chatSession);

        noticeService.addNotice(userDetails.getUsername(),
                new SessionNoticeAction(SessionNoticeAction.ActionType.NEW_SESSION,sessionId,null, userDetails.getUsername()));
        noticeService.remindTargetUser(userDetails.getUsername());

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,sessionId);
        returnValue.addObject(chatSession);
        return returnValue;
    }

    @GetMapping("/{session_id}")
    public PackedObject getChatSession(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable("session_id")String sessionId) throws ResponsiveException {
        ChatSession chatSession =sessionService.findSession(sessionId,userDetails.getUsername());

        chatSession = (ChatSession) Hibernate.unproxy(chatSession);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,null);
        returnValue.addObject(chatSession);
        return returnValue;
    }

    @PostMapping("/{session_id}/members")
    public PackedObject addChatMember(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("session_id")String sessionId,
                                      @RequestParam("member_user_name")String memberUserName) throws ResponsiveException, JsonProcessingException {

        ChatMember chatMember=new ChatMember();
        chatMember.setSessionId(sessionId);
        chatMember.setLevel(ChatMember.LEVEL_NORMAL);
        chatMember.setUserName(memberUserName);

        sessionService.addChatMember(chatMember,userDetails.getUsername());

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.NEW_MEMBER,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).build(),userDetails.getUsername());
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/{session_id}/members")
    public PackedObject removeChatMember(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable("session_id")String sessionId,
                                         @RequestParam("member_user_name")String memberUserName) throws ResponsiveException, JsonProcessingException {

        sessionService.removeChatMember(new ChatMember(sessionId,memberUserName,0),userDetails.getUsername());

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.REMOVE_MEMBER,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).build(),userDetails.getUsername());

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        memberNames.add(memberUserName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);


        return packedObjectFactory.getReturnValue(true,null);
    }


    @PutMapping("/{session_id}/members/{member_user_name}/level")
    public PackedObject updateChatMemberLevel(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable("session_id")String sessionId,
                                         @PathVariable("member_user_name")String memberUserName,
                                         @RequestParam("level") int level) throws ResponsiveException, JsonProcessingException {
        ChatMember chatMember=new ChatMember();
        chatMember.setUserName(memberUserName);
        chatMember.setLevel(level);
        chatMember.setSessionId(sessionId);

        sessionService.updateChatMemberLevel(chatMember,userDetails.getUsername());

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.UPDATE_MEMBER_LEVEL,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).addParameter("level",String.valueOf(level)).build(),
                userDetails.getUsername());

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);


        return packedObjectFactory.getReturnValue(true,null);
    }


    @DeleteMapping("/{session_id}")
    public PackedObject deleteSession(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("session_id")String sessionId) throws Exception {

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.DELETE_SESSION,sessionId,
                null, userDetails.getUsername());
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        sessionService.deleteSession(sessionId, userDetails.getUsername());
        messageService.deleteAllMessageBySessionId(sessionId, userDetails.getUsername());

        return packedObjectFactory.getReturnValue(true,null);
    }


    @GetMapping("/")
    public PackedObject getAllSessions(@AuthenticationPrincipal UserDetails userDetails){
        List<ChatSession> chatSessionList=sessionService.getAllSessions(userDetails.getUsername());

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject("ChatSessionList",chatSessionList);
        return result;
    }

    @PutMapping("/{session_id}/{parameter_name}")
    public PackedObject updateSessionParameter(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("session_id")String sessionId,
                                      @PathVariable("parameter_name")String parameterName,
                                      @RequestParam("parameter_value")String parameterValue) throws JsonProcessingException, IllegalAccessException, ResponsiveException {
        sessionService.updateSessionParameter(sessionId, parameterName,parameterValue,userDetails.getUsername());

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.UPDATE_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).
                        addParameter("parameterValue",parameterValue).build(),userDetails.getUsername());
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/{session_id}/{parameter_name}")
    public PackedObject deleteSessionParameter(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable("session_id")String sessionId,
                                               @PathVariable("parameter_name")String parameterName) throws IllegalAccessException, JsonProcessingException, ResponsiveException {
        sessionService.deleteSessionParameter(sessionId,parameterName, userDetails.getUsername());

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.DELETE_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).build(),userDetails.getUsername());
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PostMapping("/{session_id}/{parameter_name}")
    public PackedObject addSessionParameter(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("session_id")String sessionId,
                                            @PathVariable("parameter_name")String parameterName,
                                            @RequestParam("parameter_value")String parameterValue) throws IllegalAccessException, JsonProcessingException, ResponsiveException {
        sessionService.addSessionParameter(sessionId, parameterName,parameterValue, userDetails.getUsername());

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.ADD_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).
                        addParameter("parameterValue",parameterValue).build(),userDetails.getUsername());
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);

    }

}
