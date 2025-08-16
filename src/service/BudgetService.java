package service;

import model.Budget;
import model.Expense;
import model.Category;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetService {
    private Budget budget;

    public BudgetService(double income, double limit) {
        this.budget = new Budget(income, limit);
    }

    public void addExpense(double amount, String description, Category category, LocalDate date) {
        Expense expense = new Expense(amount, description, category, date);
        budget.addExpense(expense);
    }

    public void removeExpense(int index) {
        if (index >= 0 && index < budget.getExpenses().size()) {
            budget.getExpenses().remove(index);
        }
    }

    public void editExpense(int index, double amount, String description, Category category, LocalDate date) {
        if (index >= 0 && index < budget.getExpenses().size()) {
            Expense expense = budget.getExpenses().get(index);
            expense.setAmount(amount);
            expense.setDescription(description);
            expense.setCategory(category);
            expense.setDate(date);
        }
    }

    public List<Expense> getExpenses() {
        return budget.getExpenses();
    }

    public List<Expense> getExpensesByCategory(Category category) {
        return budget.getExpenses().stream()
                .filter(e -> e.getCategory() == category)
                .collect(Collectors.toList());
    }

    public double getTotalExpenses() {
        return budget.getTotalExpenses();
    }

    public double getRemaining() {
        return budget.getRemaining();
    }

    public boolean isLimitReached() {
        return budget.isLimitReached();
    }

    public double getIncome() {
        return budget.getIncome();
    }

    public void setIncome(double income) {
        budget.setIncome(income);
    }

    public double getLimit() {
        return budget.getLimit();
    }

    public void setLimit(double limit) {
        budget.setLimit(limit);
    }
}
