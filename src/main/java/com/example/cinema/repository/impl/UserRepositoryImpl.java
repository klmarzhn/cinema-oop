package com.example.cinema.repository.impl;

import com.example.cinema.entity.Role;
import com.example.cinema.entity.User;
import com.example.cinema.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void add(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO users (name, surname, username, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole() == null ? Role.USER.name() : user.getRole().name());
            statement.executeUpdate();
        }
    }

    @Override
    public User findByCredentials(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT id, name, surname, username, password, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    Role role = Role.parse(rs.getString("role"));
                    user.setRole(role == null ? Role.USER : role);
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name, surname, username, password, role FROM users ORDER BY id";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                Role role = Role.parse(rs.getString("role"));
                user.setRole(role == null ? Role.USER : role);
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void updateRole(Connection connection, int userId, Role role) throws SQLException {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role == null ? Role.USER.name() : role.name());
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }
}
