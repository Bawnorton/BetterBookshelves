package com.bawnorton.betterbookshelves.config;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("betterbookshelves.json");

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("BetterBookshelves Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))
                        .tooltip(Text.of("General settings"))
                        .option(Option.createBuilder(Config.TextPreview.class)
                                .name(Text.of("Text Preview"))
                                .tooltip(Text.of("How to display the text preview"))
                                .binding(BetterBookshelves.CONFIG.textPreview,
                                        () -> BetterBookshelves.CONFIG.textPreview,
                                        value -> BetterBookshelves.CONFIG.textPreview = value)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(Config.BookTexture.class)
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
                .save(ConfigManager::save)
                .build()
                .generateScreen(parent);
    }

    public static void loadConfig() {
        Config config = load();
        if(config.textPreview == null) config.textPreview = Config.TextPreview.ON;
        if(config.bookTexture == null) config.bookTexture = Config.BookTexture.PER_BOOK;
        if(config.textSize == 0) config.textSize = 10;
        BetterBookshelves.CONFIG = config;
        LOGGER.info("Loaded config: " + config);
    }

    private static Config load() {
        Config config = new Config();
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                return config;
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(configPath), Config.class);
            } catch (JsonSyntaxException e) {
                LOGGER.error("Failed to parse config file, using default config");
                config = new Config();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
        return config;
    }

    private static void save() {
        try {
            Files.write(configPath, GSON.toJson(BetterBookshelves.CONFIG).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
