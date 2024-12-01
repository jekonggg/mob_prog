package com.example.finals_sizcco;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AllowanceActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText amountInput;
    private Button submitButton;
    private DatabaseHelper dbHelper; // Database helper class to handle SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allowance_activity_layout);

        dbHelper = new DatabaseHelper(this);  // Initialize the database helper

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailInput = findViewById(R.id.emailInput);
        amountInput = findViewById(R.id.amountInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });

    }

    private void handleFormSubmission() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String amount = amountInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isUsernameTaken(username)) {
            Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
            return;
        }

        saveDataToDatabase(username, password, email, amount);

        // Get the current date
        String currentDate = Calendar.getInstance().getTime().toString(); // This returns the full date as a string
        String confirmationMessage = String.format("Allowance of %s is set starting on %s", amount, currentDate);

        Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show();

        // Clear fields after submission
        clearFields();

        // Navigate to DashboardActivity
        Intent intent = new Intent(AllowanceActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Close AllowanceActivity to prevent going back to it after the user presses the back button
    }


    private boolean isUsernameTaken(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"username"}, "username=?", new String[]{username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private void saveDataToDatabase(String username, String password, String email, String amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        values.put("allowance_amount", amount);
        db.insert("users", null, values);
        db.close();
    }

    private void clearFields() {
        usernameInput.setText("");
        passwordInput.setText("");
        emailInput.setText("");
        amountInput.setText("");
        Calendar today = Calendar.getInstance();
    }
}
