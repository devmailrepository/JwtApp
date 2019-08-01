package com.jwtapp.service;

import com.jwtapp.entity.Account;


public interface AccountService {

    void create(String username, String email, String password);

    void generateJwt(String authToken);

}
