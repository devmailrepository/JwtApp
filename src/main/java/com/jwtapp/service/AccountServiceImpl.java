package com.jwtapp.service;

import com.jwtapp.entity.Account;
import com.jwtapp.exception.ClientError;
import com.jwtapp.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Service

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AccountServiceImpl(AccountRepository accountRepository,
                              BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public void create(String username, String email, String password) {
        final boolean isExists = accountRepository.exists(username, email);
        if (isExists)
            throw new ClientError("User with such username or email already exists");
        Account account = new Account(username, email, passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public void generateJwt(String authToken) {
        String preDecodeString = authToken.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(preDecodeString);
        String decodeStr = new String(decodedBytes);
        String[] stringArr = decodeStr.split(":");
        if (stringArr.length != 2) {
            //todo
            System.out.println("TODO some exception");
        }
        final String usernameVerify = stringArr[0];
        final String passwordVerify = stringArr[1];
        final Account accountFromDB = accountRepository.getAccountByName(usernameVerify);
        System.out.println(accountFromDB.toString());
        if (BCrypt.checkpw(passwordVerify, accountFromDB.getPassword()) && accountFromDB.isEnable()) {

        }


    }
}



