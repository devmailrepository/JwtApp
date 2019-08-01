package com.jwtapp.controller;

import com.jwtapp.exception.ClientError;
import com.jwtapp.exception.CreatorException;
import com.jwtapp.exception.ParameterMissingException;
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

    @ExceptionHandler(ParameterMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ParameterMissingException ex) {

        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(CreatorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(CreatorException ex) {

        return new ErrorResponse(ex.getMessage());
    }

}
