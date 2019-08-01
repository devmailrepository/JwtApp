package com.jwtapp.controller;

import com.jwtapp.entity.Account;
import com.jwtapp.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)

class AccountControllerTest {

    private AccountService accountService = Mockito.mock(AccountService.class);
    private AccountController accountController = new AccountController(accountService);
    private ExceptionCatcher exceptionCatcher = new ExceptionCatcher();

    private MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(accountController)
        .setControllerAdvice(exceptionCatcher)
        .build();


    @Test
    void newAccount() throws Exception {

            Account account = new Account("Alex", "Alex@gmail", "pass");
            when(accountService.create(eq("Alex"), eq("Alex@gmail"), eq("pass"))).thenReturn(account);

            mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("Alex")))
                .andExpect(jsonPath("$.email", Matchers.is("Alex@gmail")))
                .andExpect(jsonPath("$.password", Matchers.is("pass")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }
}