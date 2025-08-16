package report;

import model.Expense;
import util.DateUtil;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlReportGenerator {
    public static void generate(List<Expense> expenses, double income, double limit) {
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double remaining = income - totalExpenses;
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Expenses Report</title></head><body>");
        html.append("<h1>Expenses Report</h1>");
        html.append("<p><b>Income:</b> $" + String.format("%.2f", income) + "<br>");
        html.append("<b>Budget Limit:</b> $" + String.format("%.2f", limit) + "<br>");
        html.append("<b>Total Expenses:</b> $" + String.format("%.2f", totalExpenses) + "<br>");
        html.append("<b>Remaining:</b> $" + String.format("%.2f", remaining) + "</p>");
        if (expenses.isEmpty()) {
            html.append("<p>No expenses to show.</p>");
        } else {
            html.append("<table border='1' cellpadding='5'><tr><th>#</th><th>Category</th><th>Amount</th><th>Description</th><th>Date</th></tr>");
            int i = 1;
            for (Expense e : expenses) {
                html.append("<tr>")
                    .append("<td>").append(i++).append("</td>")
                    .append("<td>").append(e.getCategory().emoji).append(" ").append(e.getCategory().name()).append("</td>")
                    .append("<td>$").append(String.format("%.2f", e.getAmount())).append("</td>")
                    .append("<td>").append(e.getDescription()).append("</td>")
                    .append("<td>").append(DateUtil.format(e.getDate())).append("</td>")
                    .append("</tr>");
            }
            html.append("</table>");
        }
        html.append("</body></html>");
        try (FileWriter writer = new FileWriter("resources/expenses_report.html")) {
            writer.write(html.toString());
        } catch (IOException e) {
            System.out.println("Failed to write HTML report: " + e.getMessage());
        }
    }
}
