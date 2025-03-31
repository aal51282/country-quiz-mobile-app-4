package com.example.project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.project4.data.CountryQuizDbHelper;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CountryQuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = CountryQuizDbHelper.getInstance(this);

        // If this is the first launch, initialize the database from CSV asynchronously.
        if (!dbHelper.isDatabasePopulated()) {
            new CSVAsyncTask(this, dbHelper).execute();
        }

        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);
        Button btnViewResults = findViewById(R.id.btnStartQuiz);

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        btnViewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });
    }
}