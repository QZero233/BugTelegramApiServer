package com.qzero.bt.storage.service;

public class TransportBlockCalculator {

    public static long getRestContentSize(long length,long blockLength){
        return length%blockLength;
    }

    public static int getEntireBlockCount(long length,long blockLength){
        int count=(int) (length/blockLength);
        if(count*blockLength>length)
            count--;

        return count;
    }

}
