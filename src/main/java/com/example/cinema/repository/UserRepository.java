package com.example.cinema.repository;

import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    void add(Connection connection, User user) throws SQLException;

    User findByCredentials(Connection connection, String username, String password) throws SQLException;

    List<User> findAll(Connection connection) throws SQLException;

    void updateRole(Connection connection, int userId, Role role) throws SQLException;
}
