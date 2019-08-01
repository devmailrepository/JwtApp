package com.jwtapp.controller;

import com.jwtapp.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class AccountControllerTest {

    private AccountService accountService = Mockito.mock(AccountService.class);
    private AccountController accountController = new AccountController(accountService);
    private ExceptionCatcher exceptionCatcher = new ExceptionCatcher();

    private MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(accountController)
        .setControllerAdvice(exceptionCatcher)
        .build();

    private final static String USERNAME = "Alex";
    private final static String EMAIL = "Alex@gmail";
    private final static String PASSWORD = "pass";

    @Test
    @DisplayName("When all incoming parameters are correct")
    void newAccount() throws Exception {

        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"username\": \"USERNAME\", \"email\": \"EMAIL\", \"password\": \"PASSWORD\"}")
        )
            .andExpect(status().isOk());

        verify(accountService, times(1))
            .create(eq(USERNAME), eq(EMAIL), eq(PASSWORD));
    }

    @Test
    @DisplayName("When missed all parameters")
    void newAccount2() throws Exception {
        mockMvc.perform(post("/signup"))
            .andExpect(status().isBadRequest());

        verify(accountService, times(0))
            .create(any(), any(), any());
    }

    @Test
    @DisplayName("When missed parameter PASSWORD")
    void newAccount3() throws Exception {
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"username\": \"USERNAME\", \"email\": \"EMAIL\"}")
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When missed parameter USERNAME")
    void newAccount4() throws Exception {
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"email\": \"EMAIL\", \"password\": \"PASSWORD\"}")
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When missed parameter EMAIL")
    void newAccount5() throws Exception {
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"username\": \"USERNAME\", \"password\": \"PASSWORD\"}")
        )
            .andExpect(status().isBadRequest());
    }
}