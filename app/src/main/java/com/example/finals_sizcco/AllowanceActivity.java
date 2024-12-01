package com.example.finals_sizcco;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AllowanceActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText amountInput;
    private Button submitButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allowance_activity_layout);

        dbHelper = new DatabaseHelper(this);

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

        String confirmationMessage = String.format("Allowance of %s has been set", amount);
        Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show();

        clearFields();

        // Navigate to LoginActivity instead of DashboardActivity
        Intent intent = new Intent(AllowanceActivity.this, LoginActivity.class);
        startActivity(intent);

        // Finish the current activity so the user cannot go back to AllowanceActivity
        finish();
    }


    private boolean isUsernameTaken(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users_data", new String[]{"username"}, "username=?", new String[]{username}, null, null, null);
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

        db.insert("users_data", null, values);
        db.close();
    }

    private void clearFields() {
        usernameInput.setText("");
        passwordInput.setText("");
        emailInput.setText("");
        amountInput.setText("");
    }
}
