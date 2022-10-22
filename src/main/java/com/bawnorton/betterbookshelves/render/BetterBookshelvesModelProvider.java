package com.bawnorton.betterbookshelves.render;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class BetterBookshelvesModelProvider implements ModelResourceProvider {
    public static final Identifier CHISELED_BOOKSHELF_MODEL = new Identifier("minecraft", "block/template_chiseled_bookshelf");

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) {
        if(resourceId.equals(CHISELED_BOOKSHELF_MODEL)) {
            LOGGER.info("Loading model resource: " + resourceId);
            return new ChiseledBookshelfModel();
        }
        return null;
    }
}
