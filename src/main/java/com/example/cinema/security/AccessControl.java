package com.example.cinema.security;

import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;
import java.util.Arrays;

public final class AccessControl {
    private AccessControl() {
    }

    public static boolean hasRole(User user, Role... allowed) {
        if (user == null || user.getRole() == null || allowed == null) {
            return false;
        }
        return Arrays.asList(allowed).contains(user.getRole());
    }
}
