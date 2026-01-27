package com.example.cinema.entity;

import lombok.Data;

@Data
public class Movie {
    private int id;
    private String title;
    private String genre;
    private int durationMin;
}
