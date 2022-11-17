package com.bawnorton.betterbookshelves.config;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("betterbookshelves.json");

    public static Screen createGui(Screen parent) {
        return parent;
    }

    public static void loadConfig() {
        Config config = load();
        if(config.textPreview == null) config.textPreview = Config.TextPreview.ON;
        if(config.perBookTexture == null) config.perBookTexture = true;
        if(config.textSize == null) config.textSize = 10;
        if(config.bookTypeComparatorOutput == null) config.bookTypeComparatorOutput = false;

        if(config.textSize > 20) config.textSize = 20;
        if(config.textSize < 5) config.textSize = 5;

        BetterBookshelves.CONFIG = config;
        save();
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
