package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.access.ChiseledBookshelfBlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {

    @Inject(method = "canAccessBookshelf", at = @At("RETURN"), cancellable = true)
    private static void canAccessBookshelf(World world, BlockPos tablePos, BlockPos bookshelfOffset, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(tablePos.add(bookshelfOffset));
        boolean isBookshelf = cir.getReturnValueZ();
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
            AtomicInteger count = new AtomicInteger();
            ((ChiseledBookshelfBlockEntityAccess) world.getBlockEntity(chiseledBookshelfPos)).getBooks().forEach(book -> { if(book != ItemStack.EMPTY) count.getAndIncrement(); });
            cir.setReturnValue(validFacing && count.get() >= 3);
        } else {
            cir.setReturnValue(isBookshelf);
        }
    }
}
