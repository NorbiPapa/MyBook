package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    // Splash képernyőn töltődési idő beállítása
    private static int splashtimeout=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Címsor elrejtése és teljes képernyő beállítása
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        // Handler használata a SplashActivity elindításának késleltetésére
        new Handler().postDelayed(()->{
            // Token ellenőrzése a SharedPreferences-ben
            SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
            String token=sharedPreferences.getString("token","");
            // Ha a token üres, akkor átlépés a bejelentkező aktivitásra
            if (token.isEmpty()){
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            // Ha a token nem üres, akkor átlépés a főmenüre
            else {
                Intent intent=new Intent(SplashActivity.this,MainMenz.class);
                startActivity(intent);
                finish();
            }
        }, splashtimeout); // Várakozás a beállított időtartamig
    }
}