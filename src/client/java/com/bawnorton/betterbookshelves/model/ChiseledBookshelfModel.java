package com.bawnorton.betterbookshelves.model;

import com.bawnorton.betterbookshelves.config.Config;
import com.bawnorton.betterbookshelves.config.ConfigManager;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;
import static com.bawnorton.betterbookshelves.BetterBookshelvesClient.LOGGER;

public class ChiseledBookshelfModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_empty")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_side")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf_top")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/default/default_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_2/book_2_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_1")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_2")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_3")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_4")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_5")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_6")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_1_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_2_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_3_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_4_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_5_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_0/book_0_6_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_1_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_2_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_3_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_4_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_5_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_1/book_1_6_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_1_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_2_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_3_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_4_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_5_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_3/book_3_6_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_1_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_2_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_3_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_4_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_5_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_4/book_4_6_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_1_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_2_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_3_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_4_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_5_band")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/chiseled_bookshelf/book_5/book_5_6_band"))
    };
    private final Map<String, Sprite> SPRITES = new HashMap<>();
    private Mesh mesh;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        RenderAttachedBlockView renderAttachedBlockView = (RenderAttachedBlockView) blockView;

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        QuadEmitter emitter = context.getEmitter();
        Direction facing = state.get(Properties.HORIZONTAL_FACING);
        if(renderer == null) return;
        BlockEntity blockEntity = blockView.getBlockEntity(pos);
        if(!(blockEntity instanceof ChiseledBookshelfBlockEntity)) return;

        List<ItemStack> inventory = (List<ItemStack>) renderAttachedBlockView.getBlockEntityRenderAttachment(pos);
        if(inventory == null) {
            LOGGER.info("Inventory is null, skipping render");
            return;
        }

        emitter.square(facing, 0, 0, 1, 1, 0);
        emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_empty"), MutableQuadView.BAKE_LOCK_UV);
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();

        LOGGER.info("Rendering " + inventory + " books");
        for(int i = 0; i < ChiseledBookshelfBlockEntity.MAX_BOOKS; i++) {
            ItemStack stack = inventory.get(i);
            Item item = stack.getItem();
            if(item == Items.AIR) continue;

            if(!Config.getInstance().perBookTexture) {
                emitter.square(facing, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, SPRITES.get("default_" + (i + 1)), MutableQuadView.BAKE_LOCK_UV);
                emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
                continue;
            }
            Config.BookTexture texture = ConfigManager.getBookTexture(item);

            int model = texture.model;
            int decimal = texture.getDecimal() - 0xFFFFFF - 1;
            if(texture.type == Config.BookType.ENCHANTED_BOOK) {
                NbtList enchantmentNbt = EnchantedBookItem.getEnchantmentNbt(stack);
                if(enchantmentNbt.isEmpty()) continue;
                NbtElement first = enchantmentNbt.get(0);
                if (first instanceof NbtCompound compound) {
                    String enchantment = compound.getString("id");
                    Config.EnchantedTexture enchantedTexture = ConfigManager.getEnchantedTexture(enchantment);
                    decimal = enchantedTexture.getDecimal() - 0xFFFFFF - 1;
                    model = enchantedTexture.getModel();
                }
            }

            emitter.square(facing, 0, 0, 1, 1, 0);
            emitter.spriteBake(0, SPRITES.get("book_" + model + "_" + (i + 1)), MutableQuadView.BAKE_LOCK_UV);
            emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
            emitter.spriteColor(0, decimal, decimal, decimal, decimal);
            emitter.emit();

            if(texture.type != Config.BookType.ENCHANTED_BOOK) {
                emitter.square(facing, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, SPRITES.get("book_" + model + "_" + (i + 1) + "_band"), MutableQuadView.BAKE_LOCK_UV);
                emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            }
        }

        emitter.material(renderer.materialFinder().blendMode(0, BlendMode.SOLID).find());
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
                    emitter.spriteBake(0, SPRITES.get("chiseled_bookshelf_empty"), MutableQuadView.BAKE_LOCK_UV);
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
                    int slot = Integer.parseInt(nbtString.substring(nbtString.indexOf("Slot:") + 5, nbtString.indexOf("b,id")));
                    String type = nbtString.substring(nbtString.indexOf("minecraft:") + 10, nbtString.indexOf("\"", nbtString.indexOf("minecraft:") + 10));
                    Config.BookType bookType = switch (type) {
                        case "writable_book" -> Config.BookType.WRITABLE_BOOK;
                        case "written_book" -> Config.BookType.WRITTEN_BOOK;
                        case "enchanted_book" -> Config.BookType.ENCHANTED_BOOK;
                        default -> Config.BookType.BOOK;
                    };
                    String regex = "(?<=StoredEnchantments:\\[\\{id:\")(.*)(?=\")";
                    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(nbtString);
                    if (Config.getInstance().perBookTexture) {
                        if(bookType != Config.BookType.ENCHANTED_BOOK) {
                            Config.BookTexture bookTexture = ConfigManager.getBookTexture(bookType);
                            int decimal = bookTexture.getDecimal() - 0xFFFFFF - 1;
                            emitter.spriteBake(0, SPRITES.get("book_" + bookTexture.model + "_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                            emitter.spriteColor(0, decimal, decimal, decimal, decimal);
                        } else {
                            if(!matcher.find()) continue;
                            String enchant = matcher.group(0);
                            Config.EnchantedTexture enchantedTexture = ConfigManager.getEnchantedTexture(enchant);
                            int decimal = enchantedTexture.getDecimal() - 0xFFFFFF - 1;
                            emitter.spriteBake(0, SPRITES.get("book_" + enchantedTexture.getModel() + "_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                            emitter.spriteColor(0, decimal, decimal, decimal, decimal);
                        }
                    } else if (!type.equals("minecraft:air")) {
                        emitter.spriteBake(0, SPRITES.get("default_" + (slot + 1)), MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColor(0, -1, -1, -1, -1);
                    }
                    emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                    emitter.emit();

                    if (Config.getInstance().perBookTexture) {
                        emitter.square(Direction.NORTH, 0, 0, 1, 1, 0);
                        int model;
                        if(bookType != Config.BookType.ENCHANTED_BOOK) model = ConfigManager.getBookTexture(bookType).model;
                        else {
                            if(!matcher.find()) continue;
                            String enchant = matcher.group(0);
                            model = ConfigManager.getEnchantedTexture(enchant).getModel();
                        }
                        if(model != 2) {
                            emitter.spriteBake(0, SPRITES.get("book_" + model + "_" + (slot + 1) + "_band"), MutableQuadView.BAKE_LOCK_UV);
                            emitter.spriteColor(0, -1, -1, -1, -1);
                            emitter.material(renderer.materialFinder().blendMode(0, CUTOUT_MIPPED).find());
                            emitter.emit();
                        }
                    }
                }
            }
        }
        context.meshConsumer().accept(mesh);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return null;
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
        return SPRITES.get("chiseled_bookshelf_empty");
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
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        for(SpriteIdentifier spriteIdentifier : SPRITE_IDS) {
            String path = spriteIdentifier.getTextureId().getPath();
            String name = path.substring(path.lastIndexOf('/') + 1);
            SPRITES.put(name, textureGetter.apply(spriteIdentifier));
        }
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if(renderer == null) return this;
        mesh = renderer.meshBuilder().build();
        return this;
    }
}
