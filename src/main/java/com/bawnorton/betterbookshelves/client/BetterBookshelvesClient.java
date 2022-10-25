package com.bawnorton.betterbookshelves.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;

public class BetterBookshelvesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> new BetterBookshelvesModelProvider());
        BlockEntityRendererRegistry.register(BlockEntityType.CHISELED_BOOKSHELF, ChiseledBookshelfBlockEntityRenderer::new);
    }
}
