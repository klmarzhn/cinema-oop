package com.example.cinema.repository;

import com.example.cinema.entity.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSessionRepository implements SessionRepository {
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
}
