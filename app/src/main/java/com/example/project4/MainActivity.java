package com.example.project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


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
            Toast.makeText(this, "Initializing database from CSV file...", Toast.LENGTH_SHORT).show();
            new CSVAsyncTask(this, dbHelper).execute();
        } // if

        // Set up the continents map image
        ImageView ivContinentsMap = findViewById(R.id.ivContinentsMap);
        ivContinentsMap.setImageResource(R.drawable.continents_map);

        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);
        Button btnViewResults = findViewById(R.id.btnViewResults);  // Fixed ID

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            } // onClick
        });

        btnViewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
            } // onClick
        });
    } // onCreate
} // MainActivity