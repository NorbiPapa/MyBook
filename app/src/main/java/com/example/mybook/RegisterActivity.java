package com.example.mybook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;


public class RegisterActivity extends AppCompatActivity {

    // Nézetek deklarálása
    public Button send;
    public EditText emailedit;
    public EditText usernameedit;
    public EditText passwordedit;
    public Button backtobtn;

    // URL a regisztrációhoz
    public  String url="http://10.0.2.2:3000/users/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        // Nézetek inicializálása
        init();

        // Kattintáskezelő a vissza gombhoz
        backtobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Visszalépés a főmenübe
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Kattintáskezelő az "Elküldés" gombhoz
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Felhasználónév, email és jelszó begyűjtése
                String username = usernameedit.getText().toString().trim();
                String email= emailedit.getText().toString().trim();
                String password= passwordedit.getText().toString().trim();

                // Ellenőrzés: tartalmaz-e a jelszó betűket és számokat
                boolean ContainsLetters=false;
                boolean ContainsNumbers=false;
                for (int i = 0; i < password.length(); i++) {
                    char c= password.charAt(i);
                    if (Character.isLetter(c)) {
                        ContainsLetters=true;
                    } else if (Character.isDigit(c)) {
                        ContainsNumbers=true;
                    }
                }

                // Ellenőrzés: érvényes-e az email formátuma
                if (!email.isEmpty() && !email.contains("@")) {
                    // Hibás email esetén üzenet megjelenítése
                    AlertDialog.Builder builder =new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Érvényes email cím szükséges");
                    builder.setPositiveButton("Ok", null);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                } else if (username.length()<5) {
                    // Hibás felhasználónév esetén üzenet megjelenítése
                    AlertDialog.Builder builder =new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("A felhasználónév legalább 5 karakter hosszú legyen");
                    builder.setPositiveButton("Ok", null);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                } else if (!ContainsLetters || !ContainsNumbers) {
                    // Hibás jelszó esetén üzenet megjelenítése
                    AlertDialog.Builder builder =new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("A jelszónak tartalmaznia kell betűket és számokat");
                    builder.setPositiveButton("Ok", null);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                else {
                    // Ha minden adat helyes, regisztráció végrehajtása
                    Users user=new Users(email,username,password);
                    Gson gsonconverter=new Gson();
                    RequestTask task=new RequestTask(url,"POST",gsonconverter.toJson(user));
                    task.execute();
                }
            }
        });
    }

    // Nézetek inicializálása
    public void init(){
        emailedit=findViewById(R.id.emailEditRegister);
        backtobtn=findViewById(R.id.backtobtn);
        send=findViewById(R.id.send);
        usernameedit=findViewById(R.id.usernameEditRegister);
        passwordedit=findViewById(R.id.passEditrRgister);
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

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                if (requestType.equals("POST")) {
                    // POST kérés végrehajtása
                    response = RequestHandler.post(requestUrl, requestParams, null);
                }
            } catch (IOException e) {
                // Hibaüzenet megjelenítése
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.getResponseCode() >= 400) {
                // Hibaüzenet megjelenítése
                Toast.makeText(RegisterActivity.this, ""+response.getResponseMessage(), Toast.LENGTH_LONG).show();
                Log.d("onPostExecuteError: ", response.getResponseMessage());
                return;
            }
            if (requestType.equals("POST")) {
                // Sikeres regisztráció esetén visszalépés a főmenübe
                Toast.makeText(RegisterActivity.this, "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}