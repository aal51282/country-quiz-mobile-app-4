package com.example.project4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.project4.data.CountryQuizDbHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Asynchronous task to populate the database from a CSV file.
 */
public class CSVAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "CSVAsyncTask";
    private Context context;
    private CountryQuizDbHelper dbHelper;

    /**
     * Constructs a CSVAsyncTask object with the given context and database helper.
     * @param context The application context.
     * @param dbHelper The database helper.
     */
    public CSVAsyncTask(Context context, CountryQuizDbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    } // CSVAsyncTask

    /**
     * Populates the database from a CSV file.
     * @param voids The parameters of the task.
     *
     * @return True if the database is populated successfully, false otherwise.
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            InputStream is = context.getAssets().open("country_continent.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Begin transaction for efficiency
            db.beginTransaction();
            try {
                while ((line = reader.readLine()) != null) {
                    // Assumes CSV file is formatted as: country,continent
                    String[] tokens = line.split(",");
                    if (tokens.length >= 2) {
                        String countryName = tokens[0].trim();
                        String continent = tokens[1].trim();

                        String insertSQL = "INSERT INTO " + CountryQuizDbHelper.TABLE_COUNTRIES +
                                " (" + CountryQuizDbHelper.COL_COUNTRY_NAME + ", " +
                                CountryQuizDbHelper.COL_COUNTRY_CONTINENT + ") VALUES (?, ?)";
                        db.execSQL(insertSQL, new Object[]{countryName, continent});
                    } // if
                } // while
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                reader.close();
            } // try
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error reading CSV file", e);
            return false;
        } // try
    } // doInBackground

    /**
     * Called after the task is completed.
     * @param result The result of the task.
     */
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.d(TAG, "Database populated successfully from CSV.");
        } else {
            Log.e(TAG, "Failed to populate database from CSV.");
        } // if
    } // onPostExecute
} // CSVAsyncTask