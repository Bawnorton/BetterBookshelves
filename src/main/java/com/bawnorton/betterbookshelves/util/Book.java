package com.bawnorton.betterbookshelves.util;

public enum Book {
    TOP_LEFT(0, 4, 15, 8),
    TOP_MIDDLE(5, 10, 15, 8),
    TOP_RIGHT(11, 15, 15, 8),
    BOTTOM_LEFT(0, 4, 7, 0),
    BOTTOM_MIDDLE(5, 10, 7, 0),
    BOTTOM_RIGHT(11, 15, 7, 0);

    private final int x1, x2, y1, y2;
    Book(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public int index() {
        return this.ordinal();
    }

    public boolean isWithin(int x, int y) {
        return x >= x1 && x <= x2 && y >= y2 && y <= y1;
    }

    public static Book getBook(int x, int y) {
        for (Book box : Book.values()) {
            if (box.isWithin(x, y)) {
                return box;
            }
        }
        return null;
    }
}
