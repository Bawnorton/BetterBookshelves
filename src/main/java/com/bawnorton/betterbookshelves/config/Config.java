package com.bawnorton.betterbookshelves.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    public TextPreview textPreview = TextPreview.ON;
    public BookTexture bookTexture = BookTexture.PER_BOOK;
    public int textSize = 10;

    @Override
    public String toString() {
        return "Config{" +
                "textPreview=" + textPreview +
                ", bookTexture=" + bookTexture +
                ", textSize=" + textSize +
                '}';
    }

    public enum TextPreview {
        @SerializedName("off") OFF,
        @SerializedName("on") ON,
        @SerializedName("under_crosshair") UNDER_CROSSHAIR
    }

    public enum BookTexture {
        @SerializedName("per_slot") PER_SLOT,
        @SerializedName("per_book") PER_BOOK
    }
}
