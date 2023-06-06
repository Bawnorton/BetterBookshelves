package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.ServerConfigManager;
import dev.isxander.yacl3.gui.ImageRenderer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class BetterBookshelves implements ModInitializer {
	public static final String MOD_ID = "betterbookshelves";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		ServerConfigManager.loadConfig();
		LOGGER.info("BetterBookshelves Initialized");
	}
}