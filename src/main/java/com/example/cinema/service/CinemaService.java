package com.example.cinema.service;

import com.example.cinema.entity.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public interface CinemaService {
    void seedMovies(Connection connection) throws SQLException;

    void seedSessions(Connection connection) throws SQLException;

    void showMovies(Connection connection) throws SQLException;

    void showSessions(Connection connection) throws SQLException;

    void register(Connection connection, Scanner scanner);

    User login(Connection connection, Scanner scanner);

    void buyTicket(Connection connection, Scanner scanner, int userId) throws SQLException;

    void showMyTickets(Connection connection, int userId) throws SQLException;
}
