package com.bawnorton.betterbookshelves.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class VectorHelper {
    private static boolean isNotChiseledBookshelf(BlockState blockState) {
        return blockState.getBlock() != Blocks.CHISELED_BOOKSHELF;
    }

    @Nullable
    public static Vec3d getLookOnBlockCoords(PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
        if (player.squaredDistanceTo(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ()) > 25) return null;
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d cameraDir = client.getCameraEntity().getRotationVec(1.0F);
        Vec3d cameraPos = client.getCameraEntity().getCameraPosVec(1.0F);
        Vec3d cameraEnd = cameraPos.add(cameraDir.multiply(4));
        RaycastContext raycastContext = new RaycastContext(cameraPos, cameraEnd, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player);
        Vec3d hitPos = client.world.raycast(raycastContext).getPos();
        return hitPos.subtract(Vec3d.of(blockEntity.getPos()));
    }

    public static boolean isOnFrontFace(ChiseledBookshelfBlockEntity blockEntity, Vec3d relPos) {
        BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
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

    public static int getRelativeX(ChiseledBookshelfBlockEntity blockEntity, Vec3d relPos) {
        BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
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

    public static int getRelativeY(Vec3d relPos) {
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
