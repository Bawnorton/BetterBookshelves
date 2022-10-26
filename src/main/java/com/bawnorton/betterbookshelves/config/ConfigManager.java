package com.bawnorton.betterbookshelves.config;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configPath = configDir.resolve("betterbookshelves.json");
        Config config = load(configPath);
        if(config.textPreview == null) {
            config.textPreview = Config.HoverType.ON;
        }
        if(config.bookTexture == null) {
            config.bookTexture = Config.BookTexture.PER_BOOK;
        }
        if(config.textSize == 0) {
            config.textSize = 10;
        }
        BetterBookshelves.config = config;
        LOGGER.info("Loaded config: " + config);
        save(configPath, config);
    }


    private static Config load(Path path) {
        Config config = new Config();
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                save(path, config);
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(path), Config.class);
            } catch (JsonSyntaxException e) {
                LOGGER.error("Failed to parse config file, using default config");
                config = new Config();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
        return config;
    }

    private static void save(Path path, Config config) {
        try {
            Files.write(path, GSON.toJson(config).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
