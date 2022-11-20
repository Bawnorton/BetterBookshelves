package com.bawnorton.betterbookshelves.config;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Config {
    @SerializedName("per_book_texture")
    public Boolean perBookTexture = true;

    @SerializedName("text_preview")
    public TextPreview textPreview = TextPreview.ON;

    @SerializedName("text_size")
    public Integer textSize = 10;

    @SerializedName("book_type_comparator_output")
    public Boolean bookTypeComparatorOutput = false;

    @SerializedName("book_textures")
    public List<BookTexture> bookTextures = new ArrayList<>();

    @SerializedName("enchanted_book_textures")
    public List<EnchantedTexture> enchantedTextures = new ArrayList<>();

    public static final class BookTexture {
        @SerializedName("book_type")
        public BookType type;
        @SerializedName("book_model")
        public Integer model;
        @SerializedName("hex_color")
        private String hex;

        public BookTexture(BookType type, Integer model, String hex) {
            this.type = type;
            this.model = model;
            this.hex = hex;
        }

        public String getHex() {
            return hex.startsWith("#") ? hex.substring(1) : hex;
        }

        public int getDecimal() {
            return Integer.parseInt(getHex(), 16);
        }

        public void setHex(String toHexString) {
            this.hex = "#" + (toHexString.startsWith("#") ? toHexString.substring(1) : toHexString);
        }

        public void setDecimal(int decimal) {
            this.hex = "#" + Integer.toHexString(decimal);
        }

        public static BookTexture of(BookType type) {
            return new BookTexture(type, type.getModel(), type.getHexColor());
        }

    }

    public static final class EnchantedTexture {
        @SerializedName("enchantment")
        public String enchantement;
        @SerializedName("book_model")
        public Integer model;
        @SerializedName("hex_color")
        public String hex;

        public EnchantedTexture(String enchantement, Integer model, String hex) {
            this.enchantement = enchantement;
            this.model = model;
            this.hex = hex;
        }

        public String getHex() {
            if(hex.equals("inherit")) return ConfigManager.getBookTexture(BookType.ENCHANTED_BOOK).getHex();
            return hex.startsWith("#") ? hex.substring(1) : hex;
        }

        public int getDecimal() {
            return Integer.parseInt(getHex(), 16);
        }

        public void setHex(String toHexString) {
            this.hex = "#" + (toHexString.startsWith("#") ? toHexString.substring(1) : toHexString);
        }

        public void setDecimal(int decimal) {
            this.hex = "#" + Integer.toHexString(decimal);
        }

        public static EnchantedTexture of(Identifier enchantement) {
            return new EnchantedTexture(enchantement.toString(), 2, "inherit");
        }

        @Override
        public String toString() {
            return "EnchantedTexture{" +
                    "enchantement='" + enchantement + '\'' +
                    ", model=" + model +
                    ", hex='" + hex + '\'' +
                    '}';
        }
    }

    public enum TextPreview {
        @SerializedName("off") OFF,
        @SerializedName("on") ON,
        @SerializedName("under_crosshair") UNDER_CROSSHAIR
    }

    public enum BookType {
        @SerializedName("book") BOOK("3E9293", 0),
        @SerializedName("written_book") WRITTEN_BOOK("C8335B", 3),
        @SerializedName("writable_book") WRITABLE_BOOK("749B48", 1),
        @SerializedName("enchanted_book") ENCHANTED_BOOK("651A96", 2);

        private String hexColor;
        private int model;

        BookType(String hex, int model) {
            this.hexColor = hex;
            this.model = model;
        }

        public void setHexColor(String hex) {
            this.hexColor = hex;
        }

        public void setModel(int model) {
            this.model = model;
        }

        public String getHexColor() {
            return hexColor;
        }

        public int getModel() {
            return model;
        }
    }
}
