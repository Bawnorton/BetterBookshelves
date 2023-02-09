package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.ConfigManager;
import com.bawnorton.betterbookshelves.render.BetterBookshelvesModelProvider;
import com.bawnorton.betterbookshelves.render.ChiseledBookshelfBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBookshelvesClient implements ClientModInitializer {
	public static final String MOD_ID = "betterbookshelves";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> new BetterBookshelvesModelProvider());
		BlockEntityRendererRegistry.register(BlockEntityType.CHISELED_BOOKSHELF, context -> new ChiseledBookshelfBlockEntityRenderer());

		ConfigManager.loadConfig();
		LOGGER.info("BetterBookshelves Client Initialized");
	}
}