package com.example.finals_sizcco;

public class Expense {
    private String category;
    private double price;

    public Expense(String category, double price) {
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}
