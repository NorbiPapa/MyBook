package com.example.mybook;

public class Response {
    private int responseCode; // Válaszkód
    private String responseMessage; // Válaszüzenet

    // Konstruktor a válaszkód és válaszüzenet beállításához
    public Response(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    // Getter és setter metódus a válaszkódhoz
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    // Getter és setter metódus a válaszüzenethez
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
