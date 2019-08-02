package com.jwtapp.service;

import com.jwtapp.entity.Account;
import com.jwtapp.entity.Role;
import com.jwtapp.exception.ClientError;
import com.jwtapp.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AccountServiceTest {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final AccountService accountService = new AccountServiceImpl(accountRepository, passwordEncoder);

    private final static String USERNAME = "Alex";
    private final static String EMAIL = "Alex@gmail";
    private final static String PASSWORD = "pass";

    @Test
    @DisplayName("If account exist already should return exception")
    void create() {
        when(accountRepository.exists(eq(USERNAME), eq(EMAIL)))
            .thenReturn(true);

        assertThrows(ClientError.class, () -> accountService.create(USERNAME, EMAIL, PASSWORD));
        verify(accountRepository, times(1))
            .exists(eq(USERNAME), eq(EMAIL));
    }

    @Test
    @DisplayName("If account not exist should create new account")
    void create1() {
        when(accountRepository.exists(USERNAME, EMAIL))
            .thenReturn(false);

        accountService.create(USERNAME, EMAIL, PASSWORD);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1))
            .save(accountArgumentCaptor.capture());
        Account account = accountArgumentCaptor.getValue();
        assertEquals(USERNAME, account.getUsername());
        assertEquals(EMAIL, account.getEmail());
        assertTrue(BCrypt.checkpw(PASSWORD, account.getPassword()));
        assertTrue(account.isEnable());
        assertTrue(account.getRoles().contains(Role.USER));
        assertTrue(account.getRoles().contains(Role.BOSS));
    }

    @Test
    void generateJwt() {
    }
}