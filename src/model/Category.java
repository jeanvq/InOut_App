package model;

public enum Category {
    FOOD("🍔", "\u001B[33m"),
    BILLS("💡", "\u001B[35m"),
    PLEASURE("🎉", "\u001B[36m"),
    SAVINGS("💰", "\u001B[32m"),
    INCOME("💵", "\u001B[34m"),
    OTHER("🛒", "\u001B[37m");

    public final String emoji;
    public final String color;

    Category(String emoji, String color) {
        this.emoji = emoji;
        this.color = color;
    }
}
