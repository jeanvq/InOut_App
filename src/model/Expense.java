package model;

import java.time.LocalDate;

public class Expense {
    private double amount;
    private String description;
    private Category category;
    private LocalDate date;

    public Expense(double amount, String description, Category category, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
}
