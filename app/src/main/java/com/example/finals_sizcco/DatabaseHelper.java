package com.example.finals_sizcco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 3; // Incremented version for the new table

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
    public static final String TABLE_USERS = "users_data";
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

    // Insert user method for "users" table
    public boolean insertUser(String username, String password, String email, String allowanceAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ALLOWANCE_AMOUNT, allowanceAmount); // Store allowance amount in the database

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // Return true if user is inserted, false otherwise
    }

    // Retrieve records method
    public Cursor getAllRecords(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECORDS,  // Table name
                null,               // All columns (*)
                COLUMN_RECORDS_USERNAME + "=?", // Where clause
                new String[]{username},  // Where clause arguments
                null,               // Group by
                null,               // Having
                COLUMN_DATE + " DESC"); // Order by
    }

    // Check if username exists in the users table
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Retrieve a user by username
    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS,
                null,
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);
    }

    // Retrieve the allowance amount for a user
    public String getAllowanceAmount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,  // The table we are querying
                new String[]{COLUMN_ALLOWANCE_AMOUNT},  // The column we are interested in
                COLUMN_USERNAME + "=?",  // The WHERE clause to filter by username
                new String[]{username},  // The username value to match
                null,  // GROUP BY (no grouping required)
                null,  // HAVING (no additional filters)
                null);  // ORDER BY (no sorting required)

        if (cursor != null) {
            if (cursor.moveToFirst()) {  // Check if the cursor is not empty
                int columnIndex = cursor.getColumnIndex(COLUMN_ALLOWANCE_AMOUNT); // Get the index of the column
                if (columnIndex >= 0) { // Ensure the column exists
                    String allowanceAmount = cursor.getString(columnIndex);  // Retrieve the value from the column
                    cursor.close();
                    db.close();
                    return allowanceAmount;  // Return the allowance amount
                } else {
                    cursor.close();
                    db.close();
                    return null;  // Return null if the column index is invalid
                }
            } else {
                cursor.close();
                db.close();
                return null;  // Return null if no rows were returned
            }
        } else {
            db.close();
            return null;  // Return null if cursor is null
        }
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
        Cursor cursor = null;

        try {
            String[] selectionArgs = {username, password};
            cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", selectionArgs, null, null, null);

            // Check if the username and password match a record in the database
            if (cursor != null && cursor.getCount() > 0) {
                return true; // Valid user
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return false; // Invalid user
    }
    }

