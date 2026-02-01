package com.example.cinema.factory;

import com.example.cinema.repository.MovieRepository;
import com.example.cinema.repository.SessionRepository;
import com.example.cinema.repository.TicketRepository;
import com.example.cinema.repository.UserRepository;
import com.example.cinema.repository.impl.MovieRepositoryImpl;
import com.example.cinema.repository.impl.SessionRepositoryImpl;
import com.example.cinema.repository.impl.TicketRepositoryImpl;
import com.example.cinema.repository.impl.UserRepositoryImpl;

public class RepositoryFactory {
    public MovieRepository createMovieRepository() {
        return new MovieRepositoryImpl();
    }

    public SessionRepository createSessionRepository() {
        return new SessionRepositoryImpl();
    }

    public TicketRepository createTicketRepository() {
        return new TicketRepositoryImpl();
    }

    public UserRepository createUserRepository() {
        return new UserRepositoryImpl();
    }
}
