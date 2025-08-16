package cli;

import model.Category;
import model.Expense;
import service.BudgetService;
import util.ConsoleColors;
import util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InOutApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static BudgetService budgetService;

    public static void main(String[] args) {
        printBanner();
        printHelp();
        setupBudget();
        runMenu();
    }
    private static void printBanner() {
        String banner = ConsoleColors.CYAN +
                "\n" +
                "██╗███╗   ██╗ ██████╗ ██    ██ ████████╗\n" +
                "██║████╗  ██║██╔═══██╗██╔═══██╗   ██║   \n" +
                "██║██╔██╗ ██║██║   ██║██║   ██║   ██║   \n" +
                "██║██║╚██╗██║██║   ██║██║   ██║   ██╔   \n" +
                "██║██║ ╚████║╚██████╔╝╚██████╔╝   ██╔╝  \n" +
                "╚═╝╚═╝  ╚═══╝ ╚═════╝  ╚═════╝   ╚═══╝  \n" +
                ConsoleColors.RESET;
        System.out.println(banner);
        System.out.println(ConsoleColors.GREEN + "Welcome to InOut! 💸💰\nTrack your expenses and income!\n" + ConsoleColors.RESET);
    }

    private static void printHelp() {
        System.out.println(ConsoleColors.PURPLE + "InOut — Friendly Expense & Income Tracker (colors, emojis, monthly budget, summary & HTML report)\n" + ConsoleColors.RESET);
        System.out.println(" Commands:");
        System.out.println("  add   = a   Add an expense 💰");
        System.out.println("  list  = l   List expenses 📋");
        System.out.println("  summary = s   Monthly summary 📊");
        System.out.println("  report  = r   HTML report 🗒️");
        System.out.println("  help    = h   Show help 🆘");
        System.out.println("  exit    = 0   Exit\n 👋");
       
    }

    private static void setupBudget() {
        double income = 0;
        double limit = 0;
        while (true) {
            System.out.print("Enter your monthly income: $");
            String input = scanner.nextLine();
            if (!input.matches("^\\d+(\\.\\d{1,2})?$")) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for income (no letters, no scientific notation)." + ConsoleColors.RESET);
                continue;
            }
            try {
                income = Double.parseDouble(input);
                if (income < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for income." + ConsoleColors.RESET);
            }
        }
        while (true) {
            System.out.print("Set your monthly budget limit: $");
            String input = scanner.nextLine();
            if (!input.matches("^\\d+(\\.\\d{1,2})?$")) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for budget limit (no letters, no scientific notation)." + ConsoleColors.RESET);
                continue;
            }
            try {
                limit = Double.parseDouble(input);
                if (limit < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for budget limit." + ConsoleColors.RESET);
            }
        }
        budgetService = new BudgetService(income, limit);
    }

    private static void runMenu() {
        while (true) {
            if (budgetService.isLimitReached()) {
                System.out.println(ConsoleColors.YELLOW + "⚠️  Budget limit reached!" + ConsoleColors.RESET);
            }
            System.out.print("\nType a command (type 'help' for options): ");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "1": case "add": case "a": addExpense(); break;
                case "2": case "list": case "l": viewExpenses(); break;
                case "3": case "edit": case "e": editExpense(); break;
                case "4": case "delete": case "d": deleteExpense(); break;
                case "5": case "report": case "r": generateHtmlReport(); break;
                case "6": case "summary": case "s": showSummary(); break;
                case "help": case "h": printHelp(); break;
                case "0": case "exit": System.out.println("👋Goodbye!👋"); return;
                default: System.out.println("Invalid command. Type 'help' to see available commands.");
            }
        }
    }

    private static void addExpense() {
        double amount = 0;
        while (true) {
            System.out.print("Amount: $");
            String input = scanner.nextLine();
            try {
                amount = Double.parseDouble(input);
                if (amount < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for amount." + ConsoleColors.RESET);
            }
        }
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.println("Category:");
        for (Category c : Category.values()) {
            System.out.println(c.ordinal() + 1 + ". " + c.emoji + " " + c.name());
        }
        int catIdx = -1;
        while (true) {
            String input = scanner.nextLine();
            try {
                catIdx = Integer.parseInt(input) - 1;
                if (catIdx < 0 || catIdx >= Category.values().length) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid category number." + ConsoleColors.RESET);
            }
        }
        Category category = Category.values()[catIdx];
        LocalDate date = null;
        while (true) {
            System.out.print("Date (yyyy-MM-dd): ");
            String input = scanner.nextLine();
            try {
                date = DateUtil.parse(input);
                break;
            } catch (Exception e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid date in yyyy-MM-dd format." + ConsoleColors.RESET);
            }
        }
        budgetService.addExpense(amount, description, category, date);
        System.out.println(ConsoleColors.GREEN + "Expense added!" + ConsoleColors.RESET);
    }

    private static void viewExpenses() {
        List<Expense> expenses = budgetService.getExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        System.out.println("\nExpenses:");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%d. %s%s%s $%.2f - %s (%s)\n",
                i + 1,
                e.getCategory().color, e.getCategory().emoji, ConsoleColors.RESET,
                e.getAmount(), e.getDescription(), DateUtil.format(e.getDate()));
        }
    }

    private static void editExpense() {
        viewExpenses();
        int idx = -1;
        while (true) {
            System.out.print("Enter expense number to edit: ");
            String input = scanner.nextLine();
            try {
                idx = Integer.parseInt(input) - 1;
                if (idx < 0 || idx >= budgetService.getExpenses().size()) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid expense number." + ConsoleColors.RESET);
            }
        }
        double amount = 0;
        while (true) {
            System.out.print("New amount: $");
            String input = scanner.nextLine();
            try {
                amount = Double.parseDouble(input);
                if (amount < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid positive number for amount." + ConsoleColors.RESET);
            }
        }
        System.out.print("New description: ");
        String description = scanner.nextLine();
        System.out.println("New category:");
        for (Category c : Category.values()) {
            System.out.println(c.ordinal() + 1 + ". " + c.emoji + " " + c.name());
        }
        int catIdx = -1;
        while (true) {
            String input = scanner.nextLine();
            try {
                catIdx = Integer.parseInt(input) - 1;
                if (catIdx < 0 || catIdx >= Category.values().length) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid category number." + ConsoleColors.RESET);
            }
        }
        Category category = Category.values()[catIdx];
        LocalDate date = null;
        while (true) {
            System.out.print("New date (yyyy-MM-dd): ");
            String input = scanner.nextLine();
            try {
                date = DateUtil.parse(input);
                break;
            } catch (Exception e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid date in yyyy-MM-dd format." + ConsoleColors.RESET);
            }
        }
        budgetService.editExpense(idx, amount, description, category, date);
        System.out.println(ConsoleColors.GREEN + "Expense updated!" + ConsoleColors.RESET);
    }

    private static void deleteExpense() {
        viewExpenses();
        int idx = -1;
        while (true) {
            System.out.print("Enter expense number to delete: ");
            String input = scanner.nextLine();
            try {
                idx = Integer.parseInt(input) - 1;
                if (idx < 0 || idx >= budgetService.getExpenses().size()) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.YELLOW + "Please enter a valid expense number." + ConsoleColors.RESET);
            }
        }
        budgetService.removeExpense(idx);
        System.out.println(ConsoleColors.GREEN + "Expense deleted!" + ConsoleColors.RESET);
    }

    private static void showSummary() {
        System.out.printf("\nIncome: $%.2f\n", budgetService.getIncome());
        System.out.printf("Budget Limit: $%.2f\n", budgetService.getLimit());
        System.out.printf("Total Expenses: $%.2f\n", budgetService.getTotalExpenses());
        System.out.printf("Remaining: $%.2f\n", budgetService.getRemaining());
    }

    private static void generateHtmlReport() {
        report.HtmlReportGenerator.generate(
            budgetService.getExpenses(),
            budgetService.getIncome(),
            budgetService.getLimit()
        );
        System.out.println("HTML report generated: resources/expenses_report.html");
    }
}
