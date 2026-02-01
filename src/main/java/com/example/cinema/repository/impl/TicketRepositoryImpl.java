package com.example.cinema.repository.impl;

import com.example.cinema.entity.Ticket;
import com.example.cinema.entity.TicketDetails;
import com.example.cinema.repository.TicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {
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

    @Override
    public List<TicketDetails> findDetailedByUser(Connection connection, int userId) throws SQLException {
        String sql = "SELECT t.id AS ticket_id, t.seat_number, t.purchase_date, " +
                "s.session_date, s.price, " +
                "m.title AS movie_title, m.genre AS movie_genre, " +
                "u.name AS user_name, u.surname AS user_surname, u.username " +
                "FROM tickets t " +
                "JOIN sessions s ON t.session_id = s.id " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN users u ON t.user_id = u.id " +
                "WHERE t.user_id = ? " +
                "ORDER BY t.id";
        List<TicketDetails> tickets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    TicketDetails ticket = TicketDetails.builder()
                            .ticketId(rs.getInt("ticket_id"))
                            .seatNumber(rs.getInt("seat_number"))
                            .purchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime())
                            .sessionDate(rs.getTimestamp("session_date").toLocalDateTime())
                            .price(rs.getDouble("price"))
                            .movieTitle(rs.getString("movie_title"))
                            .movieGenre(rs.getString("movie_genre"))
                            .userName(rs.getString("user_name"))
                            .userSurname(rs.getString("user_surname"))
                            .username(rs.getString("username"))
                            .build();
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }

    @Override
    public List<TicketDetails> findAllDetailed(Connection connection) throws SQLException {
        String sql = "SELECT t.id AS ticket_id, t.seat_number, t.purchase_date, " +
                "s.session_date, s.price, " +
                "m.title AS movie_title, m.genre AS movie_genre, " +
                "u.name AS user_name, u.surname AS user_surname, u.username " +
                "FROM tickets t " +
                "JOIN sessions s ON t.session_id = s.id " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN users u ON t.user_id = u.id " +
                "ORDER BY t.id";
        List<TicketDetails> tickets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                TicketDetails ticket = TicketDetails.builder()
                        .ticketId(rs.getInt("ticket_id"))
                        .seatNumber(rs.getInt("seat_number"))
                        .purchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime())
                        .sessionDate(rs.getTimestamp("session_date").toLocalDateTime())
                        .price(rs.getDouble("price"))
                        .movieTitle(rs.getString("movie_title"))
                        .movieGenre(rs.getString("movie_genre"))
                        .userName(rs.getString("user_name"))
                        .userSurname(rs.getString("user_surname"))
                        .username(rs.getString("username"))
                        .build();
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
