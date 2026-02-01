package com.example.cinema.entity;

import lombok.Data;

@Data
public class Movie {
    private int id;
    private String title;
    private int durationMin;
    private MovieCategory category;

    public MovieCategory getCategory() {
        return category;
    }

    public void setCategory(MovieCategory category) {
        this.category = category;
    }
}
