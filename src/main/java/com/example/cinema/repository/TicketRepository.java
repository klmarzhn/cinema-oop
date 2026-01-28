package com.example.cinema.repository;

import com.example.cinema.entity.Ticket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TicketRepository {
    boolean isSeatTaken(Connection connection, int sessionId, int seatNumber) throws SQLException;

    void add(Connection connection, Ticket ticket) throws SQLException;

    List<Ticket> findByUser(Connection connection, int userId) throws SQLException;
}
