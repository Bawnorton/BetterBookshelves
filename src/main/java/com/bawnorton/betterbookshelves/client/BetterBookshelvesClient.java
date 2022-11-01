package com.bawnorton.betterbookshelves.client;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.client.render.BetterBookshelvesModelProvider;
import com.bawnorton.betterbookshelves.client.render.ChiseledBookshelfBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class BetterBookshelvesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> new BetterBookshelvesModelProvider());
        BlockEntityRendererRegistry.register(BlockEntityType.CHISELED_BOOKSHELF, ChiseledBookshelfBlockEntityRenderer::new);

        KeyBindingHelper.registerKeyBinding(BetterBookshelvesKeybinds.OPEN_CONFIG);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (BetterBookshelvesKeybinds.OPEN_CONFIG.wasPressed()) {
                Screen parent = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(BetterBookshelves.CONFIG.createGui(parent));
            }
        });
    }
}
