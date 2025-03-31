package com.example.project4.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CountryQuizDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "CountryQuizDbHelper";
    private static final String DATABASE_NAME = "countryquiz.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_COUNTRIES = "countries";
    public static final String TABLE_QUIZZES = "quizzes";

    // Countries table columns
    public static final String COL_COUNTRY_ID = "id";
    public static final String COL_COUNTRY_NAME = "name";
    public static final String COL_COUNTRY_CONTINENT = "continent";

    // Quizzes table columns
    public static final String COL_QUIZ_ID = "id";
    public static final String COL_QUIZ_DATE = "quiz_date";
    public static final String COL_QUIZ_SCORE = "score";

    // Singleton instance
    private static CountryQuizDbHelper instance;

    public static synchronized CountryQuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CountryQuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private CountryQuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for countries.
        String createCountries = "CREATE TABLE " + TABLE_COUNTRIES + " (" +
                COL_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COUNTRY_NAME + " TEXT NOT NULL, " +
                COL_COUNTRY_CONTINENT + " TEXT NOT NULL);";
        db.execSQL(createCountries);

        // Create table for quizzes.
        String createQuizzes = "CREATE TABLE " + TABLE_QUIZZES + " (" +
                COL_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_QUIZ_DATE + " TEXT NOT NULL, " +
                COL_QUIZ_SCORE + " INTEGER NOT NULL);";
        db.execSQL(createQuizzes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        onCreate(db);
    }

    /**
     * Checks whether the countries table has been populated.
     * (For simplicity, this example assumes that if at least one record exists, the DB is populated.)
     */
    public boolean isDatabasePopulated() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_COUNTRIES;
        try (android.database.Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking database population", e);
        }
        return false;
    }
}