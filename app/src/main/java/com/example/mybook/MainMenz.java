package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.SearchView;

public class MainMenz extends AppCompatActivity {

    public Button mylibrarybtn;
    public EditText searchview;
    public Button backlogbtn;
    public Button logoutbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menz);
        init();
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenz.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void init(){
        mylibrarybtn=findViewById(R.id.mylibrarybtn);
        searchview=findViewById(R.id.searchView);
        backlogbtn=findViewById(R.id.backlogbtn);
        logoutbtn=findViewById(R.id.logoutbtn);
    }
}