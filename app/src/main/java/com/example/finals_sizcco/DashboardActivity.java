package com.example.finals_sizcco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private TextView dateToday, totalLimitValue;
    private ListView expensesListView;
    private FloatingActionButton addExpenseButton;
    private View transactionsButton, homeButton;

    private TextView foodTotal, travelTotal, utilitiesTotal, onlineShoppingTotal, leisureTotal, othersTotal;
    private String username; // To store the username passed from LoginActivity

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Retrieve the username passed from LoginActivity
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");  // Get the username from Intent

        // Initialize UI elements
        addExpenseButton = findViewById(R.id.btnAddRecord_button);
        dateToday = findViewById(R.id.date_today);
        totalLimitValue = findViewById(R.id.total_limit_value);
        expensesListView = findViewById(R.id.expenses_list_view);
        transactionsButton = findViewById(R.id.transactions_button);
        homeButton = findViewById(R.id.home_button);

        foodTotal = findViewById(R.id.food_total);
        travelTotal = findViewById(R.id.travel_total);
        utilitiesTotal = findViewById(R.id.utilities_total);
        onlineShoppingTotal = findViewById(R.id.online_shopping_total);
        leisureTotal = findViewById(R.id.leisure_total);
        othersTotal = findViewById(R.id.others_total);

        // Set the current date
        setCurrentDate();

        // Initialize database helper and database instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // Set button click listeners
        addExpenseButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(DashboardActivity.this, AddExpenseActivity.class);
            intent1.putExtra("USERNAME", username);  // Pass the username to AddRecordActivity
            startActivity(intent1);
        });

        homeButton.setOnClickListener(view -> {
            // Add home button functionality here if needed
        });

        transactionsButton.setOnClickListener(view -> {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update expenses list with the current user
        updateExpensesList(username);
    }

    private void setCurrentDate() {
        String currentDate = new java.text.SimpleDateFormat("MMMM dd, yyyy").format(new Date());
        dateToday.setText(currentDate);
    }

    // Format currency to Philippine Peso (₱)
    private String formatCurrency(double amount) {
        // Set currency format for Philippine Peso
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setCurrency(java.util.Currency.getInstance("PHP"));  // Set to PHP (Philippine Peso)
        return currencyFormat.format(amount);  // Format amount as currency (e.g., ₱0.00)
    }


    // Update the expenses list view based on the current user's category expenses
    private void updateExpensesList(String username) {
        // Query the database for the user's expenses per category
        HashMap<String, Double> categoryExpenses = getCategoryExpensesFromDatabase(username);

        // Create a list of formatted strings to display in the ListView
        ArrayList<String> expenseDetails = new ArrayList<>();
        double totalExpenses = 0.0; // To store the total of all expenses

        for (String category : categoryExpenses.keySet()) {
            double categoryTotal = categoryExpenses.get(category);
            expenseDetails.add(category + ": " + formatCurrency(categoryTotal));
            totalExpenses += categoryTotal; // Add the category total to the overall total
        }

        // Use ArrayAdapter to bind the data to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseDetails);
        expensesListView.setAdapter(adapter);

        // Update total per category
        updateCategoryTotals(categoryExpenses);

        // Update the totalLimitValue to show the total of all expenses
        totalLimitValue.setText(formatCurrency(totalExpenses)); // Set the total value in totalLimitValue
    }


    // Fetch category expenses from the database for the user
    private HashMap<String, Double> getCategoryExpensesFromDatabase(String username) {
        HashMap<String, Double> categoryExpenses = new HashMap<>();

        String[] categories = {"Food", "Transport", "Utilities", "Online Shopping", "Leisure", "Others"};

        for (String category : categories) {
            double totalAmount = 0.0;
            String query = "SELECT SUM(price) FROM records WHERE username = ? AND category = ?";
            Cursor cursor = db.rawQuery(query, new String[]{username, category});

            if (cursor.moveToFirst()) {
                totalAmount = cursor.getDouble(0);
            }
            cursor.close();

            categoryExpenses.put(category, totalAmount);
        }

        return categoryExpenses;
    }

    // Update the totals for each category
    private void updateCategoryTotals(HashMap<String, Double> categoryExpenses) {
        // Update total for each category, formatted as currency
        foodTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Food", 0.0)));
        travelTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Transport", 0.0)));
        utilitiesTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Utilities", 0.0)));
        onlineShoppingTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Online Shopping", 0.0)));
        leisureTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Leisure", 0.0)));
        othersTotal.setText(formatCurrency(categoryExpenses.getOrDefault("Others", 0.0)));
    }
}
