package com.example.cinema.service;

import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Session;
import com.example.cinema.entity.Ticket;
import com.example.cinema.entity.User;
import com.example.cinema.repository.MovieRepository;
import com.example.cinema.repository.SessionRepository;
import com.example.cinema.repository.TicketRepository;
import com.example.cinema.repository.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SimpleCinemaService implements CinemaService {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public SimpleCinemaService(
            MovieRepository movieRepository,
            SessionRepository sessionRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository
    ) {
        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
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
        for (Movie movie : movies) {
            movieRepository.add(connection, movie);
        }
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
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            LocalDateTime sessionTime = baseTime
                    .plusDays(i / 3)
                    .plusHours((i % 3) * 3L);
            double price = 10.0 + (i % 3) * 2.5;
            Session session = createSession(movie.getId(), sessionTime, price, 60);
            sessionRepository.add(connection, session);
        }
        System.out.println("Sessions seeded.");
    }

    @Override
    public void showMovies(Connection connection) throws SQLException {
        List<Movie> movies = movieRepository.findAll(connection);
        System.out.println("Movies:");
        for (Movie movie : movies) {
            System.out.println(movie.getId() + ". " + movie.getTitle() + " ("
                    + movie.getGenre() + ", " + movie.getDurationMin() + " min)");
        }
    }

    @Override
    public void showSessions(Connection connection) throws SQLException {
        List<Session> sessions = sessionRepository.findAll(connection);
        System.out.println("Sessions:");
        for (Session session : sessions) {
            System.out.println(session.getId() + ". movie_id=" + session.getMovieId()
                    + " | " + session.getSessionDate() + " | $" + session.getPrice());
        }
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

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(password);

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
        int sessionId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Seat number: ");
        int seatNumber = Integer.parseInt(scanner.nextLine().trim());

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
        List<Ticket> tickets = ticketRepository.findByUser(connection, userId);
        System.out.println("My tickets:");
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getId() + ". session_id=" + ticket.getSessionId()
                    + " seat=" + ticket.getSeatNumber() + " date=" + ticket.getPurchaseDate());
        }
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
}
