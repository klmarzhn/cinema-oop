package com.example.cinema.repository;

import com.example.cinema.entity.User;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserRepository {
    void add(Connection connection, User user) throws SQLException;

    User findByCredentials(Connection connection, String username, String password) throws SQLException;
}
