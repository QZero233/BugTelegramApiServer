package com.qzero.bt.authorize.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerStatus {

    @Value("1")
    private int statusInInt;

    public int getStatusInInt() {
        return statusInInt;
    }

    public void setStatusInInt(int statusInInt) {
        this.statusInInt = statusInInt;
    }

    @Override
    public String toString() {
        return "ServerStatus{" +
                "statusInInt=" + statusInInt +
                '}';
    }
}
