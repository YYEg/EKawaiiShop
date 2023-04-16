package com.yy.ekawaiishop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yy.ekawaiishop.R;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        progressBar = findViewById(R.id.progressBar);

        //Кнопка старт
        Button btnStart = findViewById(R.id.btnStartPageStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                Intent LoginActivity = new Intent(MainActivity.this, com.yy.ekawaiishop.activity.LoginActivity.class);
                startActivity(LoginActivity);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}