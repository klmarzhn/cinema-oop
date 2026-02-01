package com.example.cinema.app;

import com.example.cinema.entity.User;
import com.example.cinema.repository.impl.MovieRepositoryImpl;
import com.example.cinema.repository.impl.SessionRepositoryImpl;
import com.example.cinema.repository.impl.TicketRepositoryImpl;
import com.example.cinema.repository.impl.UserRepositoryImpl;
import com.example.cinema.service.CinemaService;
import com.example.cinema.service.SimpleCinemaService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MyApplication {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/cinema-db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            runApp(connection);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void runApp(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        CinemaService service = new SimpleCinemaService(
                new MovieRepositoryImpl(),
                new SessionRepositoryImpl(),
                new TicketRepositoryImpl(),
                new UserRepositoryImpl()
        );
        service.seedMovies(connection);
        service.seedSessions(connection);

        User currentUser = null;
        while (true) {
            if (currentUser == null) {
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    service.register(connection, scanner);
                } else if (choice.equals("2")) {
                    User user = service.login(connection, scanner);
                    if (user != null) {
                        currentUser = user;
                        System.out.println("Login successful. User id: " + currentUser.getId());
                    }
                } else if (choice.equals("0")) {
                    break;
                }
            } else {
                System.out.println();
                System.out.println("Logged in as: " + currentUser.getUsername());
                System.out.println("1. Show movies");
                System.out.println("2. Show sessions");
                System.out.println("3. Buy ticket");
                System.out.println("4. My tickets");
                System.out.println("5. Logout");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    service.showMovies(connection);
                } else if (choice.equals("2")) {
                    service.showSessions(connection);
                } else if (choice.equals("3")) {
                    service.buyTicket(connection, scanner, currentUser.getId());
                } else if (choice.equals("4")) {
                    service.showMyTickets(connection, currentUser.getId());
                } else if (choice.equals("5")) {
                    currentUser = null;
                    System.out.println("Logged out.");
                } else if (choice.equals("0")) {
                    break;
                }
            }
        }
    }
}
