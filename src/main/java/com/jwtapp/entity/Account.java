package com.jwtapp.entity;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Account {
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;
    private final boolean isEnable;
    private final List<Role> roles;

    public Account(
        UUID id,
        String username,
        String email,
        String password,
        boolean isEnable,
        List<Role> roles
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnable = isEnable;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", isEnable=" + isEnable +
            ", roles=" + roles +
            '}';
    }

    public Account(
        String username,
        String email,
        String password,
        List<Role> roles
    ) {
        id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnable = true;
        this.roles = roles;
    }

    public Account(
        String username,
        String email,
        String password
    ) {
        id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnable = true;
        this.roles = new ArrayList<Role>() {{
            add(Role.USER);
            add(Role.BOSS);
        }};
    }
}
