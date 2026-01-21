package models;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
}