package com.qzero.bt.message.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NoticeConnection extends Thread {

    public static final byte REMIND_CONSTANT='c';

    private final Logger log= LoggerFactory.getLogger(getClass());

    private String userName;
    private NoticeConnectionManager.ClientDisconnectedCallback callback;

    private ServerSocket serverSocket;
    private Socket socket;

    public NoticeConnection(String userName, NoticeConnectionManager.ClientDisconnectedCallback callback) {
        this.userName = userName;
        this.callback = callback;
    }

    public int initServer() throws Exception{
        serverSocket=new ServerSocket(0);
        log.debug(String.format("Remind server started on port %d for user %s",serverSocket.getLocalPort(),userName));
        start();
        return serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        super.run();

        try {
            socket=serverSocket.accept();
        }catch (Exception e){
            log.error("Error while accepting client",e);
            callback.disconnected(userName);
        }

    }

    public void remindUpdate(){
        if(socket==null){
            log.error("Can not remind until connected");
            return;
        }

        try {
            OutputStream os=socket.getOutputStream();
            os.write(REMIND_CONSTANT);

            log.debug(String.format("Reminded user %s", userName));
        }catch (Exception e){
            log.error(String.format("Error while reminding user %s", userName),e);
        }
    }

    public void disconnect(){
        if(socket==null){
            log.error("Can not disconnect until connected");
            return;
        }

        try {
            socket.close();
        }catch (Exception e){
            log.error("Error while disconnecting",e);
        }
    }

}
