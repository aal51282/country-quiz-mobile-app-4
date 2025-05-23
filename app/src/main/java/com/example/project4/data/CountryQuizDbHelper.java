package com.example.project4.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class for managing the SQLite database for the country quiz app.
 */
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

    /**
     * Returns the singleton instance of the database helper.
     * @param context The application context.
     * @return The singleton instance of the database helper.
     */
    public static synchronized CountryQuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CountryQuizDbHelper(context.getApplicationContext());
        }
        return instance;
    } // getInstance

    /**
     * Private constructor to prevent direct instantiation.
     * @param context The application context.
     */
    private CountryQuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } // CountryQuizDbHelper

    /**
     * Called when the database is created for the first time.
     * @param db The database object.
     */
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
    } // onCreate

    /**
     * Called when the database needs to be upgraded.
     * @param db The database object.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        onCreate(db);
    } // onUpgrade

    /**
     * Checks if the database is populated with countries.
     * @return True if the database is populated, false otherwise.
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
    } // isDatabasePopulated
} // CountryQuizDbHelper