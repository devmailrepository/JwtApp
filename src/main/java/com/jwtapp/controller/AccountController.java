package com.jwtapp.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwtapp.exception.ParameterMissingException;
import com.jwtapp.service.AccountService;
import com.nimbusds.jose.JOSEException;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.com.google.common.base.Strings;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public void newAccount(@RequestBody final NewUserRequest newUserRequest) {
        if (Strings.isNullOrEmpty(newUserRequest.username)) {
            throw new ParameterMissingException("At least USERNAME not supplied");
        }
        if (Strings.isNullOrEmpty(newUserRequest.email)) {
            throw new ParameterMissingException("At least EMAIL not supplied");
        }
        if (Strings.isNullOrEmpty(newUserRequest.password)) {
            throw new ParameterMissingException("At least EMAIL not supplied");
        }
        accountService.create(newUserRequest.getUsername(), newUserRequest.getEmail(), newUserRequest.getPassword());
    }

    @Getter
    public static class NewUserRequest {
        private final String username;
        private final String email;
        private final String password;

        @JsonCreator
        public NewUserRequest(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
        ) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    @GetMapping(path = "/login")
    public void login(@RequestHeader(value = "Authorization") String param) throws JOSEException, NoSuchAlgorithmException, NoSuchProviderException {
        if (Strings.isNullOrEmpty(param)) {
            throw new ParameterMissingException("Your request is empty");
        }
        accountService.generateJwt(param);
    }
}
