package com.qzero.bt.message.notice;

import com.qzero.bt.common.view.ParameterObject;

@ParameterObject(name = "NoticeConnectInfo")
public class NoticeConnectInfo {

    private int port;

    public NoticeConnectInfo() {
    }

    public NoticeConnectInfo(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ObserveConnectInfo{" +
                "port=" + port +
                '}';
    }
}
