package com.example.cinema.app;

import com.example.cinema.config.DbConnectionManager;
import com.example.cinema.entity.User;
import com.example.cinema.entity.Role;
import com.example.cinema.factory.RepositoryFactory;
import com.example.cinema.pricing.PricingStrategy;
import com.example.cinema.pricing.TieredPricingStrategy;
import com.example.cinema.security.AccessControl;
import com.example.cinema.service.CinemaService;
import com.example.cinema.service.SimpleCinemaService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MyApplication {
    public static void main(String[] args) {
        try (Connection connection = DbConnectionManager.getInstance().getConnection()) {
            runApp(connection);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void runApp(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        PricingStrategy pricingStrategy = new TieredPricingStrategy();
        CinemaService service = new SimpleCinemaService(
                repositoryFactory.createMovieRepository(),
                repositoryFactory.createSessionRepository(),
                repositoryFactory.createTicketRepository(),
                repositoryFactory.createUserRepository(),
                pricingStrategy
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
                System.out.println("Logged in as: " + currentUser.getUsername()
                        + " (" + currentUser.getRole() + ")");
                System.out.println("1. Show movies");
                System.out.println("2. Show sessions");
                System.out.println("3. Buy ticket");
                System.out.println("4. My tickets");
                System.out.println("5. Logout");
                if (AccessControl.hasRole(currentUser, Role.MANAGER, Role.ADMIN)) {
                    System.out.println("6. All tickets (manager/admin)");
                }
                if (AccessControl.hasRole(currentUser, Role.ADMIN)) {
                    System.out.println("7. Manage roles (admin)");
                }
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
                } else if (choice.equals("6")) {
                    if (AccessControl.hasRole(currentUser, Role.MANAGER, Role.ADMIN)) {
                        service.showAllTickets(connection);
                    } else {
                        System.out.println("Access denied.");
                    }
                } else if (choice.equals("7")) {
                    if (AccessControl.hasRole(currentUser, Role.ADMIN)) {
                        service.changeUserRole(connection, scanner);
                    } else {
                        System.out.println("Access denied.");
                    }
                } else if (choice.equals("0")) {
                    break;
                }
            }
        }
    }
}
