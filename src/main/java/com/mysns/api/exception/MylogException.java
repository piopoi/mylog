package com.mysns.api.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class MylogException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public MylogException(String message) {
        super(message);
    }

    public MylogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message)   {
        validation.put(fieldName, message);
    }
}
