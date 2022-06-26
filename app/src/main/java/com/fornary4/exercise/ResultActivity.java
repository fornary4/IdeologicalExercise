package com.fornary4.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fornary4.exercise.adapter.ListResultAdapter;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ListView lv_result = findViewById(R.id.lv_result);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        List<Integer> extract = bundle.getIntegerArrayList("extract");
        List<String> choices = bundle.getStringArrayList("userChoices");
        ListResultAdapter adapter = new ListResultAdapter(this, extract, choices);
        lv_result.setAdapter(adapter);

    }
}