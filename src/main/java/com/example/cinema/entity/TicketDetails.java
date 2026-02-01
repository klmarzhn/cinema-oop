package com.example.cinema.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketDetails {
    private int ticketId;
    private int seatNumber;
    private LocalDateTime purchaseDate;
    private LocalDateTime sessionDate;
    private double price;
    private String movieTitle;
    private String movieGenre;
    private String userName;
    private String userSurname;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
