package com.geercode.elehall.common.exception;

public class ElehallRuntimeException extends RuntimeException implements ElehallException {
    public ElehallRuntimeException(String message) {
        super(message);
    }
}
