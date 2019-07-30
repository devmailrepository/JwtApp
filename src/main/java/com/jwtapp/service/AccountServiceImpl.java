package com.jwtapp.service;

import com.jwtapp.entity.Account;
import com.jwtapp.exception.ClientError;
import com.jwtapp.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    BCryptPasswordEncoder passwordEncoder;


    public AccountServiceImpl(AccountRepository accountRepository,
                              BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Account create(String username, String email, String password) {
        final boolean isExists = accountRepository.exists(username, email);
        if (isExists)
            throw new ClientError("User with such username or email already exists");
        Account account = new Account(username, email, passwordEncoder.encode(password));
        accountRepository.save(account);
        return account;
    }

    public void login(String username, String password) {
        Account account = accountRepository.findAccountByUsername(username);

    }

}
