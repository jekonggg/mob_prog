package com.example.finals_sizcco;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Declare variables for UI elements
    private TextView tvPriceLabel, tvCategoryLabel, tvDateLabel, tvTimeLabel, tvNotesLabel, etDate, etTime;
    private EditText etPrice, etNotes;
    private Spinner spinnerCategory;
    private ImageView categoryIcon;
    private Button btnAddRecord, btnCancel;

    // Declare variables for dynamic values
    private String[] categories = {"Food", "Transport", "Entertainment", "Utilities", "Online Shopping", "Others"};
    private String selectedCategory = null;
    private String price = "";
    private String date = "";
    private String time = "";
    private String notes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        tvPriceLabel = findViewById(R.id.tvPriceLabel);
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
        tvDateLabel = findViewById(R.id.tvDateLabel);
        tvTimeLabel = findViewById(R.id.tvTimeLabel);
        tvNotesLabel = findViewById(R.id.tvNotesLabel);
        etPrice = findViewById(R.id.etPrice);
        etNotes = findViewById(R.id.etNotes);
        etDate = findViewById(R.id.etDate); // Use TextView or EditText for Date
        etTime = findViewById(R.id.etTime); // Use TextView or EditText for Time
        spinnerCategory = findViewById(R.id.spinnerCategory);
        categoryIcon = findViewById(R.id.categoryIcon);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        btnCancel = findViewById(R.id.btnCancel);

        // Set dynamic values
        tvPriceLabel.setText(getString(R.string.label_price));
        tvCategoryLabel.setText(getString(R.string.label_category));
        tvDateLabel.setText(getString(R.string.label_date));
        tvTimeLabel.setText(getString(R.string.label_time));
        tvNotesLabel.setText(getString(R.string.label_notes));

        // Set Spinner (Category options)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // Add click listener to the "Add Record" button
        btnAddRecord.setOnClickListener(v -> {
            price = etPrice.getText().toString();
            date = etDate.getText().toString();
            time = etTime.getText().toString();
            notes = etNotes.getText().toString();

            // Check if all fields are filled
            if (price.isEmpty() || date.isEmpty() || time.isEmpty() || notes.isEmpty() || selectedCategory == null) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the record addition
                Toast.makeText(MainActivity.this, "Record Added: " + price + " - " + selectedCategory, Toast.LENGTH_SHORT).show();
            }
        });

        // Add click listener to the "Cancel" button
        btnCancel.setOnClickListener(v -> finish());  // Close the activity

        // Set the selected category when an item is chosen from the Spinner
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = categories[position];
                // Update the category icon based on the selected category
                updateCategoryIcon(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where no category is selected
                selectedCategory = null;
            }
        });

        // Set click listener for the Date field
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Set click listener for the Time field
        etTime.setOnClickListener(v -> showTimePickerDialog());
    }

    // Method to update the category icon based on the selected category
    private void updateCategoryIcon(String category) {
        switch (category) {
            case "Food":
                categoryIcon.setImageResource(R.drawable.food_icon); // Set the appropriate drawable for Food
                break;
            case "Transport":
                categoryIcon.setImageResource(R.drawable.transportation_icon); // Set the appropriate drawable for Transport
                break;
            case "Entertainment":
                categoryIcon.setImageResource(R.drawable.entertainment_icon); // Set the appropriate drawable for Entertainment
                break;
            case "Utilities":
                categoryIcon.setImageResource(R.drawable.utilities_icon); // Set the appropriate drawable for Utilities
                break;
            case "Online Shopping":
                categoryIcon.setImageResource(R.drawable.shopping_icon); // Set the appropriate drawable for Shopping
                break;
            case "Others":
                categoryIcon.setImageResource(R.drawable.others_icon); // Set the appropriate drawable for Others
                break;
            default:
                categoryIcon.setImageResource(R.drawable.category); // Set a default icon
                break;
        }
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            // Set the date to the EditText
            etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
        }, year, month, day);
        datePickerDialog.show();
    }

    // Method to show TimePickerDialog
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Set the time to the EditText
            etTime.setText(hourOfDay + ":" + String.format("%02d", minute1));
        }, hour, minute, true);
        timePickerDialog.show();
    }
}
