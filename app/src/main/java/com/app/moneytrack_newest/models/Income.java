package com.app.moneytrack_newest.models;

public class Income {
    private String id;
    private String amount;
    private String description;
    private String userEmail;
    private String category; // Neues Feld für die Kategorie

    public Income() {
        // Default constructor required for calls to DataSnapshot.getValue(Income.class)
    }

    public Income(String id, String amount, String description, String userEmail, String category) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.userEmail = userEmail;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
