package com.jwtapp.controller;

import com.jwtapp.service.AccountService;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
//    @GetMapping(path = "/auth/login")
//    public Account getLoginData (@RequestParam(value = "realm") String param) {
//        accountService.
//    }

}
