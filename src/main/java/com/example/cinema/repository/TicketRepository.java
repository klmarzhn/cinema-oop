package com.example.cinema.repository;

import com.example.cinema.entity.Ticket;
import com.example.cinema.entity.TicketDetails;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TicketRepository {
    boolean isSeatTaken(Connection connection, int sessionId, int seatNumber) throws SQLException;

    void add(Connection connection, Ticket ticket) throws SQLException;

    List<Ticket> findByUser(Connection connection, int userId) throws SQLException;

    List<TicketDetails> findDetailedByUser(Connection connection, int userId) throws SQLException;

    List<TicketDetails> findAllDetailed(Connection connection) throws SQLException;
}
