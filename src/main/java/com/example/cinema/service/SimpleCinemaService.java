package com.example.cinema.service;

import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Role;
import com.example.cinema.entity.Session;
import com.example.cinema.entity.Ticket;
import com.example.cinema.entity.TicketDetails;
import com.example.cinema.pricing.PricingStrategy;
import com.example.cinema.entity.User;
import com.example.cinema.repository.MovieRepository;
import com.example.cinema.repository.SessionRepository;
import com.example.cinema.repository.TicketRepository;
import com.example.cinema.repository.UserRepository;
import com.example.cinema.validation.ValidationUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class SimpleCinemaService implements CinemaService {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final PricingStrategy pricingStrategy;

    public SimpleCinemaService(
            MovieRepository movieRepository,
            SessionRepository sessionRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository,
            PricingStrategy pricingStrategy
    ) {
        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public void seedMovies(Connection connection) throws SQLException {
        if (movieRepository.count(connection) > 0) {
            return;
        }
        List<Movie> movies = Arrays.asList(
                createMovie("Inception", "Sci-Fi", 148),
                createMovie("Interstellar", "Sci-Fi", 169),
                createMovie("The Matrix", "Action", 136),
                createMovie("The Godfather", "Crime", 175),
                createMovie("Pulp Fiction", "Crime", 154),
                createMovie("Spirited Away", "Animation", 125),
                createMovie("Parasite", "Thriller", 132),
                createMovie("The Dark Knight", "Action", 152),
                createMovie("La La Land", "Drama", 128),
                createMovie("Toy Story", "Animation", 81)
        );
        movies.forEach(movie -> addMovieSafe(connection, movie));
        System.out.println("Movies seeded.");
    }

    @Override
    public void seedSessions(Connection connection) throws SQLException {
        if (sessionRepository.count(connection) > 0) {
            return;
        }
        List<Movie> movies = movieRepository.findAll(connection);
        if (movies.isEmpty()) {
            System.out.println("No movies found. Seed movies first.");
            return;
        }
        LocalDateTime baseTime = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(2);
        IntStream.range(0, movies.size()).forEach(i -> addSessionSafe(connection, movies, baseTime, i));
        System.out.println("Sessions seeded.");
    }

    @Override
    public void showMovies(Connection connection) throws SQLException {
        List<Movie> movies = movieRepository.findAll(connection);
        System.out.println("Movies:");
        movies.forEach(movie -> {
            System.out.println(movie.getId() + ". " + movie.getTitle() + " ("
                    + movie.getGenre() + ", " + movie.getDurationMin() + " min)");
        });
    }

    @Override
    public void showSessions(Connection connection) throws SQLException {
        List<Session> sessions = sessionRepository.findAll(connection);
        System.out.println("Sessions:");
        sessions.forEach(session -> {
            System.out.println(session.getId() + ". movie_id=" + session.getMovieId()
                    + " | " + session.getSessionDate() + " | $" + session.getPrice());
        });
    }

    @Override
    public void register(Connection connection, Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (!ValidationUtils.isValidName(name)) {
            System.out.println("Invalid name. Use 2-50 letters/spaces.");
            return;
        }
        if (!ValidationUtils.isValidName(surname)) {
            System.out.println("Invalid surname. Use 2-50 letters/spaces.");
            return;
        }
        if (!ValidationUtils.isValidUsername(username)) {
            System.out.println("Invalid username. Use 3-20 letters/numbers/_");
            return;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            System.out.println("Invalid password. Minimum 4 characters.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.USER);

        try {
            userRepository.add(connection, user);
            System.out.println("Registered.");
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public User login(Connection connection, Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (ValidationUtils.isBlank(username) || ValidationUtils.isBlank(password)) {
            System.out.println("Username and password are required.");
            return null;
        }

        try {
            User user = userRepository.findByCredentials(connection, username, password);
            if (user != null) {
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
        System.out.println("Wrong username or password.");
        return null;
    }

    @Override
    public void buyTicket(Connection connection, Scanner scanner, int userId) throws SQLException {
        System.out.print("Session id: ");
        Integer sessionId = ValidationUtils.parsePositiveInt(scanner.nextLine());
        System.out.print("Seat number: ");
        Integer seatNumber = ValidationUtils.parsePositiveInt(scanner.nextLine());

        if (sessionId == null) {
            System.out.println("Invalid session id.");
            return;
        }
        if (seatNumber == null) {
            System.out.println("Invalid seat number.");
            return;
        }

        Session session = sessionRepository.findById(connection, sessionId);
        if (session == null) {
            System.out.println("Session not found.");
            return;
        }
        if (seatNumber > session.getTotalSeats()) {
            System.out.println("Invalid seat number. Max seats: " + session.getTotalSeats());
            return;
        }

        if (ticketRepository.isSeatTaken(connection, sessionId, seatNumber)) {
            System.out.println("This seat is already taken.");
            return;
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setSessionId(sessionId);
        ticket.setSeatNumber(seatNumber);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticketRepository.add(connection, ticket);
        System.out.println("Ticket purchased.");
    }

    @Override
    public void showMyTickets(Connection connection, int userId) throws SQLException {
        List<TicketDetails> tickets = ticketRepository.findDetailedByUser(connection, userId);
        System.out.println("My tickets:");
        printTickets(tickets);
    }

    private Movie createMovie(String title, String genre, int durationMin) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setDurationMin(durationMin);
        return movie;
    }

    private Session createSession(int movieId, LocalDateTime sessionDate, double price, int totalSeats) {
        Session session = new Session();
        session.setMovieId(movieId);
        session.setSessionDate(sessionDate);
        session.setPrice(price);
        session.setTotalSeats(totalSeats);
        return session;
    }

    @Override
    public void showAllTickets(Connection connection) throws SQLException {
        List<TicketDetails> tickets = ticketRepository.findAllDetailed(connection);
        System.out.println("All tickets:");
        printTickets(tickets);
    }

    @Override
    public void showUsers(Connection connection) throws SQLException {
        List<User> users = userRepository.findAll(connection);
        System.out.println("Users:");
        users.forEach(user -> System.out.println(user.getId() + ". "
                + user.getUsername() + " | " + user.getName() + " " + user.getSurname()
                + " | role=" + user.getRole()));
    }

    @Override
    public void changeUserRole(Connection connection, Scanner scanner) {
        try {
            showUsers(connection);
        } catch (SQLException e) {
            System.out.println("Failed to load users: " + e.getMessage());
            return;
        }
        System.out.print("User id: ");
        String idInput = scanner.nextLine().trim();
        System.out.print("New role (USER/MANAGER/ADMIN): ");
        String roleInput = scanner.nextLine().trim();
        Role role = Role.parse(roleInput);
        if (role == null) {
            System.out.println("Unknown role.");
            return;
        }
        try {
            int userId = Integer.parseInt(idInput);
            userRepository.updateRole(connection, userId, role);
            System.out.println("Role updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user id.");
        } catch (SQLException e) {
            System.out.println("Failed to update role: " + e.getMessage());
        }
    }

    private void printTickets(List<TicketDetails> tickets) {
        tickets.forEach(ticket -> System.out.println(ticket.getTicketId() + ". " + ticket.getMovieTitle()
                + " (" + ticket.getMovieGenre() + ")"
                + " | session=" + ticket.getSessionDate()
                + " | seat=" + ticket.getSeatNumber()
                + " | price=$" + ticket.getPrice()
                + " | buyer=" + ticket.getUserName() + " " + ticket.getUserSurname()
                + " (" + ticket.getUsername() + ")"
                + " | purchased=" + ticket.getPurchaseDate()));
    }

    private void addMovieSafe(Connection connection, Movie movie) {
        try {
            movieRepository.add(connection, movie);
        } catch (SQLException e) {
            System.out.println("Failed to add movie: " + e.getMessage());
        }
    }

    private void addSessionSafe(Connection connection, List<Movie> movies, LocalDateTime baseTime, int index) {
        Movie movie = movies.get(index);
        LocalDateTime sessionTime = baseTime
                .plusDays(index / 3)
                .plusHours((index % 3) * 3L);
        double price = pricingStrategy.priceForIndex(index);
        Session session = createSession(movie.getId(), sessionTime, price, 60);
        try {
            sessionRepository.add(connection, session);
        } catch (SQLException e) {
            System.out.println("Failed to add session: " + e.getMessage());
        }
    }
}
