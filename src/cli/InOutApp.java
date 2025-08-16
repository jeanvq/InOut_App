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
        System.out.print("Enter your monthly income: $");
        double income = Double.parseDouble(scanner.nextLine());
        System.out.print("Set your monthly budget limit: $");
        double limit = Double.parseDouble(scanner.nextLine());
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
        System.out.print("Amount: $");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.println("Category:");
        for (Category c : Category.values()) {
            System.out.println(c.ordinal() + 1 + ". " + c.emoji + " " + c.name());
        }
        int catIdx = Integer.parseInt(scanner.nextLine()) - 1;
        Category category = Category.values()[catIdx];
        System.out.print("Date (yyyy-MM-dd): ");
        LocalDate date = DateUtil.parse(scanner.nextLine());
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
        System.out.print("Enter expense number to edit: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx < 0 || idx >= budgetService.getExpenses().size()) {
            System.out.println("Invalid expense number.");
            return;
        }
        System.out.print("New amount: $");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("New description: ");
        String description = scanner.nextLine();
        System.out.println("New category:");
        for (Category c : Category.values()) {
            System.out.println(c.ordinal() + 1 + ". " + c.emoji + " " + c.name());
        }
        int catIdx = Integer.parseInt(scanner.nextLine()) - 1;
        Category category = Category.values()[catIdx];
        System.out.print("New date (yyyy-MM-dd): ");
        LocalDate date = DateUtil.parse(scanner.nextLine());
        budgetService.editExpense(idx, amount, description, category, date);
        System.out.println(ConsoleColors.GREEN + "Expense updated!" + ConsoleColors.RESET);
    }

    private static void deleteExpense() {
        viewExpenses();
        System.out.print("Enter expense number to delete: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx < 0 || idx >= budgetService.getExpenses().size()) {
            System.out.println("Invalid expense number.");
            return;
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
