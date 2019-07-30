package com.jwtapp.exception;

import com.github.dockerjava.api.exception.InternalServerErrorException;

import java.util.Objects;

public class ClientError extends RuntimeException {
    public ClientError(String s) {
        super(s);
        Objects.requireNonNull(s);
    }

}
