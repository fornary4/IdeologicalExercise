package com.fornary4.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_ok).setOnClickListener(v -> finish());
    }
}