package com.jwtapp.controller;

import javax.validation.constraints.NotNull;

public class ErrorResponse {
    private final String description;

    public ErrorResponse(@NotNull final String description) {
        this.description = description;
    }
}
