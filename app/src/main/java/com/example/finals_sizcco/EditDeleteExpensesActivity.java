package com.example.finals_sizcco;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditDeleteExpensesActivity extends AppCompatActivity {

    private ListView lvEditExpenses;
    private Button btnDeleteSelected;
    private ArrayList<String> expensesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_expenses);

        lvEditExpenses = findViewById(R.id.lvEditExpenses);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);

        // Get data from intent
        expensesList = getIntent().getStringArrayListExtra("expenses");

        // Setup ListView with multiple choice mode
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, expensesList);
        lvEditExpenses.setAdapter(adapter);
        lvEditExpenses.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Delete selected items
        btnDeleteSelected.setOnClickListener(view -> {
            ArrayList<String> selectedItems = new ArrayList<>();
            for (int i = 0; i < lvEditExpenses.getCount(); i++) {
                if (lvEditExpenses.isItemChecked(i)) {
                    selectedItems.add(expensesList.get(i));
                }
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "No items selected!", Toast.LENGTH_SHORT).show();
            } else {
                expensesList.removeAll(selectedItems);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Deleted selected items!", Toast.LENGTH_SHORT).show();

                // Clear selections
                lvEditExpenses.clearChoices();
            }
        });
    }
}