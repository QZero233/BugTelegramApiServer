package com.qzero.bt.authorize.exception;

public class PermissionDeniedException extends ResponsiveException {

    public PermissionDeniedException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
