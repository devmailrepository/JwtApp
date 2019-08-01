package com.jwtapp.exception;

public class ParameterMissingException extends RuntimeException {
    public ParameterMissingException(String s) {
        super(s);
    }
}
