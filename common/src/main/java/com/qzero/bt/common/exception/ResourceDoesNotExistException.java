package com.qzero.bt.common.exception;

public class ResourceDoesNotExistException extends ResponsiveException {

    public ResourceDoesNotExistException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
