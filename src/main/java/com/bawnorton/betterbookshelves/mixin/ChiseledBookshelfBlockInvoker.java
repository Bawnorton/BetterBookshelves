package com.bawnorton.betterbookshelves.mixin;

import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(ChiseledBookshelfBlock.class)
public interface ChiseledBookshelfBlockInvoker {
    @SuppressWarnings("unused")
    @Invoker("getHitPos")
    static Optional<Vec2f> getHitPos(BlockHitResult hit, Direction facing) {
        throw new IllegalStateException("Mixin invoker failed to apply");
    }

    @SuppressWarnings("unused")
    @Invoker("getSlotForHitPos")
    static int getSlotForHitPos(Vec2f hitPos) {
        throw new IllegalStateException("Mixin invoker failed to apply");
    }
}
