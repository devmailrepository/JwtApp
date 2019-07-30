package com.jwtapp.service;

import com.jwtapp.entity.Account;
import org.springframework.stereotype.Service;


public interface AccountService {

  Account create(String username, String email, String password);
//  boolean userVerification();
  void login(String username, String password);
}
