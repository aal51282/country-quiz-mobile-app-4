package com.example.project4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

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
    private static final String KEY_QUIZ_STATE = "quiz_state";
    private static final String KEY_CURRENT_POSITION = "current_position";

    private Quiz quiz;
    private ViewPager2 viewPager;
    private QuizPagerAdapter pagerAdapter;
    private List<Country> allCountries; // Loaded from the database
    private CountryQuizDbHelper dbHelper;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = CountryQuizDbHelper.getInstance(this);
        viewPager = findViewById(R.id.quizViewPager);
        tvProgress = findViewById(R.id.tvProgress);

        // Set initial progress text
        tvProgress.setText(getString(R.string.progress_text, 1, NUM_QUESTIONS));

        // If we have a saved instance, restore the quiz
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_QUIZ_STATE)) {
            // Restore quiz from saved instance
            quiz = (Quiz) savedInstanceState.getSerializable(KEY_QUIZ_STATE);
            int currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);

            // Update progress text for restored position
            tvProgress.setText(getString(R.string.progress_text, currentPosition + 1, NUM_QUESTIONS));

            // Load countries and then set up the restored quiz
            new LoadCountriesTask(true, currentPosition).execute();
        } else {
            // Start a fresh quiz by loading countries first
            new LoadCountriesTask(false, 0).execute();
        }

        // Disable swiping right (to previous questions)
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean isScrolling = false;
            private int currentPosition = 0;

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isScrolling = true;
                    currentPosition = viewPager.getCurrentItem();
                } else if (state == ViewPager2.SCROLL_STATE_SETTLING) {
                    isScrolling = false;
                } else if (state == ViewPager2.SCROLL_STATE_IDLE && isScrolling) {
                    isScrolling = false;
                    // If trying to swipe to a previous question, revert back
                    if (viewPager.getCurrentItem() < currentPosition) {
                        viewPager.setCurrentItem(currentPosition, true);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                // Update progress text when page changes
                tvProgress.setText(getString(R.string.progress_text, position + 1, NUM_QUESTIONS));
            }
        });
    }

    private class LoadCountriesTask extends AsyncTask<Void, Void, List<Country>> {
        private boolean isRestoring;
        private int positionToRestore;

        public LoadCountriesTask(boolean isRestoring, int positionToRestore) {
            this.isRestoring = isRestoring;
            this.positionToRestore = positionToRestore;
        }

        @Override
        protected List<Country> doInBackground(Void... voids) {
            return loadCountriesFromDatabase();
        }

        @Override
        protected void onPostExecute(List<Country> countries) {
            allCountries = countries;
            if (allCountries.size() < NUM_QUESTIONS) {
                Log.e(TAG, "Not enough countries in database");
                return;
            }

            if (isRestoring && quiz != null) {
                // Set up UI with the restored quiz
                setupQuizUI(positionToRestore);
            } else {
                // Create a new quiz
                createNewQuiz();
            }
        }
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
        // Randomly select NUM_QUESTIONS countries without duplicates
        Set<Integer> indices = new HashSet<>();
        Random rand = new Random();
        while (indices.size() < NUM_QUESTIONS) {
            indices.add(rand.nextInt(allCountries.size()));
        }

        List<Question> questions = new ArrayList<>();
        for (Integer index : indices) {
            Country country = allCountries.get(index);
            // Get the correct answer
            String correctContinent = country.getContinent();

            // Prepare two incorrect continent answers
            List<String> continents = new ArrayList<>();
            // List all possible continents
            continents.add("Africa");
            continents.add("Asia");
            continents.add("Europe");
            continents.add("North America");
            continents.add("South America");
            continents.add("Oceania");

            // Remove the correct continent
            continents.remove(correctContinent);

            // Randomly pick two incorrect answers
            List<String> incorrectAnswers = new ArrayList<>();
            while (incorrectAnswers.size() < 2) {
                if (continents.isEmpty()) break;
                String candidate = continents.remove(rand.nextInt(continents.size()));
                incorrectAnswers.add(candidate);
            }

            Question question = new Question(country, correctContinent, incorrectAnswers);
            questions.add(question);
        }

        quiz = new Quiz(questions);
        setupQuizUI(0); // Start at the first question
    }

    private void setupQuizUI(int startPosition) {
        // Initialize the ViewPager2 with the question fragments
        pagerAdapter = new QuizPagerAdapter(this, quiz);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(startPosition, false);

        // Disable swiping back to previous questions
        viewPager.setUserInputEnabled(true);
    }

    /**
     * This callback is invoked when a user selects an answer in a QuestionFragment
     */
    @Override
    public void onAnswerSelected(int questionPosition, String answer) {
        Question currentQuestion = quiz.getQuestions().get(questionPosition);
        currentQuestion.setUserAnswer(answer);
        if (answer.equals(currentQuestion.getCorrectAnswer())) {
            quiz.incrementScore();
        }
        quiz.moveToNextQuestion();

        // If there are more questions, move to the next fragment
        if (!quiz.isFinished()) {
            viewPager.setCurrentItem(questionPosition + 1, true);
        } else {
            // Quiz finished – store the quiz result asynchronously in the DB
            new StoreQuizResultTask().execute();
        }
    }

    private class StoreQuizResultTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            storeQuizResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Once stored, show the result
            Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
            intent.putExtra("score", quiz.getCurrentScore());
            startActivity(intent);
            finish();
        }
    }

    private void storeQuizResult() {
        // Insert the quiz date and score into the quizzes table
        String date = String.valueOf(System.currentTimeMillis());
        String insertSQL = "INSERT INTO " + CountryQuizDbHelper.TABLE_QUIZZES +
                " (" + CountryQuizDbHelper.COL_QUIZ_DATE + ", " +
                CountryQuizDbHelper.COL_QUIZ_SCORE + ") VALUES (?, ?)";
        dbHelper.getWritableDatabase().execSQL(insertSQL,
                new Object[]{date, quiz.getCurrentScore()});
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (quiz != null) {
            outState.putSerializable(KEY_QUIZ_STATE, quiz);
            outState.putInt(KEY_CURRENT_POSITION, viewPager.getCurrentItem());
        }
    }
}