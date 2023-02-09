package com.bawnorton.betterbookshelves.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import org.spongepowered.asm.mixin.transformer.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class ServerConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("betterbookshelves_server.json");

    public static void loadConfig() {
        ServerConfig config = load();

        // Set config values and repair if needed
        if (config.bookTypeComparatorOutput == null) config.bookTypeComparatorOutput = false;
        if (config.enchantingTableBookRequirement == null) config.enchantingTableBookRequirement = 3;
        if (config.enchantingTableBookRequirement > ChiseledBookshelfBlockEntity.MAX_BOOKS) config.enchantingTableBookRequirement = ChiseledBookshelfBlockEntity.MAX_BOOKS;
        if (config.enchantingTableBookRequirement < -1) config.enchantingTableBookRequirement = -1;

        ServerConfig.update(config);
        save();
        LOGGER.info("Loaded server config");
    }

    private static ServerConfig load() {
        ServerConfig config = ServerConfig.getInstance();
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                return config;
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(configPath), ServerConfig.class);
            } catch (JsonSyntaxException e) {
                LOGGER.error("Failed to parse config file, using default config");
                config = new ServerConfig();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
        return config == null ? new ServerConfig() : config;
    }

    private static void save() {
        try {
            Files.write(configPath, GSON.toJson(ServerConfig.getInstance()).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
