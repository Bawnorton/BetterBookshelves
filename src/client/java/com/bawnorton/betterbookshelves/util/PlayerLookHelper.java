package com.bawnorton.betterbookshelves.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class PlayerLookHelper {
    private static boolean isNotChiseledBookshelf(BlockState blockState) {
        return blockState.getBlock() != Blocks.CHISELED_BOOKSHELF;
    }

    public static Pair<Book, ItemStack> getLookingAtBook(BlockEntity entity) {
        Pair<Book, ItemStack> book = new Pair<>(Book.NONE, ItemStack.EMPTY);
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult target = client.crosshairTarget;
        if(!(target != null && target.getType() == HitResult.Type.BLOCK)) return book;

        BlockHitResult blockHitResult = (BlockHitResult) target;
        BlockPos pos = blockHitResult.getBlockPos();
        assert client.world != null;
        if(entity == null) entity = client.world.getBlockEntity(pos);
        if(!(Objects.equals(client.world.getBlockEntity(pos), entity)) || entity == null) return book;

        BlockState state = entity.getCachedState();
        if(isNotChiseledBookshelf(state)) return book;

        ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity = (ChiseledBookshelfBlockEntity) entity;
        Book bookToRender = getBook(chiseledBookshelfBlockEntity);
        if(!state.isOf(Blocks.CHISELED_BOOKSHELF)) return book;
        if(bookToRender == Book.NONE) return book;

        if(!switch (bookToRender.getSlot()) {
            case 0 -> state.get(Properties.SLOT_0_OCCUPIED);
            case 1 -> state.get(Properties.SLOT_1_OCCUPIED);
            case 2 -> state.get(Properties.SLOT_2_OCCUPIED);
            case 3 -> state.get(Properties.SLOT_3_OCCUPIED);
            case 4 -> state.get(Properties.SLOT_4_OCCUPIED);
            case 5 -> state.get(Properties.SLOT_5_OCCUPIED);
            default -> false;
        }) return book;

        return new Pair<>(bookToRender, chiseledBookshelfBlockEntity.getStack(bookToRender.getSlot()));
    }

    private static Book getBook(ChiseledBookshelfBlockEntity entity) {
        assert MinecraftClient.getInstance().player != null;
        Vec3d relPos = getLookOnBlockCoords(MinecraftClient.getInstance().player, entity);
        if(relPos == null) return Book.NONE;
        boolean isFrontFace = isOnFrontFace(entity, relPos);
        if (!isFrontFace) return Book.NONE;
        int x = getRelativeX(entity, relPos);
        int y = getRelativeY(relPos);
        return Book.getBook(x, y);
    }

    public static List<Text> getBookText(ItemStack book) {
        List<Text> displayText = new ArrayList<>();
        displayText.add(book.getName());
        if(book.getItem() == Items.ENCHANTED_BOOK) {
            NbtCompound tag = book.getNbt();
            if(tag != null && tag.contains("StoredEnchantments")) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.fromNbt(tag.getList("StoredEnchantments", 10));
                for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    displayText.add(entry.getKey().getName(entry.getValue()));
                }
            }
        }
        return displayText;
    }

    @Nullable
    private static Vec3d getLookOnBlockCoords(PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
        if (!blockEntity.canPlayerUse(player)) return null;
        Vec3d hitPos = getLookPos(player);
        return hitPos.subtract(Vec3d.of(blockEntity.getPos()));
    }

    public static Vec3d getLookPos(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity cameraEntity = client.getCameraEntity();
        if (cameraEntity == null) return Vec3d.ZERO;
        Vec3d cameraDir = cameraEntity.getRotationVec(1.0F);
        Vec3d cameraPos = cameraEntity.getCameraPosVec(1.0F);
        Vec3d cameraEnd = cameraPos.add(cameraDir.multiply(4));
        RaycastContext raycastContext = new RaycastContext(cameraPos, cameraEnd, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player);
        assert client.world != null;
        return client.world.raycast(raycastContext).getPos();
    }

    private static boolean isOnFrontFace(ChiseledBookshelfBlockEntity blockEntity, Vec3d relPos) {
        World world = blockEntity.getWorld();
        if (world == null) return false;
        BlockState state = world.getBlockState(blockEntity.getPos());
        if(isNotChiseledBookshelf(state)) return false;
        Direction facing = state.get(Properties.HORIZONTAL_FACING).getOpposite();
        boolean isFrontFace = false;
        if (facing == Direction.NORTH && relPos.z > 0.99) {
            isFrontFace = true;
        } else if (facing == Direction.SOUTH && relPos.z < 0.01) {
            isFrontFace = true;
        } else if (facing == Direction.EAST && relPos.x < 0.01) {
            isFrontFace = true;
        } else if (facing == Direction.WEST && relPos.x > 0.99) {
            isFrontFace = true;
        }
        return isFrontFace;
    }

    private static int getRelativeX(ChiseledBookshelfBlockEntity blockEntity, Vec3d relPos) {
        World world = blockEntity.getWorld();
        if (world == null) return -1;
        BlockState state = world.getBlockState(blockEntity.getPos());
        if(isNotChiseledBookshelf(state)) return -1;
        Direction facing = state.get(Properties.HORIZONTAL_FACING).getOpposite();
        int x = 0;
        if (facing == Direction.NORTH && relPos.z > 0.99) {
            x = (int) (relPos.x * 16);
        } else if (facing == Direction.SOUTH && relPos.z < 0.01) {
            x = 16 - (int) (relPos.x * 16);
        } else if (facing == Direction.EAST && relPos.x < 0.01) {
            x = (int) (relPos.z * 16);
        } else if (facing == Direction.WEST && relPos.x > 0.99) {
            x = 16 - (int) (relPos.z * 16);
        }
        return x;
    }

    private static int getRelativeY(Vec3d relPos) {
        return  (int) (relPos.y * 16);
    }

    public static float getRotation(Direction rotation) {
        return switch (rotation) {
            case WEST -> 90;
            case SOUTH -> 180;
            case EAST -> 270;
            default -> 0;
        };
    }
}
