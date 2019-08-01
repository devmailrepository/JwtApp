package com.jwtapp.controller;

import com.jwtapp.service.AccountService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.com.fasterxml.jackson.annotation.JsonProperty;

@RestController

public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public void newAccount(@RequestBody final User user) {

        accountService.create(user.getUsername(), user.getEmail(), user.getPassword());
    }


    @Getter
    public static class User {
        private final String username;
        private final String email;
        private final String password;

        public User(
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
    public void login(@RequestHeader(value = "Authorization") String param) {
        System.out.println(param);
        accountService.generateJwt(param);
    }

}
