import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import models.*;

public class CinemaService {
    public void showMovies(Connection connection) throws SQLException {
        String sql = "SELECT id, title, genre, duration_min FROM movies ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            System.out.println("Movies:");
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMin(rs.getInt("duration_min"));
                System.out.println(movie.getId() + ". " + movie.getTitle() + " ("
                        + movie.getGenre() + ", " + movie.getDurationMin() + " min)");
            }
        }
    }

    public void register(Connection connection, Scanner scanner) {
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

    public User login(Connection connection, Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String sql = "SELECT id, name, surname, username, password FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
        System.out.println("Wrong username or password.");
        return null;
    }
    public void showSessions(Connection connection) throws SQLException {
        String sql = "SELECT id, movie_id, session_date, price, total_seats FROM sessions ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            System.out.println("Sessions:");
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("id"));
                session.setMovieId(rs.getInt("movie_id"));
                session.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime());
                session.setPrice(rs.getDouble("price"));
                session.setTotalSeats(rs.getInt("total_seats"));
                System.out.println(session.getId() + ". movie_id=" + session.getMovieId()
                        + " | " + session.getSessionDate() + " | $" + session.getPrice());
            }
        }
    }

    public void buyTicket(Connection connection, Scanner scanner, int userId) throws SQLException {
        System.out.print("Session id: ");
        int sessionId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Seat number: ");
        int seatNumber = Integer.parseInt(scanner.nextLine().trim());

        if (seatTaken(connection, sessionId, seatNumber)) {
            System.out.println("This seat is already taken.");
            return;
        }

        String sql = "INSERT INTO tickets (user_id, session_id, seat_number, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, sessionId);
            statement.setInt(3, seatNumber);
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            System.out.println("Ticket purchased.");
        }
    }

    public void showMyTickets(Connection connection, int userId) throws SQLException {
        System.out.println("My tickets:");
        String sql = "SELECT id, user_id, session_id, seat_number, purchase_date FROM tickets WHERE user_id = ? ORDER BY id";
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
                    System.out.println(ticket.getId() + ". session_id=" + ticket.getSessionId()
                            + " seat=" + ticket.getSeatNumber() + " date=" + ticket.getPurchaseDate());
                }
            }
        }
    }

    private boolean seatTaken(Connection connection, int sessionId, int seatNumber) throws SQLException {
        String sql = "SELECT 1 FROM tickets WHERE session_id = ? AND seat_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, sessionId);
            statement.setInt(2, seatNumber);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }
}

