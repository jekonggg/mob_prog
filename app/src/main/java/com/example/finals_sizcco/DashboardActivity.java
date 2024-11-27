package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private TextView dateToday;
    private TextView totalLimitValue;
    private ListView expensesListView;
    private FloatingActionButton addExpenseButton;
    private View transactionsButton; // The Transactions button
    private View homeButton;         // The Home button

    private HashMap<String, Double> categoryExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize UI elements
        dateToday = findViewById(R.id.date_today);
        totalLimitValue = findViewById(R.id.total_limit_value);
        expensesListView = findViewById(R.id.expenses_list_view); // Initialize the ListView
        transactionsButton = findViewById(R.id.transactions_button);
        homeButton = findViewById(R.id.home_button);

        // Initialize expenses by category
        categoryExpenses = new HashMap<>();
        categoryExpenses.put("Food", 0.0);
        categoryExpenses.put("Transportation", 0.0);
        categoryExpenses.put("Bills", 0.0);
        categoryExpenses.put("Online Shopping", 0.0);
        categoryExpenses.put("Miscellaneous", 0.0);
        categoryExpenses.put("Others", 0.0);

        // Set the current date in totalLimitValue TextView
        setCurrentDate();

        // Populate list view with dummy data
        updateExpensesList();

        // Set Transactions Button Click Listener
        transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Transactions Activity
                Intent intent = new Intent(DashboardActivity.this, TransactionsActivity.class);
                startActivity(intent);
            }
        });

        // Optionally handle Home Button (if it needs functionality)
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Home button functionality here if needed, or leave as no-op
            }
        });
    }

    // Method to set the current date in the TextView
    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy"); // Format as "Month day, year"
        String currentDate = sdf.format(new Date()); // Get the current date as a string

        // Set the formatted date as the text of the TextView
        dateToday.setText(currentDate);  // This sets the current date in the TextView
    }


    private void updateExpensesList() {
        // Create a list of category and expenses
        ArrayList<String> expenseDetails = new ArrayList<>();
        for (String category : categoryExpenses.keySet()) {
            expenseDetails.add(category + ": $" + categoryExpenses.get(category));
        }

        // Use ArrayAdapter to bind the data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseDetails);
        expensesListView.setAdapter(adapter);
    }

    public void updateTotalLimit(double limit) {
        totalLimitValue.setText("$" + limit);
    }

    public void addExpense(String category, double amount) {
        double currentAmount = categoryExpenses.getOrDefault(category, 0.0);
        categoryExpenses.put(category, currentAmount + amount);
        updateExpensesList();
    }
}