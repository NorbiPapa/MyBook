package com.example.mybook;

public class MyBookStatus {
    private int id; // Státusz azonosítója
    private String statusname; // Státusz neve

    // Konstruktor a státusz azonosítójának és nevének beállításához
    public MyBookStatus(int id, String statusname) {
        this.id = id;
        this.statusname = statusname;
    }

    // Getter és setter metódus az azonosítóhoz
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter és setter metódus a státusz névéhez
    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }
}
