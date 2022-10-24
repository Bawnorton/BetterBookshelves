package com.bawnorton.betterbookshelves.client;

import com.bawnorton.betterbookshelves.BetterBookshelves;
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
import net.minecraft.class_7775;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PlayerScreenHandler;
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

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;
import static net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;


public class ChiseledBookshelfModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/base")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_side")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_top")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/signed_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_5")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/written_book_6")),

    };
    private final Map<String, Sprite> SPRITES = new HashMap<>();
    private Mesh mesh;

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void method_45785(Function<Identifier, UnbakedModel> function) {
    }

    @Nullable
    @Override
    public BakedModel bake(class_7775 arg, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
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
        List<ItemStack> items = BetterBookshelves.bookshelves.get(pos);
        String base5String = String.format("%6s", toBase5Representation(items)).replace(' ', '0');
        emitter.square(facing, 0, 0, 1, 1, 0);
        emitter.spriteBake(0, SPRITES.get("base"), MutableQuadView.BAKE_LOCK_UV);
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();
        for(int i = 0; i < 6; i++) {
            char c = base5String.charAt(i);
            String type;
            if(c == '1') {
                type = "enchanted";
            } else if(c == '2') {
                type = "normal";
            } else if(c == '3') {
                type = "signed";
            } else if(c == '4') {
                type = "written";
            } else continue;
            emitter.square(facing, 0, 0, 1, 1, 0);
            emitter.spriteBake(0, SPRITES.get(type + "_book_" + (i + 1)), MutableQuadView.BAKE_LOCK_UV);
            emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
            emitter.spriteColor(0, -1, -1, -1, -1);
            emitter.emit();

        }
        for(Direction dir: Direction.values()) {
            if(dir != facing) {
                if(dir != Direction.UP && dir != Direction.DOWN) {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_side"), MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                } else {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_top"), MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }

    private String toBase5Representation(List<ItemStack> books) {
        StringBuilder sb = new StringBuilder();
        for(ItemStack stack : books) {
            if(stack == ItemStack.EMPTY) {
                sb.append("0");
            } else if (stack.getItem() == Items.ENCHANTED_BOOK) {
                sb.append("1");
            } else if (stack.getItem() == Items.BOOK) {
                sb.append("2");
            } else if (stack.getItem() == Items.WRITTEN_BOOK) {
                sb.append("3");
            } else if (stack.getItem() == Items.WRITABLE_BOOK) {
                sb.append("4");
            }
        }
        return sb.reverse().toString();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
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
                    String nbtString = item.asString();
                    int slot = 5 - Integer.parseInt(nbtString.substring(nbtString.indexOf("Slot:") + 5, nbtString.indexOf("b,id")));
                    String type = nbtString.substring(nbtString.indexOf("id:") + 4, nbtString.indexOf("\"", nbtString.indexOf("id:") + 4));
                    switch (type) {
                        case "minecraft:enchanted_book":
                            type = "enchanted";
                            break;
                        case "minecraft:book":
                            type = "normal";
                            break;
                        case "minecraft:written_book":
                            type = "signed";
                            break;
                        case "minecraft:writable_book":
                            type = "written";
                            break;
                        default:
                            continue;
                    }
                    emitter.square(Direction.NORTH, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES.get(type + "_book_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                    emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }
}
