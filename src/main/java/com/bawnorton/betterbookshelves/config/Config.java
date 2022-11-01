package com.bawnorton.betterbookshelves.config;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.google.gson.annotations.SerializedName;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class Config {
    public TextPreview textPreview = TextPreview.ON;
    public BookTexture bookTexture = BookTexture.PER_BOOK;
    public int textSize = 10;

    public void save() {
        ConfigManager.saveConfig();
    }

    public Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("BetterBookshelves Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))
                        .tooltip(Text.of("General settings"))
                        .option(Option.createBuilder(TextPreview.class)
                                .name(Text.of("Text Preview"))
                                .tooltip(Text.of("How to display the text preview"))
                                .binding(BetterBookshelves.CONFIG.textPreview,
                                        () -> BetterBookshelves.CONFIG.textPreview,
                                        value -> BetterBookshelves.CONFIG.textPreview = value)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(BookTexture.class)
                                .name(Text.of("Book Texture"))
                                .tooltip(Text.of("How to display the book texture"))
                                .binding(BetterBookshelves.CONFIG.bookTexture,
                                        () -> BetterBookshelves.CONFIG.bookTexture,
                                        value -> BetterBookshelves.CONFIG.bookTexture = value)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(Integer.class)
                                .name(Text.of("Text Size"))
                                .tooltip(Text.of("The size of the text preview"))
                                .binding(BetterBookshelves.CONFIG.textSize,
                                        () -> BetterBookshelves.CONFIG.textSize,
                                        value -> BetterBookshelves.CONFIG.textSize = value)
                                .controller(integerOption -> new IntegerSliderController(integerOption, 5, 20, 1))
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }

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
        @SerializedName("under_crosshair") UNDER_CROSSHAIR;

        public Text toText() {
            return Text.of(name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase().replace("_", " "));
        }
    }

    public enum BookTexture {
        @SerializedName("per_slot") PER_SLOT,
        @SerializedName("per_book") PER_BOOK;

        public Text toText() {
            return Text.of(name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase().replace("_", " "));
        }
    }
}
