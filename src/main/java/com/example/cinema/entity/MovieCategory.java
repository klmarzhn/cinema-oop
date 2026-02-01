package com.example.cinema.entity;

public enum MovieCategory {
    ACTION,
    CRIME,
    DRAMA,
    THRILLER,
    SCI_FI,
    ANIMATION,
    GENERAL;

    public static MovieCategory parse(String value) {
        if (value == null) {
            return null;
        }
        try {
            return MovieCategory.valueOf(value.trim().toUpperCase().replace('-', '_'));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
