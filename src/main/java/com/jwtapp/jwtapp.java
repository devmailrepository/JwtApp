package com.jwtapp;

import com.jwtapp.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = ApplicationConfiguration.class)
public class jwtapp {

    public static void main(String[] args) {
        SpringApplication.run(jwtapp.class, args);
    }
}
