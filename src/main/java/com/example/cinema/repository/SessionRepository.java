package com.example.cinema.repository;

import com.example.cinema.entity.Session;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SessionRepository {
    List<Session> findAll(Connection connection) throws SQLException;
}
