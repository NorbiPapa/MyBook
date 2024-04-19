package com.example.mybook;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestHandler {

    private RequestHandler() {
    }

    //backend csatlakozásért felelős methódus
    private static HttpURLConnection setupConnection(String url, String token) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        Log.d("Token", "setupConnection: " + token);
        if (token != null) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        return conn;
    }

    //response elérése
    private static Response getResponse(HttpURLConnection connection)
            throws IOException {
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (responseCode < 400) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String sor = bufferedReader.readLine();
        while (sor != null) {
            builder.append(sor);
            sor = bufferedReader.readLine();
        }
        bufferedReader.close();
        inputStream.close();
        return new Response(responseCode, builder.toString());
    }

    //request body hozzáadása
    private static void addRequestBody(HttpURLConnection connection, String data) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(data);
        writer.flush();
        writer.close();
        outputStream.close();
    }

    //GET METHOD
    public static Response get(String url, String token) throws IOException {
        HttpURLConnection connection = setupConnection(url, token);
        return getResponse(connection);
    }

    //POST METHOD
    public static Response post(String url, String data, String token) throws IOException {
        HttpURLConnection connection = setupConnection(url, token);
        connection.setRequestMethod("POST");
        addRequestBody(connection, data);
        return getResponse(connection);
    }

    //PUT METHOD
    public static Response put(String url, String data, String token) throws IOException {
        HttpURLConnection connection = setupConnection(url,token);
        connection.setRequestMethod("PUT");
        addRequestBody(connection, data);
        return getResponse(connection);
    }

    //DELETE METHOD
    public static Response post(String url, String token) throws IOException {
        HttpURLConnection connection = setupConnection(url, token);
        connection.setRequestMethod("POST");
        return getResponse(connection);
    }
}
