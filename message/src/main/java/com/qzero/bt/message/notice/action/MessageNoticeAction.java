package com.qzero.bt.message.notice.action;

import java.util.Map;

public class MessageNoticeAction extends NoticeAction {

    public enum ActionType{
        ADD_MESSAGE("addMessage"),
        DELETE_MESSAGE("deleteMessage"),
        UPDATE_MESSAGE_STATUS("updateMessageStatus");
        ActionType(String actionInString) {
            this.actionInString = actionInString;
        }

        String actionInString;
        public String getActionInString() {
            return actionInString;
        }
    }

    public MessageNoticeAction(ActionType actionType, String dataId, Map<String, String> parameter, String operator) {
        super("chatMessage", actionType.getActionInString(), dataId, parameter, operator);
    }
}
