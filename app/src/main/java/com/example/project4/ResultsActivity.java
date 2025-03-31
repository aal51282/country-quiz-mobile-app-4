package com.example.project4;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project4.data.CountryQuizDbHelper;


public class ResultsActivity extends AppCompatActivity {

    private CountryQuizDbHelper dbHelper;
    private TextView tvScore;
    private ListView lvPastResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        tvScore = findViewById(R.id.tvScore);
        lvPastResults = findViewById(R.id.lvPastResults);
        dbHelper = CountryQuizDbHelper.getInstance(this);

        // Show current quiz score if passed from QuizActivity.
        int score = getIntent().getIntExtra("score", -1);
        if (score != -1) {
            tvScore.setText("Your score: " + score);
        }

        // Load past quiz results asynchronously.
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String query = "SELECT " + "rowid _id, " +
                        CountryQuizDbHelper.COL_QUIZ_DATE + ", " +
                        CountryQuizDbHelper.COL_QUIZ_SCORE +
                        " FROM " + CountryQuizDbHelper.TABLE_QUIZZES +
                        " ORDER BY " + CountryQuizDbHelper.COL_QUIZ_DATE + " DESC";
                return dbHelper.getReadableDatabase().rawQuery(query, null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                // Use a SimpleCursorAdapter to bind results to the ListView.
                String[] from = {CountryQuizDbHelper.COL_QUIZ_DATE, CountryQuizDbHelper.COL_QUIZ_SCORE};
                int[] to = {R.id.tvResultDate, R.id.tvResultScore};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(ResultsActivity.this,
                        R.layout.item_result, cursor, from, to, 0);
                lvPastResults.setAdapter(adapter);
            }
        }.execute();
    }
}