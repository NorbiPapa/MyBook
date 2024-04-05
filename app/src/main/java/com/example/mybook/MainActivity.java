package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public Button loginbtn;
    public Button registerbt;
    public EditText username;
    public EditText password;

    public  String url="http://10.0.2.2:3000/auth/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        init();
        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=username.getText().toString();
                String jelszo=password.getText().toString();

                Login login=new Login(email, jelszo);
                Gson gsonconverter=new Gson();
                RequestTask task=new RequestTask(url,"POST",gsonconverter.toJson(login));
                task.execute();
            }
        });
    }
    public void init(){
        loginbtn=findViewById(R.id.LoginButton);
        registerbt=findViewById(R.id.RegisterButton1);
        username=findViewById(R.id.usernameedit);
        password=findViewById(R.id.passedit);
    }
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
                    response = RequestHandler.post(requestUrl, requestParams);
                }
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, ""+response.getResponseMessage(), Toast.LENGTH_LONG).show();
                Log.d("onPostExecuteError: ", response.getResponseMessage());
                return;
            }
            if (requestType.equals("POST")) {
                SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token",response.getResponseMessage());
                editor.commit();
                Toast.makeText(MainActivity.this, "Sikeres Bejelentkezés", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this, MainMenz.class);
                startActivity(intent);
                finish();
            }
        }
    }
}