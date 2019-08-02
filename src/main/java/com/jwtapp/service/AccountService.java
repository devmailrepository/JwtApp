package com.jwtapp.service;

import com.jwtapp.entity.Account;
import com.nimbusds.jose.JOSEException;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public interface AccountService {

    void create(String username, String email, String password);

    void generateJwt(String authToken) throws JOSEException, NoSuchProviderException, NoSuchAlgorithmException;

}
