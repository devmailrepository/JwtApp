package com.jwtapp.controller;

import com.jwtapp.exception.ClientError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class ExceptionCatcher {
    @ExceptionHandler(ClientError.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(ClientError ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
