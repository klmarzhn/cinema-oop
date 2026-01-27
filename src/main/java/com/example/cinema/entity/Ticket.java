package com.example.cinema.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Ticket {
    private int id;
    private int userId;
    private int sessionId;
    private int seatNumber;
    private LocalDateTime purchaseDate;
}
