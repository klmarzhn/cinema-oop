package com.example.cinema.repository.impl;

import com.example.cinema.entity.Movie;
import com.example.cinema.entity.MovieCategory;
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
        String sql = "SELECT id, title, duration_min, category FROM movies ORDER BY id";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDurationMin(rs.getInt("duration_min"));
                MovieCategory category = MovieCategory.parse(rs.getString("category"));
                movie.setCategory(category == null ? MovieCategory.GENERAL : category);
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
        String sql = "INSERT INTO movies (title, duration_min, category) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getDurationMin());
            MovieCategory category = movie.getCategory();
            statement.setString(3, category == null ? MovieCategory.GENERAL.name() : category.name());
            statement.executeUpdate();
        }
    }
}
