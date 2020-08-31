package com.qzero.bt.common.observe.remind;

public class ObserveConnectInfo {

    private int port;

    public ObserveConnectInfo() {
    }

    public ObserveConnectInfo(int port) {
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
