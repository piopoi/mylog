package com.mysns.api.exception;

public class PostNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
}
