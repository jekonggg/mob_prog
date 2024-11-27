package com.example.finals_sizcco;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
    private Spinner frequencySpinner;
    private DatePicker datePicker;
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
        frequencySpinner = findViewById(R.id.frequencySpinner);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });

        setupFrequencySpinner();
    }

    private void setupFrequencySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);
    }

    private void handleFormSubmission() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String amount = amountInput.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isUsernameTaken(username)) {
            Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
            return;
        }

        saveDataToDatabase(username, password, email, amount, frequency);

        String confirmationMessage = String.format("Allowance of %s is set %s starting on %tF", amount, frequency);
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

    private void saveDataToDatabase(String username, String password, String email, String amount, String frequency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        values.put("allowance_amount", amount);
        values.put("frequency", frequency);

        db.insert("users", null, values);
        db.close();
    }

    private void clearFields() {
        usernameInput.setText("");
        passwordInput.setText("");
        emailInput.setText("");
        amountInput.setText("");
        frequencySpinner.setSelection(0);
        Calendar today = Calendar.getInstance();
        datePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }
}
