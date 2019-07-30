package com.jwtapp.repository;

import com.jwtapp.entity.Account;
import com.jwtapp.entity.Role;
import com.jwtapp.exception.ClientError;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository


public class PostgresAccountRepository implements AccountRepository {


    private static String ADD_ROLES = "INSERT INTO roles (name, account_id) VALUES (?, ?)";
    private static String ADD_ACCOUNT = "INSERT INTO accounts (id, username, email, password, is_enable) VALUES (?,?,?,?,?)";
    private static String CHECK_USER = "SELECT EXISTS(SELECT id FROM accounts WHERE username = ? OR email = ?)";
    private static String FIND_BY_NAME = "SELECT * FROM accounts WHERE accounts.username = ?";
    private static String FIND_ROLES_BY_ID = "SELECT name FROM roles  WHERE roles.user_id = ?";



    private JdbcTemplate jdbcTemplate;


    public PostgresAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean exists(String username, String email) {
        final String[] args = new String[]{username, email};
        return jdbcTemplate.queryForObject(CHECK_USER, args, Boolean.class);
    }
    public void save(final Account account) {
        final String accountId = account.getId().toString();
        try {
            jdbcTemplate.update(ADD_ACCOUNT, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, accountId);
                    ps.setString(2, account.getUsername());
                    ps.setString(3, account.getEmail());
                    ps.setString(4, account.getPassword());
                    ps.setBoolean(5, account.isEnable());
                }
            });

            for (Role role : account.getRoles()) {
                jdbcTemplate.update(ADD_ROLES, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {

                        ps.setString(1, role.toString());
                        ps.setString(2, accountId);
                    }
                });
            }
        } catch (DuplicateKeyException e) {
            throw new ClientError("DB contains such user");
        }
    }

    private final AccountRowMapper mapper = new AccountRowMapper();
    private static class AccountRowMapper implements RowMapper<Account> {
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            final String id = rs.getString("id");
            final String username = rs.getString("username");
            final String email = rs.getString("email");
            final String password = rs.getString("password");
            final Boolean isEnable = rs.getBoolean("status");
            return ...


        }
        private static class RolesRowMapper implements RowMapper<Role>{
            @Override
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                final Array roles = rs.getArray("name");
                List<Role> role = Arrays.asList(roles);
                return Role;
            }
        }

    }

    @Override
    public Account getAccountByName(String usernameVerify) {
        Account existingAccount = jdbcTemplate.queryForObject(FIND_BY_NAME, mapper, usernameVerify);
        return existingAccount;
    }
}