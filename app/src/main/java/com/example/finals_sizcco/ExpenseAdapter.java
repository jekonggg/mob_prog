package com.example.finals_sizcco;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private List<Expense> expenses;
    private Context context;
    private boolean isSelectionMode = false; // Flag to check if selection mode is enabled
    private SparseBooleanArray selectedItems = new SparseBooleanArray(); // Stores selection state of each item

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
        this.context = context;
        this.expenses = expenses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        }

        Expense expense = expenses.get(position);

        TextView categoryTextView = convertView.findViewById(R.id.tvCategory);
        TextView priceTextView = convertView.findViewById(R.id.tvPrice);
        CheckBox checkBox = convertView.findViewById(R.id.cbSelect);

        categoryTextView.setText(expense.getCategory());
        priceTextView.setText(String.valueOf(expense.getPrice()));

        // If selection mode is enabled, show checkboxes
        checkBox.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);

        // Set the checkbox state based on the selectedItems array
        checkBox.setChecked(selectedItems.get(position, false));

        // Handle checkbox clicks
        checkBox.setOnClickListener(v -> {
            boolean checked = checkBox.isChecked();
            selectedItems.put(position, checked);
        });

        return convertView;
    }

    // Enable or disable selection mode
    public void setSelectionMode(boolean enabled) {
        isSelectionMode = enabled;
        selectedItems.clear(); // Clear any previously selected items
        notifyDataSetChanged();
    }

    // Get selected expenses based on checked items
    public List<Expense> getSelectedExpenses() {
        List<Expense> selectedExpenses = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            if (selectedItems.get(i)) {
                selectedExpenses.add(expenses.get(i));
            }
        }
        return selectedExpenses;
    }
}

