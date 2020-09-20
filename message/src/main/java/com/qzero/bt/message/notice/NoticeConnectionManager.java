package com.qzero.bt.message.notice;

import java.util.HashMap;
import java.util.Map;

public class NoticeConnectionManager {

    private static NoticeConnectionManager instance;

    private Map<String, NoticeConnection> connectionMap;

    public static NoticeConnectionManager getInstance() {
        if(instance==null)
            instance=new NoticeConnectionManager();
        return instance;
    }

    public interface ClientDisconnectedCallback{
        void disconnected(String userName);
    }

    private NoticeConnectionManager(){
        connectionMap=new HashMap<>();
    }

    public NoticeConnectInfo startServer(String userName) throws Exception {
        if(connectionMap.containsKey(userName)){
            connectionMap.get(userName).disconnect();
            connectionMap.remove(userName);
        }

        NoticeConnection connection=new NoticeConnection(userName, new ClientDisconnectedCallback() {
            @Override
            public void disconnected(String userName) {
                connectionMap.remove(userName);
            }
        });

        int port=connection.initServer();
        NoticeConnectInfo connectInfo=new NoticeConnectInfo(port);

        connectionMap.put(userName,connection);

        return connectInfo;
    }

    public void remindUpdate(String userName){
        if(!connectionMap.containsKey(userName))
            return;

        connectionMap.get(userName).remindUpdate();
    }

}
