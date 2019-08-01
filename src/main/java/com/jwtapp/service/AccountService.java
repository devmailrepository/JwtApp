package com.jwtapp.service;

import com.jwtapp.entity.Account;


public interface AccountService {

    Account create(String username, String email, String password);

    void generateJwt(String authToken);

}
