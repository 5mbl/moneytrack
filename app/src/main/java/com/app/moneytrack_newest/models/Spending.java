package com.app.moneytrack_newest.models;

public class Spending {
    private String id;
    private String amount;
    private String description;
    private String userEmail; // Add this field

    public Spending() {
        // Default constructor required for calls to DataSnapshot.getValue(Spending.class)
    }

    public Spending(String id, String amount, String description, String userEmail) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.userEmail = userEmail;
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}