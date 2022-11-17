package com.bawnorton.betterbookshelves.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("per_book_texture")
    public Boolean perBookTexture = true;
    @SerializedName("text_preview")
    public TextPreview textPreview = TextPreview.ON;
    @SerializedName("text_size")
    public Integer textSize = 10;
    @SerializedName("book_type_comparator_output")
    public Boolean bookTypeComparatorOutput = false;

    @Override
    public String toString() {
        return "Config{" +
                "textPreview=" + textPreview +
                ", perBookTexture=" + perBookTexture +
                ", textSize=" + textSize +
                '}';
    }

    public enum TextPreview {
        @SerializedName("off") OFF,
        @SerializedName("on") ON,
        @SerializedName("under_crosshair") UNDER_CROSSHAIR
    }
}
