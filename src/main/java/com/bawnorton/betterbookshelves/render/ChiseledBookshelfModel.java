package com.bawnorton.betterbookshelves.render;

import com.bawnorton.betterbookshelves.util.IChiseledBookshelfBlockEntity;
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
import net.minecraft.class_7775;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bawnorton.betterbookshelves.BetterBookshelves.BOOK_VALUE;
import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;
import static net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;


@Environment(EnvType.CLIENT)
public class ChiseledBookshelfModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/base")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_side")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_top")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_1")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_2")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_3")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_4")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/enchanted_book_6")),
        new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/normal_book_1")),
    };
    private final Sprite[] SPRITES = new Sprite[SPRITE_IDS.length];
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
        for (int i = 0; i < SPRITE_IDS.length; i++) {
            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
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
        return SPRITES[0];
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
        String binaryString = Integer.toBinaryString(state.get(BOOK_VALUE));
        binaryString = String.format("%6s", binaryString).replace(' ', '0');
        LOGGER.info("Books: " + binaryString);
        emitter.square(facing, 0, 0, 1, 1, 0);
        emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();
        for(int i = 0; i < 6; i++) {
            if(binaryString.charAt(i) == '1') {
                emitter.square(facing, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, SPRITES[i+3], MutableQuadView.BAKE_LOCK_UV);
                emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            }
        }
        for(Direction dir: Direction.values()) {
            if(dir != facing) {
                if(dir != Direction.UP && dir != Direction.DOWN) {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                } else {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        QuadEmitter emitter = context.getEmitter();
        for(Direction dir : Direction.values()) {
            switch (dir) {
                case UP, DOWN -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
                case EAST, WEST, SOUTH -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
                case NORTH -> {
                    emitter.square(dir, 0, 0, 1, 1, 0);
                    emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
                    emitter.spriteColor(0, -1, -1, -1, -1);
                    emitter.emit();
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }
}
