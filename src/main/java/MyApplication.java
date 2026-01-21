import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import models.User;

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
        CinemaService service = new CinemaService();

        while (true) {
            System.out.println();
            System.out.println("1. Show movies");
            System.out.println("2. Register");
            System.out.println("3. Login");
            System.out.println("4. Show sessions");
            System.out.println("5. Buy ticket");
            System.out.println("6. My tickets");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                service.showMovies(connection);
            } else if (choice.equals("2")) {
                service.register(connection, scanner);
            } else if (choice.equals("3")) {
                User user = service.login(connection, scanner);
                if (user != null) {
                    System.out.println("Login successful. User id: " + user.getId());
                }
            } else if (choice.equals("4")) {
                service.showSessions(connection);
            } else if (choice.equals("5")) {
                User user = service.login(connection, scanner);
                if (user != null) {
                    service.buyTicket(connection, scanner, user.getId());
                }
            } else if (choice.equals("6")) {
                User user = service.login(connection, scanner);
                if (user != null) {
                    service.showMyTickets(connection, user.getId());
                }
            } else if (choice.equals("0")) {
                break;
            }
        }
    }
}
