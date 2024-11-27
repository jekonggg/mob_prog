package com.example.finals_sizcco;

public class Record {
    private String price;
    private String date;
    private String time;
    private String notes;
    private String category;

    // Constructor
    public Record(String price, String date, String time, String notes, String category) {
        this.price = price;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.category = category;
    }

    // Getter methods
    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }

    public String getCategory() {
        return category;
    }
}
