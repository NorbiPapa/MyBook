package com.example.mybook;

public class Users {
    String email; // Felhasználó e-mail címe
    String username; // Felhasználó felhasználóneve
    String password; // Felhasználó jelszava

    // Konstruktor a felhasználó létrehozásához
    public Users(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getter és setter metódusok az e-mail címhez, felhasználónévhez és jelszóhoz
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
