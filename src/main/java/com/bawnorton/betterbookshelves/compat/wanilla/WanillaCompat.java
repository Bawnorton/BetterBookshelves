package com.bawnorton.betterbookshelves.compat.wanilla;

import com.bawnorton.betterbookshelves.reflection.Reflection;
import net.kas.wanilla.block.ChiseledBookshelfBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class WanillaCompat {
    public static boolean isChiseledBookself(BlockState blockState) {
        for(Block block: Reflection.getFields(ChiseledBookshelfBlocks.class, Block.class)) {
            if (blockState.isOf(block)) {
                return true;
            }
        }
        return false;
    }
}
