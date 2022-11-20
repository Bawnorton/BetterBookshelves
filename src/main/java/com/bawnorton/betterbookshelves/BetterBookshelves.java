package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBookshelves implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("betterbookshelves");

	@Override
	public void onInitialize() {
		LOGGER.info("Loaded BetterBookshelves");
		ConfigManager.loadConfig();
	}
}

