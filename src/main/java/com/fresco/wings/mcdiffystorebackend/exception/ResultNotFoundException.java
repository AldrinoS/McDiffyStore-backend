package com.fresco.wings.mcdiffystorebackend.exception;

public class ResultNotFoundException extends RuntimeException{

    public ResultNotFoundException(String message) {
        super(message);
    }

    public ResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
