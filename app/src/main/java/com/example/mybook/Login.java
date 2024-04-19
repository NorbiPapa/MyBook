package com.example.mybook;

public class Login {
    String email; // Felhasználó e-mail címe
    String password; // Felhasználó jelszava

    // Konstruktor az e-mail cím és a jelszó beállításához
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter és setter metódusok az e-mail címhez
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter és setter metódusok a jelszóhoz
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
