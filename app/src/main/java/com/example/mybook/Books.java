package com.example.mybook;

public class Books {
    private int id; // Könyv azonosítója
    private String bookname; // Könyv címe
    private String writer; // Könyv szerzője
    private int release; // Könyv kiadási éve

    // Getter és setter metódusok az osztálymezőkhöz
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getRelease() {
        return release;
    }

    public void setRelease(int release) {
        this.release = release;
    }

    // Konstruktor az osztály mezőinek inicializálásához
    public Books(int id, String bookname, String writer, int release) {
        this.id = id;
        this.bookname = bookname;
        this.writer = writer;
        this.release = release;
    }
}






