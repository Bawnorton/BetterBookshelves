package com.bawnorton.betterbookshelves;

import net.fabricmc.api.ModInitializer;
import net.minecraft.state.property.IntProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBookshelves implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("betterbookshelves");
	public static final IntProperty BOOK_VALUE = IntProperty.of("book_binary", 0, 63);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}

