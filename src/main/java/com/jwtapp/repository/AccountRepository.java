package com.jwtapp.repository;

import com.jwtapp.entity.Account;
import org.springframework.stereotype.Repository;


public interface AccountRepository {

    Boolean exists(String username, String email);

    void save(Account account);

    Account findAccountByUsername(String username);

}
