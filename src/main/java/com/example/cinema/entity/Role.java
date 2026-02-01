package com.example.cinema.entity;

public enum Role {
    USER,
    MANAGER,
    ADMIN;

    public static Role parse(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
