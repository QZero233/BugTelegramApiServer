package com.qzero.bt.common.observe.remind;

import java.util.HashMap;
import java.util.Map;

public class ObserveConnectionManager {

    private static ObserveConnectionManager instance;

    private Map<String,ObserveConnection> connectionMap;

    public static ObserveConnectionManager getInstance() {
        if(instance==null)
            instance=new ObserveConnectionManager();
        return instance;
    }

    public interface ClientDisconnectedCallback{
        void disconnected(String userName);
    }

    private ObserveConnectionManager(){
        connectionMap=new HashMap<>();
    }

    public ObserveConnectInfo startServer(String userName) throws Exception {
        if(connectionMap.containsKey(userName)){
            connectionMap.get(userName).disconnect();
            connectionMap.remove(userName);
        }

        ObserveConnection connection=new ObserveConnection(userName, new ClientDisconnectedCallback() {
            @Override
            public void disconnected(String userName) {
                connectionMap.remove(userName);
            }
        });

        int port=connection.initServer();
        ObserveConnectInfo connectInfo=new ObserveConnectInfo(port);

        connectionMap.put(userName,connection);

        return connectInfo;
    }

    public void remindUpdate(String userName){
        if(!connectionMap.containsKey(userName))
            return;

        connectionMap.get(userName).remindUpdate();
    }

}
