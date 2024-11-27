package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAddRecord;
    private Button btnEditRecord;
    private Button btnViewRecords;  // Add new button for viewing records
    private static final int EDIT_RECORD_REQUEST_CODE = 1001;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnAddRecord = findViewById(R.id.btnAddRecord);
        btnEditRecord = findViewById(R.id.btnEditRecord);
        btnViewRecords = findViewById(R.id.btnViewRecords);  // Initialize View Records button
        databaseHelper = new DatabaseHelper(this);

        // Navigate to AddRecordActivity when "Add Record" button is clicked
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivity(addIntent);
            }
        });

        // Navigate to EditRecordActivity when "Edit Record" button is clicked
        btnEditRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass sample data to EditRecordActivity, including category
                Intent editIntent = new Intent(MainActivity.this, EditRecordActivity.class);
                editIntent.putExtra("price", "500.00");  // Example price
                editIntent.putExtra("date", "2024-11-27");  // Example date
                editIntent.putExtra("time", "14:30");  // Example time
                editIntent.putExtra("notes", "This is a sample note.");  // Example notes
                editIntent.putExtra("category", "Food");  // Example category
                startActivity(editIntent);
            }
        });

        // Navigate to ViewRecordsActivity when "View Records" button is clicked
        btnViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(MainActivity.this, ViewRecordsActivity.class);
                startActivity(viewIntent);
            }
        });
    }

    private void openEditRecordActivity(String price, String date, String time, String notes) {
        Intent intent = new Intent(MainActivity.this, EditRecordActivity.class);
        intent.putExtra("price", price);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        intent.putExtra("notes", notes);
        startActivityForResult(intent, EDIT_RECORD_REQUEST_CODE);  // Expect a result back
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve updated data from EditRecordActivity
            int id = data.getIntExtra("id", -1);
            String updatedPrice = data.getStringExtra("updatedPrice");
            String updatedDate = data.getStringExtra("updatedDate");
            String updatedTime = data.getStringExtra("updatedTime");
            String updatedNotes = data.getStringExtra("updatedNotes");
            String category = data.getStringExtra("category");  // Get category data

            // Update the record in the database
            boolean success = databaseHelper.updateRecord(id, updatedPrice, updatedDate, updatedTime, updatedNotes, category);

            if (success) {
                // Show success message or update the UI accordingly
                Toast.makeText(MainActivity.this, "Record updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Handle failure
                Toast.makeText(MainActivity.this, "Failed to update record.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
