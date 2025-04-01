package com.example.project4;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project4.data.CountryQuizDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The activity that displays the quiz results.
 */
public class ResultsActivity extends AppCompatActivity {

    private CountryQuizDbHelper dbHelper;
    private TextView tvScore;
    private ListView lvPastResults;

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        tvScore = findViewById(R.id.tvScore);
        lvPastResults = findViewById(R.id.lvPastResults);
        dbHelper = CountryQuizDbHelper.getInstance(this);

        // Show current quiz score if passed from QuizActivity
        int score = getIntent().getIntExtra("score", -1);
        if (score != -1) {
            tvScore.setText(getString(R.string.quiz_score, score, 6));
        } else {
            tvScore.setText(R.string.past_results);
        } // if

        // Load past quiz results asynchronously
        new LoadResultsTask().execute();
    } // onCreate

    /**
     * Asynchronous task to load quiz results from the database.
     */
    private class LoadResultsTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            String query = "SELECT " + "rowid _id, " +
                    CountryQuizDbHelper.COL_QUIZ_DATE + ", " +
                    CountryQuizDbHelper.COL_QUIZ_SCORE +
                    " FROM " + CountryQuizDbHelper.TABLE_QUIZZES +
                    " ORDER BY " + CountryQuizDbHelper.COL_QUIZ_DATE + " DESC";
            return dbHelper.getReadableDatabase().rawQuery(query, null);
        } // doInBackground

        /**
         * Called after the task is completed.
         * @param cursor The result of the operation computed by {@link #doInBackground}.
         *
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            // Create a custom SimpleCursorAdapter with a date formatter
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    ResultsActivity.this,
                    R.layout.item_result,
                    cursor,
                    new String[]{CountryQuizDbHelper.COL_QUIZ_DATE, CountryQuizDbHelper.COL_QUIZ_SCORE},
                    new int[]{R.id.tvResultDate, R.id.tvResultScore},
                    0
            ) { // SimpleCursorAdapter

                /**
                 * Sets the text for a TextView.
                 * @param v TextView to receive text
                 * @param text the text to be set for the TextView
                 */
                @Override
                public void setViewText(TextView v, String text) {
                    if (v.getId() == R.id.tvResultDate) {
                        try {
                            // Convert timestamp to readable date
                            long timestamp = Long.parseLong(text);
                            Date date = new Date(timestamp);
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                            text = sdf.format(date);
                        } catch (NumberFormatException e) {
                            // If parsing fails, use the text as is
                        }
                    } else if (v.getId() == R.id.tvResultScore) {
                        text = text + "/6";
                    } // if
                    super.setViewText(v, text);
                } // setViewText
            };
            lvPastResults.setAdapter(adapter);
        } // onPostExecute
    } // LoadResultsTask
} // ResultsActivity