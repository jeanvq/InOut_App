package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parse(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER);
    }

    public static String format(LocalDate date) {
        return date.format(FORMATTER);
    }
}
