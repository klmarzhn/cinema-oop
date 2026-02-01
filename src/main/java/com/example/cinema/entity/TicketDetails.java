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
    private String movieCategory;
    private String userName;
    private String userSurname;
    private String username;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TicketDetails ticket = new TicketDetails();

        public Builder ticketId(int ticketId) {
            ticket.ticketId = ticketId;
            return this;
        }

        public Builder seatNumber(int seatNumber) {
            ticket.seatNumber = seatNumber;
            return this;
        }

        public Builder purchaseDate(LocalDateTime purchaseDate) {
            ticket.purchaseDate = purchaseDate;
            return this;
        }

        public Builder sessionDate(LocalDateTime sessionDate) {
            ticket.sessionDate = sessionDate;
            return this;
        }

        public Builder price(double price) {
            ticket.price = price;
            return this;
        }

        public Builder movieTitle(String movieTitle) {
            ticket.movieTitle = movieTitle;
            return this;
        }

        public Builder movieCategory(String movieCategory) {
            ticket.movieCategory = movieCategory;
            return this;
        }

        public Builder userName(String userName) {
            ticket.userName = userName;
            return this;
        }

        public Builder userSurname(String userSurname) {
            ticket.userSurname = userSurname;
            return this;
        }

        public Builder username(String username) {
            ticket.username = username;
            return this;
        }

        public TicketDetails build() {
            return ticket;
        }
    }

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

    public String getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(String movieCategory) {
        this.movieCategory = movieCategory;
    }
}
