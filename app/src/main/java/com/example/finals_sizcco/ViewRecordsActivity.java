package com.example.finals_sizcco;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewRecordsActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<Record> recordList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        // Initialize views
        btnBack = findViewById(R.id.btnClose);  // Back button
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        recordList = new ArrayList<>();

        loadRecords();

        // Set up the Back button functionality
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent backIntent = new Intent(ViewRecordsActivity.this, MainActivity.class);
                startActivity(backIntent);
                finish();  // Optionally finish the current activity to remove it from the back stack
            }
        });
    }

    private void loadRecords() {
        Cursor cursor = databaseHelper.getAllRecords("JohnDoe");  // Replace with actual username

        if (cursor != null && cursor.moveToFirst()) {
            recordList.clear();  // Clear previous records if any

            do {
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
                int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);
                int notesIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTES);
                int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);

                if (priceIndex == -1 || dateIndex == -1 || timeIndex == -1 || notesIndex == -1 || categoryIndex == -1) {
                    Toast.makeText(this, "Error: Column not found!", Toast.LENGTH_SHORT).show();
                    break; // Exit loop or continue based on handling
                }

                String price = cursor.getString(priceIndex);
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                String notes = cursor.getString(notesIndex);
                String category = cursor.getString(categoryIndex);

                recordList.add(new Record(price, date, time, notes, category));
            } while (cursor.moveToNext());

            cursor.close();

            if (adapter == null) {
                adapter = new RecordAdapter(recordList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }


}
