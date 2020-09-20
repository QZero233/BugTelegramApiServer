package com.qzero.bt.common.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getRandomUUID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }
}
