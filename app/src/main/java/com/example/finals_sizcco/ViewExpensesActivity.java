package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewExpensesActivity extends AppCompatActivity {

    private ListView lvExpenses;
    private TextView tvNoExpenses;
    private Button btnEditExpenses;
    private ArrayList<String> expensesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        lvExpenses = findViewById(R.id.lvExpenses);
        tvNoExpenses = findViewById(R.id.tvNoExpenses);
        btnEditExpenses = findViewById(R.id.btnEditExpenses);

        // Dummy data for testing
        expensesList = new ArrayList<>();
        expensesList.add("Food - $10 - 2024-11-25");
        expensesList.add("Transport - $5 - 2024-11-26");
        expensesList.add("Entertainment - $15 - 2024-11-27");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        lvExpenses.setAdapter(adapter);

        // Show message if no expenses
        if (expensesList.isEmpty()) {
            tvNoExpenses.setVisibility(View.VISIBLE);
            lvExpenses.setVisibility(View.GONE);
        } else {
            tvNoExpenses.setVisibility(View.GONE);
            lvExpenses.setVisibility(View.VISIBLE);
        }

        // Navigate to Edit/Delete Activity
        btnEditExpenses.setOnClickListener(view -> {
            Intent intent = new Intent(ViewExpensesActivity.this, EditDeleteExpensesActivity.class);
            intent.putStringArrayListExtra("expenses", expensesList);
            startActivity(intent);
        });
    }
}