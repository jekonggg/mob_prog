package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditRecordActivity extends AppCompatActivity {

    private EditText etPrice, etDate, etTime, etNotes;
    private Button btnEditRecord, btnCancel;
    private DatabaseHelper databaseHelper;

    private int recordId; // Record ID to be updated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        // Initialize the UI elements
        etPrice = findViewById(R.id.etPrice);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etNotes = findViewById(R.id.etNotes);
        btnEditRecord = findViewById(R.id.btnAddRecord);
        btnCancel = findViewById(R.id.btnCancel);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Change button text to "Update"
        btnEditRecord.setText("Update");

        // Load existing record data passed from MainActivity
        Intent intent = getIntent();
        String price = intent.getStringExtra("price");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String notes = intent.getStringExtra("notes");
        recordId = intent.getIntExtra("id", -1); // Get the record ID to update

        // Populate the fields with existing data
        etPrice.setText(price);
        etDate.setText(date);
        etTime.setText(time);
        etNotes.setText(notes);

        btnEditRecord.setOnClickListener(v -> {
            // Retrieve updated values from input fields
            String updatedPrice = etPrice.getText().toString();
            String updatedDate = etDate.getText().toString();
            String updatedTime = etTime.getText().toString();
            String updatedNotes = etNotes.getText().toString();

            // For Spinner:
            Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
            String updatedCategory = spinnerCategory.getSelectedItem().toString();  // Get selected category

            // Or, if using EditText for category:
            // String updatedCategory = etCategory.getText().toString();

            // Check if all fields are filled before proceeding
            if (updatedPrice.isEmpty() || updatedDate.isEmpty() || updatedTime.isEmpty() || updatedNotes.isEmpty() || updatedCategory.isEmpty()) {
                Toast.makeText(EditRecordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Call database helper to update the record
                boolean isUpdated = databaseHelper.updateRecord(recordId, updatedPrice, updatedDate, updatedTime, updatedNotes, updatedCategory);  // Use updatedCategory

                if (isUpdated) {
                    // Notify the user of success
                    Toast.makeText(EditRecordActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();

                    // Return the updated data to MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedPrice", updatedPrice);
                    resultIntent.putExtra("updatedDate", updatedDate);
                    resultIntent.putExtra("updatedTime", updatedTime);
                    resultIntent.putExtra("updatedNotes", updatedNotes);
                    resultIntent.putExtra("updatedCategory", updatedCategory);  // Include category
                    setResult(RESULT_OK, resultIntent);

                    // Finish EditRecordActivity and return to MainActivity
                    finish();
                } else {
                    // Show an error message if the update fails
                    Toast.makeText(EditRecordActivity.this, "Failed to update record", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Handle "Cancel" button click
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);  // Ensure result is canceled
            finish();
        });
    }
}
