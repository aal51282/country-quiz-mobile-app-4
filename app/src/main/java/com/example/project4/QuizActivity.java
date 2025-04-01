package com.example.project4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.project4.data.Country;
import com.example.project4.data.CountryQuizDbHelper;
import com.example.project4.data.Question;
import com.example.project4.data.Quiz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements QuestionFragment.OnAnswerSelectedListener {

    private static final String TAG = "QuizActivity";
    private static final int NUM_QUESTIONS = 6;

    private Quiz quiz;
    private ViewPager viewPager;
    private QuizPagerAdapter pagerAdapter;
    private List<Country> allCountries; // Loaded from the database
    private CountryQuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = CountryQuizDbHelper.getInstance(this);
        viewPager = findViewById(R.id.quizViewPager);

        // Load all countries asynchronously from the database.
        new AsyncTask<Void, Void, List<Country>>() {
            @Override
            protected List<Country> doInBackground(Void... voids) {
                // TODO: Implement actual query to load countries from the DB.
                // For demonstration, returning an empty list.
                return loadCountriesFromDatabase();
            }

            @Override
            protected void onPostExecute(List<Country> countries) {
                allCountries = countries;
                if (allCountries.size() < NUM_QUESTIONS) {
                    Log.e(TAG, "Not enough countries in database");
                    return;
                }
                createNewQuiz();
            }
        }.execute();
    }

    private List<Country> loadCountriesFromDatabase() {
        List<Country> countries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to retrieve all countries
        String query = "SELECT " +
                CountryQuizDbHelper.COL_COUNTRY_ID + ", " +
                CountryQuizDbHelper.COL_COUNTRY_NAME + ", " +
                CountryQuizDbHelper.COL_COUNTRY_CONTINENT +
                " FROM " + CountryQuizDbHelper.TABLE_COUNTRIES;

        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COL_COUNTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COL_COUNTRY_NAME));
                String continent = cursor.getString(cursor.getColumnIndexOrThrow(CountryQuizDbHelper.COL_COUNTRY_CONTINENT));

                countries.add(new Country(id, name, continent));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading countries from database", e);
        }

        return countries;
    }

    private void createNewQuiz() {
        // Randomly select NUM_QUESTIONS countries without duplicates.
        Set<Integer> indices = new HashSet<>();
        Random rand = new Random();
        while (indices.size() < NUM_QUESTIONS) {
            indices.add(rand.nextInt(allCountries.size()));
        }

        List<Question> questions = new ArrayList<>();
        for (Integer index : indices) {
            Country country = allCountries.get(index);
            // Get the correct answer.
            String correctContinent = country.getContinent();

            // Prepare two incorrect continent answers.
            List<String> continents = new ArrayList<>();
            // For simplicity, list all possible continents.
            continents.add("Africa");
            continents.add("Asia");
            continents.add("Europe");
            continents.add("North America");
            continents.add("South America");
            continents.add("Oceania");
            continents.remove(correctContinent);

            // Randomly pick two incorrect answers.
            List<String> incorrectAnswers = new ArrayList<>();
            while (incorrectAnswers.size() < 2) {
                String candidate = continents.get(rand.nextInt(continents.size()));
                if (!incorrectAnswers.contains(candidate)) {
                    incorrectAnswers.add(candidate);
                }
            }

            Question question = new Question(country, correctContinent, incorrectAnswers);
            questions.add(question);
        }

        quiz = new Quiz(questions);

        // Initialize the ViewPager with the question fragments.
        pagerAdapter = new QuizPagerAdapter(getSupportFragmentManager(), quiz);
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * This callback is invoked when a user selects an answer in a QuestionFragment.
     */
    @Override
    public void onAnswerSelected(int questionPosition, String answer) {
        Question currentQuestion = quiz.getQuestions().get(questionPosition);
        currentQuestion.setUserAnswer(answer);
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            quiz.incrementScore();
        }
        quiz.moveToNextQuestion();

        // If there are more questions, move to the next fragment.
        if (!quiz.isFinished()) {
            viewPager.setCurrentItem(questionPosition + 1);
        } else {
            // Quiz finished – store the quiz result asynchronously in the DB.
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    storeQuizResult();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // Once stored, show the result.
                    Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                    intent.putExtra("score", quiz.getCurrentScore());
                    startActivity(intent);
                    finish();
                }
            }.execute();
        }
    }

    private void storeQuizResult() {
        // Insert the quiz date and score into the quizzes table.
        String date = String.valueOf(System.currentTimeMillis());
        String insertSQL = "INSERT INTO " + CountryQuizDbHelper.TABLE_QUIZZES +
                " (" + CountryQuizDbHelper.COL_QUIZ_DATE + ", " +
                CountryQuizDbHelper.COL_QUIZ_SCORE + ") VALUES (?, ?)";
        dbHelper.getWritableDatabase().execSQL(insertSQL,
                new Object[]{date, quiz.getCurrentScore()});
    }

    // TODO: Implement onSaveInstanceState/onRestoreInstanceState to save quiz progress.
}