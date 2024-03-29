package com.bawnorton.betterbookshelves.util;

public enum Book {
    TOP_LEFT(0, 5, 15, 8),
    TOP_MIDDLE(6, 11, 15, 8),
    TOP_RIGHT(12, 16, 15, 8),
    BOTTOM_LEFT(0, 5, 7, 0),
    BOTTOM_MIDDLE(6, 11, 7, 0),
    BOTTOM_RIGHT(12, 16, 7, 0),
    NONE(-1,-1,-1,-1);

    public final int x1, x2, y1, y2;

    Book(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public static Book getBook(int slot) {
        return slot < 0 || slot >= values().length ? NONE : values()[slot];
    }
}
