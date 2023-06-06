package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.config.ServerConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
public abstract class EnchantingTableBlockMixin {

    @Inject(method = "canAccessPowerProvider", at = @At("RETURN"), cancellable = true)
    private static void canAccessBookshelf(World world, BlockPos tablePos, BlockPos bookshelfOffset, CallbackInfoReturnable<Boolean> cir) {
        if (ServerConfig.getInstance().enchantingTableBookRequirement == -1) return;
        if(cir.getReturnValueZ()) return;
        BlockState blockState = world.getBlockState(tablePos.add(bookshelfOffset));
        boolean isChiseledBookshelf = blockState.isOf(Blocks.CHISELED_BOOKSHELF) && world.isAir(tablePos.add(bookshelfOffset.getX() / 2, bookshelfOffset.getY(), bookshelfOffset.getZ() / 2));
        if(isChiseledBookshelf) {
            Direction facing = blockState.get(Properties.HORIZONTAL_FACING);
            BlockPos chiseledBookshelfPos = tablePos.add(bookshelfOffset);
            int deltaZ = chiseledBookshelfPos.getZ() - tablePos.getZ();
            int deltaX = chiseledBookshelfPos.getX() - tablePos.getX();
            int deltaY = chiseledBookshelfPos.getY() - tablePos.getY();
            boolean validFacing = switch (facing) {
                case DOWN, UP -> false;
                case NORTH -> deltaZ == 2 && Math.abs(deltaX) <= 1;
                case SOUTH -> deltaZ == -2 && Math.abs(deltaX) <= 1;
                case EAST -> deltaX == -2 && Math.abs(deltaZ) <= 1;
                case WEST -> deltaX == 2 && Math.abs(deltaZ) <= 1;
            } && deltaY <= 1 && deltaY >= 0;
            BlockEntity blockEntity = world.getBlockEntity(chiseledBookshelfPos);
            if(blockEntity instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity) {
                int numBooks = chiseledBookshelfBlockEntity.getOpenSlotCount();
                boolean hasEnoughBooks = numBooks >= ServerConfig.getInstance().enchantingTableBookRequirement;
                cir.setReturnValue(validFacing && hasEnoughBooks);
            }
        }
    }
}
