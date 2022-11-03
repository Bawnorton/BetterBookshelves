package com.bawnorton.betterbookshelves.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {
    private static boolean isNotChiseledBookshelf(BlockState blockState) {
        return blockState.getBlock() != Blocks.CHISELED_BOOKSHELF;
    }

    public static Book getLookingAtBook(ChiseledBookshelfBlockEntity entity) {
        assert MinecraftClient.getInstance().player != null;
        Vec3d relPos = Helper.getLookOnBlockCoords(MinecraftClient.getInstance().player, entity);
        if(relPos == null) return Book.NONE;
        boolean isFrontFace = Helper.isOnFrontFace(entity, relPos);
        if (!isFrontFace) return Book.NONE;
        int x = Helper.getRelativeX(entity, relPos);
        int y = Helper.getRelativeY(relPos);
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
