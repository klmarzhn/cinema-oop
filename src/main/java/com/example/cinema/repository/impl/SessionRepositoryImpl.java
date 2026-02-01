package com.example.cinema.repository.impl;

import com.example.cinema.entity.Session;
import com.example.cinema.repository.SessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionRepositoryImpl implements SessionRepository {
    @Override
    public List<Session> findAll(Connection connection) throws SQLException {
        String sql = "SELECT id, movie_id, session_date, price, total_seats FROM sessions ORDER BY id";
        List<Session> sessions = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("id"));
                session.setMovieId(rs.getInt("movie_id"));
                session.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime());
                session.setPrice(rs.getDouble("price"));
                session.setTotalSeats(rs.getInt("total_seats"));
                sessions.add(session);
            }
        }
        return sessions;
    }

    @Override
    public Session findById(Connection connection, int id) throws SQLException {
        String sql = "SELECT id, movie_id, session_date, price, total_seats FROM sessions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Session session = new Session();
                    session.setId(rs.getInt("id"));
                    session.setMovieId(rs.getInt("movie_id"));
                    session.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime());
                    session.setPrice(rs.getDouble("price"));
                    session.setTotalSeats(rs.getInt("total_seats"));
                    return session;
                }
            }
        }
        return null;
    }

    @Override
    public void add(Connection connection, Session session) throws SQLException {
        String sql = "INSERT INTO sessions (movie_id, session_date, price, total_seats) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, session.getMovieId());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(session.getSessionDate()));
            statement.setDouble(3, session.getPrice());
            statement.setInt(4, session.getTotalSeats());
            statement.executeUpdate();
        }
    }

    @Override
    public int count(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sessions";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
