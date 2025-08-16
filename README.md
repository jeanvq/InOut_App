# InOut App

A user-friendly Java CLI app to manage your income and expenses, with colorful categories and emoji icons. Generates HTML statistics and alerts you when your budget limit is reached.

## Features
- Add, view, edit, and delete expenses by category (food, bills, pleasure, savings, income, etc.)
- Each category has a color and emoji for easy recognition
- Set your income and monthly budget limit
- Automatic calculation of remaining budget
- Alerts when you reach your budget limit
- Generates an HTML report of your expenses
- Simple, English-only commands and interface

## How to Run
1. Compile all Java files:
   ```sh
   javac -d out src/model/*.java src/service/*.java src/util/*.java src/report/*.java src/cli/InOutApp.java
   ```
2. Run the app:
   ```sh
   java -cp out cli.InOutApp or ./inout
   ```
3. The HTML report will be generated at `resources/expenses_report.html`.

## Requirements
- Java 8 or higher

## Project Structure
- `src/model/` - Data models (Expense, Category, Budget)
- `src/service/` - Business logic
- `src/util/` - Utilities (colors, date)
- `src/cli/` - Command-line interface
- `src/report/` - HTML report generator
- `resources/` - Output for reports

---
Enjoy tracking your finances with InOut! ✨
