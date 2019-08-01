package com.jwtapp.repository;

import com.jwtapp.entity.Account;
import com.jwtapp.entity.Role;
import com.jwtapp.exception.ClientError;
import lombok.Getter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class PostgresAccountRepository implements AccountRepository {

    private static String ADD_ROLES = "INSERT INTO roles (name, account_id) VALUES (?, ?)";
    private static String ADD_ACCOUNT = "INSERT INTO accounts (id, username, email, password, is_enable) VALUES (?,?,?,?,?)";
    private static String CHECK_USER = "SELECT EXISTS(SELECT id FROM accounts WHERE username = ? OR email = ?)";
    private static String FIND_BY_NAME = "SELECT * FROM accounts WHERE username = ?";
    private static String FIND_ROLES_BY_ID = "SELECT name FROM roles  WHERE account_id = ?";

    private JdbcTemplate jdbcTemplate;

    private final AccountInfoMapper accountInfoMapper = new AccountInfoMapper();
    private final RoleMapper roleMapper = new RoleMapper();

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

    @Getter
    private static final class AccountInfo {
        private UUID id;
        private String username;
        private String email;
        private String password;
        private Boolean isEnable;

        public AccountInfo(UUID id, String username, String email, String password, Boolean isEnable) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.isEnable = isEnable;
        }
    }

    @Override
    public Account getAccountByName(String usernameVerify) {
        AccountInfo accountInfo = jdbcTemplate.queryForObject(FIND_BY_NAME, accountInfoMapper, usernameVerify);
        if (accountInfo == null)
            return null;

        List<Role> roles = jdbcTemplate.query(FIND_ROLES_BY_ID, roleMapper, accountInfo.id.toString());
        return new Account(
            accountInfo.getId(),
            accountInfo.getUsername(),
            accountInfo.getEmail(),
            accountInfo.getPassword(),
            accountInfo.getIsEnable(),
            roles
        );

    }
    static class AccountInfoMapper implements RowMapper<AccountInfo> {
        @Override
        public AccountInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = UUID.fromString(rs.getString("id"));
            String username = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            Boolean isEnable = rs.getBoolean("is_enable");
            return new AccountInfo(id, username, email, password, isEnable);
        }
    }
    static class RoleMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            final String nameRole = rs.getString("name");
            return Role.valueOf(nameRole);
        }
    }
}
