package org.example.sessionbackend.entity.auth;

import lombok.Data;

@Data
public class Account {
    int id;
    String username;
    String password;
    String email;
}
