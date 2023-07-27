package com.mysns.api.exception;

public abstract class MylogException extends RuntimeException {

    public MylogException(String message) {
        super(message);
    }

    public MylogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
