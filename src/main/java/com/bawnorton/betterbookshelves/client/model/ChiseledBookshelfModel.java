package com.bawnorton.betterbookshelves.client.model;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.state.property.BetterBookshelvesProperties;
import com.bawnorton.betterbookshelves.util.Helper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;

@Environment(EnvType.CLIENT)
public class ChiseledBookshelfModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/base")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_side")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_top")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/default_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/writable_book_6")),

    };
    private final Map<String, Sprite> SPRITES = new HashMap<>();
    private Mesh mesh;

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
    }

    @Nullable
    @Override
    public BakedModel bake(Baker arg, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        for (SpriteIdentifier spriteId : SPRITE_IDS) {
            SPRITES.put(spriteId.getTextureId().getPath().substring(6), textureGetter.apply(spriteId));
        }
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        assert renderer != null;
        MeshBuilder builder = renderer.meshBuilder();
        mesh = builder.build();
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return SPRITES.get("base");
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        QuadEmitter emitter = context.getEmitter();
        Direction facing = state.get(Properties.HORIZONTAL_FACING);
        if(renderer == null) return;
        BlockEntity blockEntity = blockView.getBlockEntity(pos);
        if(!(blockEntity instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity)) return;
        emitter.square(facing, 0, 0, 1, 1, 0);
        emitter.spriteBake(0, SPRITES.get("base"), MutableQuadView.BAKE_LOCK_UV);
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();

        List<ItemStack> inventory = Helper.getInventory(chiseledBookshelfBlockEntity);
        List<BooleanProperty> slotProperties = List.of(Properties.SLOT_0_OCCUPIED, Properties.SLOT_1_OCCUPIED, Properties.SLOT_2_OCCUPIED, Properties.SLOT_3_OCCUPIED, Properties.SLOT_4_OCCUPIED, Properties.SLOT_5_OCCUPIED);
        for(int i = 0; i < inventory.size(); i++) {
            if(state.get(slotProperties.get(i))) {
                Item item = inventory.get(i).getItem();
                if(item == Items.AIR || i == state.get(Properties.LAST_INTERACTION_BOOK_SLOT) - 1) {
                    item = switch (state.get(BetterBookshelvesProperties.LAST_BOOK_TYPE)) {
                        case 1 -> Items.BOOK;
                        case 2 -> Items.WRITABLE_BOOK;
                        case 3 -> Items.WRITTEN_BOOK;
                        case 4 -> Items.ENCHANTED_BOOK;
                        default -> Items.AIR;
                    };
                }
                emitter.square(facing, 0, 0, 1, 1, 0);
                if (BetterBookshelves.CONFIG.perBookTexture) {
                    emitter.spriteBake(0, SPRITES.get(item + "_" + (i + 1)), MutableQuadView.BAKE_LOCK_UV);
                } else {
                    emitter.spriteBake(0, SPRITES.get("default_" + (i + 1)), MutableQuadView.BAKE_LOCK_UV);
                }
            }
            emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
            emitter.spriteColor(0, -1, -1, -1, -1);
            emitter.emit();
        }

        for(Direction dir: Direction.values()) {
            if(dir != facing) {
                emitter.square(dir, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, SPRITES.get(dir != Direction.UP && dir != Direction.DOWN ? "chiseled_bookshelf_side" : "chiseled_bookshelf_top"), MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            }
        }
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if(renderer == null) return;
        QuadEmitter emitter = context.getEmitter();
        NbtCompound tag = stack.getNbt();
        for(Direction dir : Direction.values()) {
            switch (dir) {
                case UP, DOWN -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_top"), MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
                case EAST, WEST, SOUTH -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_side"), MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
                case NORTH -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get("base"), MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }

        if(tag != null && tag.contains("BlockEntityTag")) {
            NbtCompound blockEntityTag = tag.getCompound("BlockEntityTag");
            if(blockEntityTag.contains("Items")) {
                NbtList items = blockEntityTag.getList("Items", NbtElement.COMPOUND_TYPE);
                for(NbtElement item: items) {
                    emitter.square(Direction.NORTH, 0, 0, 1, 1, 0);
                    String nbtString = item.asString();
                    int slot = 5 - Integer.parseInt(nbtString.substring(nbtString.indexOf("Slot:") + 5, nbtString.indexOf("b,id")));
                    String type = nbtString.substring(nbtString.indexOf("minecraft:") + 10, nbtString.indexOf("\"", nbtString.indexOf("minecraft:") + 10));
                    if (BetterBookshelves.CONFIG.perBookTexture) {
                        emitter.spriteBake(0, SPRITES.get(type + "_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                    } else {
                        if(!type.equals("minecraft:air")) {
                            emitter.spriteBake(0, SPRITES.get("default_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                        }
                    }
                    emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }
}
