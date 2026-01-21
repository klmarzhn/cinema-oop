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
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                showMovies(connection);
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
}
