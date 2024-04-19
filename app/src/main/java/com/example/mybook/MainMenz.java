package com.example.mybook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenz extends AppCompatActivity {

    // Gombok, szövegmező és URL-ek deklarálása
    public Button mylibrarybtn;
    public EditText searchview;
    public Button logoutbtn;
    public String url = "http://10.0.2.2:3000/books/SearchName";
    public String url2 = "http://10.0.2.2:3000/books/Author/";
    public String url3 = "http://10.0.2.2:3000/books/Status/";
    public AlertDialog.Builder builder;


    // ListView és lista a könyvek tárolásához
    public ListView listView;
    public List<Books> books = new ArrayList<>();

    // Könyv státuszok tárolása és kiválasztott státusz indexe
    public List<String> status;
    public String statussave;
    public int statusId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Teljes képernyő beállítása és a címsor elrejtése
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menz);
        // Inicializáció és adatlekérdezés indítása
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
        // Kattintáskezelők beállítása
        mylibrarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Könyvtár aktivitásra váltás
                Intent intent = new Intent(MainMenz.this, Library.class);
                startActivity(intent);
                finish();
            }
        });
        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Keresés végrehajtása
                RequestTask req = new RequestTask(url2, "POST", s.toString());
                req.execute();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Könyv elem kiválasztása és státuszának frissítése
                // Státusz párbeszédpanel megjelenítése
                String author = books.get(position).getWriter();
                String title = books.get(position).getBookname();
                int release = books.get(position).getRelease();
                int bookid = books.get(position).getId();
                builder.setTitle(title + "(" + author + ")");
                builder.setSingleChoiceItems(status.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        statussave = status.get(which);
                        statusId = which;
                        statusId += 1;
                        Toast.makeText(MainMenz.this, "" + statusId, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Gson gson = new Gson();
                        Status status1 = new Status(statusId);
                        RequestTask task = new RequestTask(url3 + bookid, "POST2", gson.toJson(status1));
                        task.execute();
                        Toast.makeText(MainMenz.this, "" + gson.toJson(status1), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();

                Toast.makeText(MainMenz.this, "" + books.get(position).getBookname(), Toast.LENGTH_SHORT).show();
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kijelentkezés és visszalépés a bejelentkező aktivitásra
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainMenz.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // View-k inicializálása
    public void init() {
        mylibrarybtn = findViewById(R.id.mylibrarybtn);
        searchview = findViewById(R.id.searchView);
        logoutbtn = findViewById(R.id.logoutbtn);
        listView = findViewById(R.id.listview);
        listView.setAdapter(new BooksAdapter());
        builder = new AlertDialog.Builder(this);

        // Státuszok inicializálása
        status = new ArrayList<>();
        status.add("Tervben van");
        status.add("Kiolvasva");
        status.add("Most olvasom");
        status.add("Szüneteltetem");
        status.add("Abbahagytam");


    }

    // ListView-hez tartozó adapter
    private class BooksAdapter extends ArrayAdapter<Books> {

        public BooksAdapter() {
            super(MainMenz.this, R.layout.book_list_items, books);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            //view létrehozása a book_list_items.xml-ből
            View view = inflater.inflate(R.layout.book_list_items, null, false);
            //library_list_items.xml-ben lévő elemek inicializálása
            TextView textViewBookTitle = view.findViewById(R.id.BookTitle);
            TextView textViewBookAuthor = view.findViewById(R.id.BookAuthor);
            TextView textViewBookYear = view.findViewById(R.id.BookYear);
            //actualBook létrehozása a book listából
            Books actualBook = books.get(position);

            // Könyv részleteinek beállítása
            textViewBookTitle.setText(actualBook.getBookname());
            textViewBookAuthor.setText(actualBook.getWriter());
            textViewBookYear.setText(String.valueOf(actualBook.getRelease()));


            return view;
        }
    }
    // HTTP kéréseket kezelő AsyncTask
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
            // Különböző HTTP kérések végrehajtása a requestType alapján
            try {
                if (requestType.equals("GET")) {
                    response = RequestHandler.get(requestUrl, null);
                }
                if (requestType.equals("POST")) {
                    response = RequestHandler.post(requestUrl + requestParams, token);
                }
                if (requestType.equals("POST2")) {
                    response = RequestHandler.post(requestUrl, requestParams, token);
                }
            } catch (IOException e) {
                Toast.makeText(MainMenz.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getResponseCode() >= 400) {
                Toast.makeText(MainMenz.this, "" + response.getResponseMessage(), Toast.LENGTH_LONG).show();
                Log.d("onPostExecuteError: ", response.getResponseMessage());
                return;
            }
            Gson converter = new Gson();
            if (requestType.equals("GET")) {
                Books[] bookarray = converter.fromJson(response.getResponseMessage(), Books[].class);
                books.clear();
                books.addAll(Arrays.asList(bookarray));
                listView.invalidateViews();

            }
            if (requestType.equals("POST")) {
                Books[] bookarray = converter.fromJson(response.getResponseMessage(), Books[].class);
                books.clear();
                books.addAll(Arrays.asList(bookarray));
                listView.invalidateViews();
            }
            if (requestType.equals("POST2")) {
                Toast.makeText(MainMenz.this, "Sikeres hozzáadás", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
