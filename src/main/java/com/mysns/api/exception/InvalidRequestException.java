package com.mysns.api.exception;

import org.springframework.http.HttpStatus;

/**
 * status: 400
 */
public class InvalidRequestException extends MylogException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
