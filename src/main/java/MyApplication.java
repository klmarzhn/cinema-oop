import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        while (true) {
            System.out.println();
            System.out.println("1. Show movies");
            System.out.println("2. Register");
            System.out.println("3. Login");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                showMovies(connection);
            } else if (choice.equals("2")) {
                register(connection, scanner);
            } else if (choice.equals("3")) {
                login(connection, scanner);
            } else if (choice.equals("0")) {
                break;
            }
        }
    }

    private static void showMovies(Connection connection) throws SQLException {
        String sql = "SELECT id, title, genre, duration_min FROM movies ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            System.out.println("Movies:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration_min");
                System.out.println(id + ". " + title + " (" + genre + ", " + duration + " min)");
            }
        }
    }

    private static void register(Connection connection, Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String sql = "INSERT INTO users (name, surname, username, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.executeUpdate();
            System.out.println("Registered.");
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void login(Connection connection, Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login successful. User id: " + rs.getInt("id"));
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return;
        }
        System.out.println("Wrong username or password.");
    }
}
