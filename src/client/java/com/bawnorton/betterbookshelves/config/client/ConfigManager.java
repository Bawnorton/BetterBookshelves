package com.bawnorton.betterbookshelves.config.client;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.compat.yacl.YACLImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("betterbookshelves.json");

    public static void loadConfig() {
        Config config = load();
        // Set config values and repair if needed
        if (config.textPreview == null) config.textPreview = Config.TextPreview.ON;
        if (config.perBookTexture == null) config.perBookTexture = true;
        if (config.textSize == null) config.textSize = 10;
        if (config.bookTextures == null) config.bookTextures = defaultBookTextures();
        if (config.enchantedTextures == null) config.enchantedTextures = defaultEnchantedTextures();

        if (config.textSize > 20) config.textSize = 20;
        if (config.textSize < 5) config.textSize = 5;

        // validate book textures
        List<Config.BookTexture> bookTextures = new ArrayList<>();
        for (Config.BookType bookType : Config.BookType.values()) {
            Config.BookTexture bookTexture = config.bookTextures.stream().filter(b -> b.type == bookType).findFirst().orElse(null);
            if (bookTexture == null) {
                bookTexture = Config.BookTexture.of(bookType);
            }
            if (bookTexture.model < 0 || bookTexture.model > 5) bookTexture.model = 0;
            try {
                Integer.parseInt(bookTexture.getHex(), 16);
            } catch (NumberFormatException e) {
                bookTexture.setHex("000000");
            }
            bookTextures.add(bookTexture);
        }
        config.bookTextures = bookTextures;

        // validate enchanted textures
        List<Config.EnchantedTexture> enchantedTextures = new ArrayList<>();
        for (Identifier enchantment : Registries.ENCHANTMENT.getIds()) {
            Config.EnchantedTexture enchantedTexture = config.enchantedTextures.stream().filter(e -> e.enchantement.equals(enchantment.toString())).findFirst().orElse(null);
            if (enchantedTexture == null) {
                enchantedTexture = Config.EnchantedTexture.of(enchantment);
            }
            if (enchantedTexture.getModel() < -1 || enchantedTexture.getModel() > 5) enchantedTexture.setModel(-1);
            try {
                String hex = enchantedTexture.getHex();
                if (!hex.equals("inherit")) Integer.parseInt(hex, 16);
            } catch (NumberFormatException e) {
                enchantedTexture.setHex("000000");
            }
            enchantedTextures.add(enchantedTexture);
        }
        config.enchantedTextures = enchantedTextures;

        Config.update(config);
        save();
        LOGGER.info("Loaded client config");
    }

    private static List<Config.BookTexture> defaultBookTextures() {
        return List.of(
                Config.BookTexture.of(Config.BookType.BOOK),
                Config.BookTexture.of(Config.BookType.WRITABLE_BOOK),
                Config.BookTexture.of(Config.BookType.WRITTEN_BOOK),
                Config.BookTexture.of(Config.BookType.ENCHANTED_BOOK)
        );
    }

    private static List<Config.EnchantedTexture> defaultEnchantedTextures() {
        Set<Identifier> enchantmentIds = Registries.ENCHANTMENT.getIds();
        List<Config.EnchantedTexture> textures = new ArrayList<>();
        for (Identifier id : enchantmentIds) {
            textures.add(Config.EnchantedTexture.of(id));
        }
        return textures;
    }

    public static Config.BookTexture getBookTexture(Config.BookType type) {
        for (Config.BookTexture bookTexture : Config.getInstance().bookTextures) {
            if (bookTexture.type.equals(type)) {
                return bookTexture;
            }
        }
        return Config.BookTexture.of(Config.BookType.BOOK);
    }

    public static Config.BookTexture getBookTexture(Item item) {
        if(item == Items.BOOK) {
            return getBookTexture(Config.BookType.BOOK);
        } else if(item == Items.WRITABLE_BOOK) {
            return getBookTexture(Config.BookType.WRITABLE_BOOK);
        } else if(item == Items.WRITTEN_BOOK) {
            return getBookTexture(Config.BookType.WRITTEN_BOOK);
        } else if(item == Items.ENCHANTED_BOOK) {
            return getBookTexture(Config.BookType.ENCHANTED_BOOK);
        } else {
            return getBookTexture(Config.BookType.BOOK);
        }
    }


    public static Config.EnchantedTexture getEnchantedTexture(String enchantment) {
        for (Config.EnchantedTexture enchantedTexture : Config.getInstance().enchantedTextures) {
            if (enchantedTexture.enchantement.equals(enchantment)) {
                return enchantedTexture;
            }
        }
        return Config.EnchantedTexture.of(Registries.ENCHANTMENT.getIds().stream().findFirst().orElseThrow());
    }

    private static Config load() {
        Config config = Config.getInstance();
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
            Files.write(configPath, GSON.toJson(Config.getInstance()).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    public static void saveConfig() {
        save();
        LOGGER.info("Saved client config");
    }

    public static Screen getConfigScreen() {
        if(!FabricLoader.getInstance().isModLoaded("yet-another-config-lib")) {
            return new ConfirmScreen((result) -> {
                if (result) {
                    Util.getOperatingSystem().open(URI.create("https://www.curseforge.com/minecraft/mc-mods/yacl/files"));
                }
                MinecraftClient.getInstance().setScreen(null);
            }, Text.of("Yet Another Config Lib not installed!"), Text.of("YACL is required to edit the config in game, would you like to install YACL?"), ScreenTexts.YES, ScreenTexts.NO);
        } else {
            String versionString = FabricLoader.getInstance().getModContainer("yet-another-config-lib").get().getMetadata().getVersion().getFriendlyString();
            int major = Integer.parseInt(versionString.split("\\.")[0]);
            int minor = Integer.parseInt(versionString.split("\\.")[1]);
            int patch = Integer.parseInt(versionString.split("\\.")[2]);
            if (major < 2 || (major == 2 && minor < 3) || (major == 2 && minor == 3 && patch < 0)) {
                return new ConfirmScreen((result) -> {
                    if (result) {
                        Util.getOperatingSystem().open(URI.create("https://www.curseforge.com/minecraft/mc-mods/yacl/files"));
                    }
                    MinecraftClient.getInstance().setScreen(null);
                }, Text.of("YACL version is '%s' not compatible with this mod".formatted(versionString)), Text.of("YACL version 2.3.0 or higher is required to edit the config in game, would you like to update YACL?"), ScreenTexts.YES, ScreenTexts.NO);
            }

        }
        return YACLImpl.getScreen();
    }
}
