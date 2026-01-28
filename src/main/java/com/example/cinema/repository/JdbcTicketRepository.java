package com.example.cinema.repository;

import com.example.cinema.entity.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcTicketRepository implements TicketRepository {
    @Override
    public boolean isSeatTaken(Connection connection, int sessionId, int seatNumber) throws SQLException {
        String sql = "SELECT 1 FROM tickets WHERE session_id = ? AND seat_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, sessionId);
            statement.setInt(2, seatNumber);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void add(Connection connection, Ticket ticket) throws SQLException {
        String sql = "INSERT INTO tickets (user_id, session_id, seat_number, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ticket.getUserId());
            statement.setInt(2, ticket.getSessionId());
            statement.setInt(3, ticket.getSeatNumber());
            statement.setTimestamp(4, Timestamp.valueOf(ticket.getPurchaseDate()));
            statement.executeUpdate();
        }
    }

    @Override
    public List<Ticket> findByUser(Connection connection, int userId) throws SQLException {
        String sql = "SELECT id, user_id, session_id, seat_number, purchase_date FROM tickets WHERE user_id = ? ORDER BY id";
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setId(rs.getInt("id"));
                    ticket.setUserId(rs.getInt("user_id"));
                    ticket.setSessionId(rs.getInt("session_id"));
                    ticket.setSeatNumber(rs.getInt("seat_number"));
                    ticket.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime());
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }
}
