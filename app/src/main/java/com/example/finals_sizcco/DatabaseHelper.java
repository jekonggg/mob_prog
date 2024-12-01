package com.example.finals_sizcco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 4; // Incremented version for the new table

    // Table Name and Columns for "records" table
    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_RECORDS_ID = "id";
    public static final String COLUMN_RECORDS_USERNAME = "username";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_UPDATED_AT = "updated_at"; // New column for timestamp

    // Table Name and Columns for "users" table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ALLOWANCE_AMOUNT = "allowance_amount"; // Column added in version 2


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create "records" table with the new "updated_at" column
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + " (" +
                COLUMN_RECORDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORDS_USERNAME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_UPDATED_AT + " TEXT)";  // New column added
        db.execSQL(CREATE_RECORDS_TABLE);

        // Create "users" table with allowance_amount column from version 2
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ALLOWANCE_AMOUNT + " TEXT)"; // Column added in version 2
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // If upgrading from version 1 to version 2, add the "allowance_amount" column to the "users" table
            String ADD_ALLOWANCE_AMOUNT_COLUMN = "ALTER TABLE " + TABLE_USERS +
                    " ADD COLUMN " + COLUMN_ALLOWANCE_AMOUNT + " TEXT";
            db.execSQL(ADD_ALLOWANCE_AMOUNT_COLUMN);
        }
        if (oldVersion < 3) {
            // If upgrading from version 2 to version 3, add the "updated_at" column to the "records" table
            String ADD_UPDATED_AT_COLUMN = "ALTER TABLE " + TABLE_RECORDS +
                    " ADD COLUMN " + COLUMN_UPDATED_AT + " TEXT";
            db.execSQL(ADD_UPDATED_AT_COLUMN);
        }
    }


    // Insert record method for "records" table
    public boolean insertRecord(String username, String price, String category, String date, String time, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECORDS_USERNAME, username);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_NOTES, notes);

        long result = db.insert(TABLE_RECORDS, null, values);
        db.close();
        return result != -1;  // Return true if record is inserted, false otherwise
    }

    // Update a record in the "records" table
    public boolean updateRecord(int id, String price, String date, String time, String notes, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_CATEGORY, category);

        int rowsAffected = db.update(TABLE_RECORDS, values, COLUMN_RECORDS_ID + "=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;  // Return true if the update was successful
    }

    // Delete a record from the "records" table
    public boolean deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RECORDS, COLUMN_RECORDS_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;  // Return true if the record was deleted
    }

    // Validate username and password
    public boolean validateUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", selectionArgs, null, null, null);

        // Check if the username and password match a record in the database
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // User is valid
        } else {
            cursor.close();
            return false; // Invalid user
        }
    }

    // Method to get the sum of prices per category
    public double getTotalByCategory(String category, String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(price) FROM " + TABLE_RECORDS +
                " WHERE category = ? AND username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category, username});
        double total = 0.0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // Method to get the total expenses per category for a specific user
    public HashMap<String, Double> getUserExpenses(String username) {
        HashMap<String, Double> expenses = new HashMap<>();

        // SQL query to retrieve expenses for the user, grouped by category
        String query = "SELECT category, SUM(price) as total FROM expenses WHERE username = ? GROUP BY category";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            // Check if the columns exist
            int categoryIndex = cursor.getColumnIndex("category");
            int totalIndex = cursor.getColumnIndex("total");

            if (categoryIndex == -1 || totalIndex == -1) {
                Log.e("Database Error", "Column not found: category or total");
            } else {
                do {
                    String category = cursor.getString(categoryIndex);
                    double total = cursor.getDouble(totalIndex);
                    expenses.put(category, total);
                } while (cursor.moveToNext());
            }
        } else {
            Log.e("Database Error", "Cursor is empty or invalid.");
        }

        cursor.close();
        db.close();
        return expenses;
    }
}
