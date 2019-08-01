package com.jwtapp.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ErrorResponse {
    @JsonProperty
    private final String description;

    public ErrorResponse(@NotNull final String description) {
        this.description = description;
    }
}
