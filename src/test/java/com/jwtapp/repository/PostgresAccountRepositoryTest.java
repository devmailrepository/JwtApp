package com.jwtapp.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

class PostgresAccountRepositoryTest {
    private JdbcTemplate jdbcTemplate;
    String url = "jdbc:postgresql://localhost:6060/my_db";
    String usernamePos = "postgres";
    String passwordPos = "password";
    String CHANGE_LOG = "src/main/resources/changelog-master.xml";
    ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
    Database database = DatabaseFactory.getInstance()
            .openDatabase(url, usernamePos, passwordPos, null, resourceAccessor);
    Liquibase  liquibase = new Liquibase(CHANGE_LOG, resourceAccessor, database);

    PostgresAccountRepositoryTest() throws DatabaseException {
    }

    void liquibaseUpdate() throws LiquibaseException {
        liquibase.update("some");
    }

    @Test
    void exists() {
    }

    @Test
    void save() {
    }

    @Test
    void findAccountByUsername() {
    }
}