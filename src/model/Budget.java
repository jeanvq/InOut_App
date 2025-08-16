package model;

import java.util.ArrayList;
import java.util.List;

public class Budget {
    private double income;
    private double limit;
    private List<Expense> expenses;

    public Budget(double income, double limit) {
        this.income = income;
        this.limit = limit;
        this.expenses = new ArrayList<>();
    }

    public double getIncome() { return income; }
    public double getLimit() { return limit; }
    public List<Expense> getExpenses() { return expenses; }

    public void setIncome(double income) { this.income = income; }
    public void setLimit(double limit) { this.limit = limit; }

    public void addExpense(Expense expense) { expenses.add(expense); }
    public void removeExpense(Expense expense) { expenses.remove(expense); }

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public double getRemaining() {
        return income - getTotalExpenses();
    }

    public boolean isLimitReached() {
        return getTotalExpenses() >= limit;
    }
}
