package com.jwtapp.repository;

import com.jwtapp.entity.Account;


public interface AccountRepository {

    Boolean exists(String username, String email);

    void save(Account account);

    Account getAccountByName(String usernameVerify);

}
