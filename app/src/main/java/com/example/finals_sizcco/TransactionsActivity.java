package com.example.finals_sizcco;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private ListView lvExpenses;
    private TextView tvNoExpenses;
    private DatabaseHelper dbHelper;
    private String username;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        lvExpenses = findViewById(R.id.lvExpenses);
        tvNoExpenses = findViewById(R.id.tvNoExpenses);
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnDeleteExpenses = findViewById(R.id.btnDeleteExpenses);

        dbHelper = new DatabaseHelper(this);

        // Retrieve the username from the Intent
        username = getIntent().getStringExtra("USERNAME");

        if (username == null || username.isEmpty()) {
            Log.e("TransactionsActivity", "Username is missing");
            return;
        }

        // Fetch expenses from the database
        List<Expense> expenses = getRecentExpenses(username);

        // Show a message if no expenses are found
        if (expenses.isEmpty()) {
            tvNoExpenses.setVisibility(View.VISIBLE);
        } else {
            tvNoExpenses.setVisibility(View.GONE);
        }

        // Set up the adapter
        adapter = new ExpenseAdapter(this, expenses);
        lvExpenses.setAdapter(adapter);

        // Handle the Edit Expenses button click
        findViewById(R.id.btnEditExpenses).setOnClickListener(v -> {
            Toast.makeText(this, "Edit Expenses clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle item clicks to select or edit an expense
        lvExpenses.setOnItemClickListener((parent, view, position, id) -> {
            Expense selectedExpense = expenses.get(position);
            Toast.makeText(this, "Selected: " + selectedExpense.getCategory(), Toast.LENGTH_SHORT).show();
        });

        // Handle the Select button click to toggle selection mode
        btnSelect.setOnClickListener(v -> {
            adapter.setSelectionMode(true); // Enable selection mode
            adapter.notifyDataSetChanged();
        });

        // Handle the Delete button click to delete selected expenses
        btnDeleteExpenses.setOnClickListener(v -> {
            List<Expense> selectedExpenses = adapter.getSelectedExpenses();

            if (selectedExpenses.isEmpty()) {
                Toast.makeText(this, "No expenses selected", Toast.LENGTH_SHORT).show();
            } else {
                showDeleteConfirmationDialog(selectedExpenses);
            }
        });
    }

    // Fetch recent expenses from the database for the given username
    private List<Expense> getRecentExpenses(String username) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_RECORDS,
                new String[]{DatabaseHelper.COLUMN_CATEGORY, DatabaseHelper.COLUMN_PRICE},
                DatabaseHelper.COLUMN_USERNAME + " = ?",
                new String[]{username},
                null, null, null);

        if (cursor != null) {
            int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
            int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);

            if (categoryIndex != -1 && priceIndex != -1) {
                while (cursor.moveToNext()) {
                    String category = cursor.getString(categoryIndex);
                    double price = cursor.getDouble(priceIndex);
                    expenses.add(new Expense(category, price));
                }
            } else {
                Log.e("Database", "Column not found");
            }
            cursor.close();
        }

        return expenses;
    }

    // Show confirmation dialog to delete selected expenses
    private void showDeleteConfirmationDialog(List<Expense> selectedExpenses) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expenses")
                .setMessage("Are you sure you want to delete the selected expenses?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteSelectedExpenses(selectedExpenses);
                    adapter.notifyDataSetChanged(); // Refresh the list
                    Toast.makeText(this, "Selected expenses deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Delete selected expenses from the database
    private void deleteSelectedExpenses(List<Expense> selectedExpenses) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Expense expense : selectedExpenses) {
            db.delete(DatabaseHelper.TABLE_RECORDS,
                    DatabaseHelper.COLUMN_CATEGORY + " = ? AND " +
                            DatabaseHelper.COLUMN_PRICE + " = ? AND " +
                            DatabaseHelper.COLUMN_USERNAME + " = ?",
                    new String[]{expense.getCategory(), String.valueOf(expense.getPrice()), username});
        }
    }
}
