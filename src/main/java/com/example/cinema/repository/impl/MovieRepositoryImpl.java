package com.example.cinema.repository.impl;

import com.example.cinema.entity.Movie;
import com.example.cinema.repository.MovieRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {
    @Override
    public List<Movie> findAll(Connection connection) throws SQLException {
        String sql = "SELECT id, title, genre, duration_min FROM movies ORDER BY id";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMin(rs.getInt("duration_min"));
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public int count(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM movies";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void add(Connection connection, Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, duration_min) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getGenre());
            statement.setInt(3, movie.getDurationMin());
            statement.executeUpdate();
        }
    }
}
