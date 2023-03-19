package com.bawnorton.betterbookshelves.compat.client.wanilla;

import net.kas.wanilla.block.ChiseledBookshelfBlocks;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WanillaClientCompat {
    private static final Map<Identifier, List<SpriteIdentifier>> bookshelfTextures = Map.ofEntries(
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.SPRUCE_CHISELED_BOOKSHELF), of("spruce")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.BIRCH_CHISELED_BOOKSHELF), of("birch")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.JUNGLE_CHISELED_BOOKSHELF), of("jungle")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.ACACIA_CHISELED_BOOKSHELF), of("acacia")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.DARK_OAK_CHISELED_BOOKSHELF), of("dark_oak")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.MANGROVE_CHISELED_BOOKSHELF), of("mangrove")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.CHERRY_CHISELED_BOOKSHELF), of("cherry")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.BAMBOO_CHISELED_BOOKSHELF), of("bamboo")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.CRIMSON_CHISELED_BOOKSHELF), of("crimson")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.WARPED_CHISELED_BOOKSHELF), of("warped")),
            Map.entry(Registries.BLOCK.getId(ChiseledBookshelfBlocks.GOLD_CHISELED_BOOKSHELF), of("gold"))
    );
    private static final Map<Identifier, List<Sprite>> bookshelfSprites = new HashMap<>();

    private static List<SpriteIdentifier> of(String woodType) {
        return List.of(
                new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("wanilla", "block/chiseled_bookshelf/" + woodType + "/" + woodType + "_chiseled_bookshelf_empty")),
                new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("wanilla", "block/chiseled_bookshelf/" + woodType + "/" + woodType + "_chiseled_bookshelf_side")),
                new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("wanilla", "block/chiseled_bookshelf/" + woodType + "/" + woodType + "_chiseled_bookshelf_top"))
        );
    }

    public static void buildBookshelfSprites(Function<SpriteIdentifier, Sprite> textureGetter) {
        bookshelfTextures.forEach((blockId, textures) -> {
            List<Sprite> sprites = List.of(
                    textureGetter.apply(textures.get(0)),
                    textureGetter.apply(textures.get(1)),
                    textureGetter.apply(textures.get(2))
            );
            bookshelfSprites.put(blockId, sprites);
        });
    }

    public static List<Sprite> getBookshelfSprites(Identifier id) {
        return bookshelfSprites.get(id);
    }
}
