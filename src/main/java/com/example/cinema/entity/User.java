package com.example.cinema.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
