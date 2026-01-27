package com.example.cinema.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Session {
    private int id;
    private int movieId;
    private LocalDateTime sessionDate;
    private double price;
    private int totalSeats;
}
