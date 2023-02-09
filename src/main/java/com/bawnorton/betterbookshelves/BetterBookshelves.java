package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.ServerConfigManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBookshelves implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("betterbookshelves");

	@Override
	public void onInitialize() {
		ServerConfigManager.loadConfig();
	}
}