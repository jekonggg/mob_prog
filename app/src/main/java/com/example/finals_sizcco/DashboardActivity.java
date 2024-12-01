package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView dateToday;
    private TextView totalLimitValue;
    private ListView expensesListView;
    private FloatingActionButton addExpenseButton;
    private View transactionsButton;
    private View homeButton;

    private HashMap<String, Double> categoryExpenses;
    private DatabaseHelper dbHelper;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get the logged-in user's username (this should be passed through Intent)
        currentUsername = getIntent().getStringExtra("USERNAME");

        // Initialize UI elements
        dateToday = findViewById(R.id.date_today);
        totalLimitValue = findViewById(R.id.total_limit_value);
        expensesListView = findViewById(R.id.expenses_list_view);
        transactionsButton = findViewById(R.id.transactions_button);
        homeButton = findViewById(R.id.home_button);
        addExpenseButton = findViewById(R.id.add_expense_button); // FloatingActionButton

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

        // Fetch the user's allowance and display it as total limit
        updateTotalLimit();

        // Set Transactions Button Click Listener
        transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Transactions Activity
                Intent intent = new Intent(DashboardActivity.this, ViewRecordsActivity.class);
                startActivity(intent);
            }
        });

        // Handle Floating Action Button click to navigate to AddRecords Activity
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to AddRecords Activity
                Intent intent = new Intent(DashboardActivity.this, AddRecordActivity.class);
                intent.putExtra("USERNAME", currentUsername); // Pass the current user's username to AddRecords
                startActivity(intent);
            }
        });
    }

    // Method to set the current date in the TextView
    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String currentDate = sdf.format(new Date());
        dateToday.setText(currentDate);
    }

    // Fetch and display the user's allowance amount (total limit)
    private void updateTotalLimit() {
        String allowanceAmount = dbHelper.getAllowanceAmount(currentUsername);
        if (allowanceAmount != null) {
            totalLimitValue.setText("$" + allowanceAmount);
        } else {
            totalLimitValue.setText("$0.00");
        }
    }

    private void updateExpensesList() {
        ArrayList<String> expenseDetails = new ArrayList<>();
        for (String category : categoryExpenses.keySet()) {
            expenseDetails.add(category + ": $" + categoryExpenses.get(category));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseDetails);
        expensesListView.setAdapter(adapter);
    }

    // Method to add an expense for a given category (local to the dashboard activity)
    public void addExpense(String category, double amount) {
        double currentAmount = categoryExpenses.getOrDefault(category, 0.0);
        categoryExpenses.put(category, currentAmount + amount);
        updateExpensesList();
    }
}
