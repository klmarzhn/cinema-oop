package com.example.cinema.repository;

import com.example.cinema.entity.Movie;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface MovieRepository {
    List<Movie> findAll(Connection connection) throws SQLException;

    int count(Connection connection) throws SQLException;

    void add(Connection connection, Movie movie) throws SQLException;
}
