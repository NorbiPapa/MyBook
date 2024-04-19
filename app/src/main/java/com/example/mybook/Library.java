package com.example.mybook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library extends AppCompatActivity {

    // URL a könyvek adatainak lekérdezéséhez
    public String url = "http://10.0.2.2:3000/books/SearchUserBook";

    // ListView a könyvek megjelenítéséhez
    public ListView listView;

    // Lista a könyvek adatainak tárolásához
    public List<MyBooks> books = new ArrayList<>();

    // Gomb a visszalépéshez
    public Button visszabtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Címsor elrejtése és teljes képernyő módban való megjelenítés
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_library);

        // Nézetek inicializálása és adatok lekérése a szerverről
        init();
        RequestTask task = new Library.RequestTask(url, "GET");
        task.execute();

        // Kattintáskezelő beállítása a vissza gombhoz
        visszabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Visszalépés a főmenübe
                Intent intent = new Intent(Library.this, MainMenz.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Nézetek inicializálása
    public void init() {
        listView = findViewById(R.id.librarylistview);
        listView.setAdapter(new BooksAdapter());
        visszabtn = findViewById(R.id.visszabutton);
    }

    // Egyedi adapter a ListView-hoz
    private class BooksAdapter extends ArrayAdapter<MyBooks> {

        public BooksAdapter() {
            super(Library.this, R.layout.library_list_items, books);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Inflater az elrendezés betöltéséhez
            LayoutInflater inflater = getLayoutInflater();

            // Elrendezés betöltése a library_list_items.xml-ből
            View view = inflater.inflate(R.layout.library_list_items, null, false);

            // Nézetek inicializálása a library_list_items.xml-ből
            TextView textViewBookTitle = view.findViewById(R.id.LibraryBookTitle);
            TextView textViewBookAuthor = view.findViewById(R.id.LibraryBookAuthor);
            TextView textViewBookYear = view.findViewById(R.id.LibraryBookYear);
            TextView textViewStatus = view.findViewById(R.id.LibraryBookStatus);

            // Aktuális könyv lekérése
            MyBooks actualBook = books.get(position);

            // Könyv részleteinek beállítása a TextView-ekre
            textViewBookTitle.setText(actualBook.getBook().getBookname());
            textViewBookAuthor.setText(actualBook.getBook().getWriter());
            textViewBookYear.setText("(" + String.valueOf(actualBook.getBook().getRelease()) + ")");
            textViewStatus.setText(actualBook.getStatus().getStatusname());

            // A státusz TextView színének beállítása a státusz alapján
            String Tervben_van = "Tervben van";
            String Kiolvasva = "Kiolvasva";
            String Most_olvasom = "Most olvasom";
            String Szüneteltetem = "Szüneteltetem";
            String Abbahagytam = "Abbahagytam";
            String statusText = textViewStatus.getText().toString();
            if (statusText.equals(Tervben_van)) {
                textViewStatus.setTextColor(Color.parseColor("#FFFF00"));
            } else if (statusText.equals(Kiolvasva)) {
                textViewStatus.setTextColor(Color.parseColor("#00A36C"));
            } else if (statusText.equals(Most_olvasom)) {
                textViewStatus.setTextColor(Color.parseColor("#008000"));
            } else if (statusText.equals(Szüneteltetem)) {
                textViewStatus.setTextColor(Color.parseColor("#808080"));
            } else if (statusText.equals(Abbahagytam)) {
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            return view;
        }
    }

    // AsyncTask az HTTP kérések kezelésére
    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");
            try {
                if (requestType.equals("GET")) {
                    response = RequestHandler.get(requestUrl, token);
                }
            } catch (IOException e) {
                // Hibaüzenet megjelenítése
                Toast.makeText(Library.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getResponseCode() >= 400) {
                Toast.makeText(Library.this, "" + response.getResponseMessage(), Toast.LENGTH_LONG).show();
                Log.d("onPostExecuteError: ", response.getResponseMessage());
                return;
            }
            Gson converter = new Gson();
            if (requestType.equals("GET")) {
                MyBooks[] bookarray = converter.fromJson(response.getResponseMessage(), MyBooks[].class);
                books.clear();
                books.addAll(Arrays.asList(bookarray));
                listView.invalidateViews();
                Toast.makeText(Library.this, "Sikeres adat lekérdezés", Toast.LENGTH_SHORT).show();
            }
        }
    }
}