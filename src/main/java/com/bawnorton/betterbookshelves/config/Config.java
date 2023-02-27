package com.bawnorton.betterbookshelves.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Config INSTANCE;

    public static Config getInstance() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public static void update(Config config) {
        INSTANCE = config;
    }

    @Expose
    @SerializedName("per_book_texture")
    public Boolean perBookTexture = true;

    @Expose
    @SerializedName("text_preview")
    public TextPreview textPreview = TextPreview.ON;

    @Expose
    @SerializedName("text_size")
    public Integer textSize = 10;

    @Expose
    @SerializedName("book_type_comparator_output")
    public Boolean bookTypeComparatorOutput = false;

    @Expose
    @SerializedName("enchanting_table_book_requirement")
    public Integer enchantingTableBookRequirement = 3;

    @Expose
    @SerializedName("book_textures")
    public List<BookTexture> bookTextures = new ArrayList<>();

    @Expose
    @SerializedName("enchanted_book_textures")
    public List<EnchantedTexture> enchantedTextures = new ArrayList<>();

    @Override
    public String toString() {
        return "Config{" +
                "perBookTexture=" + perBookTexture +
                ", textPreview=" + textPreview +
                ", textSize=" + textSize +
                ", bookTypeComparatorOutput=" + bookTypeComparatorOutput +
                ", bookTextures=" + bookTextures +
                ", enchantedTextures=" + enchantedTextures +
                '}';
    }

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

        public static BookTexture of(BookType type) {
            return new BookTexture(type, type.defaultModel(), type.defaultHex());
        }

        public String getHex() {
            return hex;
        }

        public int getDecimal() {
            return Integer.parseInt(getHex(), 16);
        }

        public void setHex(String toHexString) {
            try {
                Integer.parseInt(toHexString, 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hex color: " + toHexString);
            }
            this.hex = toHexString.startsWith("#") ? toHexString.substring(1) : hex;
        }

        @Override
        public String toString() {
            return "BookTexture{" +
                    "type=" + type +
                    ", model=" + model +
                    ", hex='" + hex + '\'' +
                    '}';
        }
    }

    public static final class EnchantedTexture {
        @SerializedName("enchantment")
        public String enchantement;
        @SerializedName("book_model")
        private Integer model;
        @SerializedName("hex_color")
        private String hex;

        public EnchantedTexture(String enchantement, Integer model, String hex) {
            this.enchantement = enchantement;
            this.model = model;
            this.hex = hex;
        }

        public static EnchantedTexture of(Identifier enchantement) {
            return new EnchantedTexture(enchantement.toString(), -1, "inherit");
        }

        public String getHex() {
            if(hex.equals("inherit")) return ConfigManager.getBookTexture(BookType.ENCHANTED_BOOK).getHex();
            return hex;
        }

        public int getDecimal() {
            return Integer.parseInt(getHex(), 16);
        }

        public void setHex(String toHexString) {
            try {
                Integer.parseInt(toHexString, 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hex color: " + toHexString);
            }
            this.hex = toHexString.startsWith("#") ? toHexString.substring(1) : toHexString;
        }

        public int getModel() {
            if(model == -1) return ConfigManager.getBookTexture(BookType.ENCHANTED_BOOK).model;
            return model;
        }

        public void setModel(int i) {
            if(i < -1 || i > 5) throw new IllegalArgumentException("Model must be between -1 and 5");
            if(i == ConfigManager.getBookTexture(BookType.ENCHANTED_BOOK).model) model = -1;
            else model = i;
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
        @SerializedName("minecraft:book") BOOK("3E9293", 0),
        @SerializedName("minecraft:written_book") WRITTEN_BOOK("C8335B", 3),
        @SerializedName("minecraft:writable_book") WRITABLE_BOOK("749B48", 1),
        @SerializedName("minecraft:enchanted_book") ENCHANTED_BOOK("9420DF", 2);

        private final String hexColor;
        private final int model;

        BookType(String hex, int model) {
            this.hexColor = hex;
            this.model = model;
        }

        public String defaultHex() {
            return hexColor;
        }

        public int defaultModel() {
            return model;
        }

        @Override
        public String toString() {
            return "BookType[" + name() + "]"
                    + "{hexColor='" + hexColor + '\''
                    + ", model=" + model + '}';
        }
    }
}
