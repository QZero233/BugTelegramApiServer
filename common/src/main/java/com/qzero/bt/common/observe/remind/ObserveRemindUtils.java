package com.qzero.bt.common.observe.remind;

public class ObserveRemindUtils {

    public static void remindUser(String userName){
        ObserveConnectionManager manager=ObserveConnectionManager.getInstance();
        manager.remindUpdate(userName);
    }

}
