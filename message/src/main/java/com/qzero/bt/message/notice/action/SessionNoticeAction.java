package com.qzero.bt.message.notice.action;

import java.util.Map;

public class SessionNoticeAction extends NoticeAction {

    public enum ActionType{
        NEW_SESSION("newSession"),
        NEW_MEMBER("newMember"),
        REMOVE_MEMBER("removeMember"),
        UPDATE_MEMBER_LEVEL("updateMemberLevel"),
        DELETE_SESSION("deleteSession"),
        UPDATE_SESSION_PARAMETER("updateSessionParameter");
        ActionType(String actionInString) {
            this.actionInString = actionInString;
        }

        String actionInString;
        public String getActionInString() {
            return actionInString;
        }
    }

    public SessionNoticeAction(ActionType actionType, String dataId, Map<String, String> parameter, String operator) {
        super("chatSession", actionType.getActionInString(), dataId, parameter, operator);
    }
}
