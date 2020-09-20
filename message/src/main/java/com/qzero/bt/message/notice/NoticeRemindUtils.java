package com.qzero.bt.message.notice;

public class NoticeRemindUtils {

    public static void remindUser(String userName){
        NoticeConnectionManager manager= NoticeConnectionManager.getInstance();
        manager.remindUpdate(userName);
    }

}
