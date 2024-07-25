package com.app.moneytrack_newest.models;

public class Transaction {
    private String id;
    private String amount;
    private String description;
    private String type; // "Income" or "Spending"
    private String category; // Hinzugefügte Kategorie
    private String userEmail;

    public Transaction() {
        // Default constructor required for calls to DataSnapshot.getValue(Transaction.class)
    }

    public Transaction(String id, String amount, String description, String type, String category, String userEmail) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.category = category; // Initialisierung der Kategorie
        this.userEmail = userEmail;
    }

    // Getter und Setter für alle Felder
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
