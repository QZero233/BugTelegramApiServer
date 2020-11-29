package com.qzero.bt.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qzero.bt.common.exception.ErrorCodeList;
import com.qzero.bt.common.exception.ResponsiveException;
import com.qzero.bt.message.data.session.ChatSessionParameter;
import com.qzero.bt.message.notice.action.ParameterBuilder;
import com.qzero.bt.message.notice.action.SessionNoticeAction;
import com.qzero.bt.message.service.MessageService;
import com.qzero.bt.message.service.NoticeService;
import com.qzero.bt.common.utils.UUIDUtils;
import com.qzero.bt.common.view.IPackedObjectFactory;
import com.qzero.bt.common.view.PackedObject;
import com.qzero.bt.message.data.session.ChatMember;
import com.qzero.bt.message.data.session.ChatSession;
import com.qzero.bt.message.service.ChatSessionService;
import com.qzero.bt.message.session.SessionParameterCheckManager;
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

    @Autowired
    private SessionParameterCheckManager parameterCheckManager;

    @PostMapping("/")
    public PackedObject createChatSession(@RequestHeader("owner_user_name") String userName,
                                          @RequestBody PackedObject parameter) throws JsonProcessingException {

        ChatSession chatSession =parameter.parseObject(ChatSession.class);

        String sessionId= UUIDUtils.getRandomUUID();
        chatSession.setSessionId(sessionId);

        chatSession.setChatMembers(Arrays.asList(new ChatMember(sessionId,userName, ChatMember.LEVEL_OWNER)));

        sessionService.createSession(chatSession);

        if(!parameterCheckManager.checkCompulsoryParameter(sessionId))
            throw new IllegalArgumentException("Session parameter is illegal");

        noticeService.addNotice(userName,new SessionNoticeAction(SessionNoticeAction.ActionType.NEW_SESSION,sessionId,null,userName));
        noticeService.remindTargetUser(userName);

        PackedObject returnValue=packedObjectFactory.getReturnValue(true,sessionId);
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

    @PostMapping("/{session_id}/members")
    public PackedObject addChatMember(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId,
                                      @RequestParam("member_user_name")String memberUserName) throws ResponsiveException, JsonProcessingException {
        if(!sessionService.isOperator(sessionId,userName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        ChatMember chatMember=new ChatMember();
        chatMember.setSessionId(sessionId);
        chatMember.setLevel(ChatMember.LEVEL_NORMAL);
        chatMember.setUserName(memberUserName);

        sessionService.addChatMember(chatMember);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.NEW_MEMBER,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).build(),userName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/{session_id}/members")
    public PackedObject removeChatMember(@RequestHeader("owner_user_name") String userName,
                                         @PathVariable("session_id")String sessionId,
                                         @RequestParam("member_user_name")String memberUserName) throws ResponsiveException, JsonProcessingException {

        //You can quit a session with this method
        if(!memberUserName.equals(userName) && !sessionService.isOperator(sessionId,userName))
            throw new ResponsiveException(ErrorCodeList.CODE_PERMISSION_DENIED,"You are not one of the operators");

        sessionService.removeChatMember(new ChatMember(sessionId,memberUserName,0));

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.REMOVE_MEMBER,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).build(),userName);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        memberNames.add(memberUserName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);


        return packedObjectFactory.getReturnValue(true,null);
    }


    @PutMapping("/{session_id}/members/{member_user_name}/level")
    public PackedObject updateChatMemberLevel(@RequestHeader("owner_user_name") String userName,
                                         @PathVariable("session_id")String sessionId,
                                         @PathVariable("member_user_name")String memberUserName,
                                         @RequestParam("level") int level) throws ResponsiveException, JsonProcessingException {
        ChatMember chatMember=new ChatMember();
        chatMember.setUserName(memberUserName);
        chatMember.setLevel(level);
        chatMember.setSessionId(sessionId);

        sessionService.updateChatMemberLevel(chatMember);

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.UPDATE_MEMBER_LEVEL,sessionId,
                new ParameterBuilder().addParameter("memberUserName",memberUserName).addParameter("level",String.valueOf(level)).build(),
                userName);

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);


        return packedObjectFactory.getReturnValue(true,null);
    }


    @DeleteMapping("/{session_id}")
    public PackedObject deleteSession(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId) throws Exception {

        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.DELETE_SESSION,sessionId,
                null,userName);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        sessionService.deleteSession(sessionId);
        messageService.deleteAllMessageBySessionId(sessionId);

        return packedObjectFactory.getReturnValue(true,null);
    }


    @GetMapping("/")
    public PackedObject getAllSessions(@RequestHeader("owner_user_name") String userName){
        List<ChatSession> chatSessionList=sessionService.getAllSessions(userName);

        PackedObject result=packedObjectFactory.getReturnValue(true,null);
        result.addObject("ChatSessionList",chatSessionList);
        return result;
    }

    @PutMapping("/{session_id}/{parameter_name}")
    public PackedObject updateSessionParameter(@RequestHeader("owner_user_name") String userName,
                                      @PathVariable("session_id")String sessionId,
                                      @PathVariable("parameter_name")String parameterName,
                                      @RequestParam("parameter_value")String parameterValue) throws JsonProcessingException, IllegalAccessException {


        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,userName, SessionParameterCheckManager.Operation.OPERATION_UPDATE))
            throw new IllegalAccessException("You have no access to the parameter named "+parameterName);

        ChatSession session=new ChatSession();
        session.setSessionId(sessionId);
        sessionService.updateSessionParameter(session, parameterName,parameterValue);

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.UPDATE_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).
                        addParameter("parameterValue",parameterValue).build(),userName);
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @DeleteMapping("/{session_id}/{parameter_name}")
    public PackedObject deleteSessionParameter(@RequestHeader("owner_user_name") String userName,
                                               @PathVariable("session_id")String sessionId,
                                               @PathVariable("parameter_name")String parameterName) throws IllegalAccessException, JsonProcessingException {

        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,userName, SessionParameterCheckManager.Operation.OPERATION_DELETE))
            throw new IllegalAccessException("You have no access to the parameter named "+parameterName);

        ChatSession session=new ChatSession();
        session.setSessionId(sessionId);
        sessionService.deleteSessionParameter(session,parameterName);

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.DELETE_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).build(),userName);
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);
    }

    @PostMapping("/{session_id}/{parameter_name}")
    public PackedObject addSessionParameter(@RequestHeader("owner_user_name") String userName,
                                            @PathVariable("session_id")String sessionId,
                                            @PathVariable("parameter_name")String parameterName,
                                            @RequestParam("parameter_value")String parameterValue) throws IllegalAccessException, JsonProcessingException {

        if(!parameterCheckManager.checkOperationPermission(sessionId,parameterName,userName, SessionParameterCheckManager.Operation.OPERATION_INSERT))
            throw new IllegalAccessException("You have no access to the parameter named "+parameterName);

        ChatSession session=new ChatSession();
        session.setSessionId(sessionId);
        sessionService.addSessionParameter(session, parameterName,parameterValue);

        SessionNoticeAction noticeAction=new SessionNoticeAction(SessionNoticeAction.ActionType.ADD_SESSION_PARAMETER,sessionId,
                new ParameterBuilder().addParameter("parameterName",parameterName).
                        addParameter("parameterValue",parameterValue).build(),userName);
        List<String> memberNames=sessionService.findAllMemberNames(sessionId);
        noticeService.addNoticeForGroupOfUsersAndRemind(memberNames,noticeAction);

        return packedObjectFactory.getReturnValue(true,null);

    }

}
