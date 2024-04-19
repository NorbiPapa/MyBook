package com.example.mybook;

public class MyBooks {
    private int id; // Könyv azonosítója
    private int statusid; // Könyv státuszának azonosítója
    private int bookid; // Könyv azonosítója
    private int userid; // Felhasználó azonosítója
    private MyBookStatus status; // Könyv státusza
    private Books book; // Könyv objektum

    // Getter és setter metódus az azonosítóhoz
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter és setter metódus a státuszazonosítóhoz
    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    // Getter és setter metódus a könyvazonosítóhoz
    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    // Getter és setter metódus a felhasználó azonosítójához
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    // Getter és setter metódus a könyv státuszához
    public MyBookStatus getStatus() {
        return status;
    }

    public void setStatus(MyBookStatus status) {
        this.status = status;
    }

    // Getter és setter metódus a könyv objektumához
    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    // Konstruktor a könyv adatainak beállításához
    public MyBooks(int id, int statusid, int bookid, int userid, MyBookStatus status, Books book) {
        this.id = id;
        this.statusid = statusid;
        this.bookid = bookid;
        this.userid = userid;
        this.status = status;
        this.book = book;
    }
}

