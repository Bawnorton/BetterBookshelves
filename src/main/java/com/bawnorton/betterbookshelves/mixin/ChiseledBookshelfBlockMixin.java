package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.config.Config;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin {

    @Inject(method = "getComparatorOutput", at = @At("RETURN"), cancellable = true)
    private void getComparatorOutput(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(Config.getInstance().bookTypeComparatorOutput) {
            if(world.getBlockEntity(pos) instanceof ChiseledBookshelfBlockEntity blockEntity) {
                int slot = blockEntity.getLastInteractedSlot();
                cir.setReturnValue(switch(blockEntity.getStack(slot).getItem().toString()) {
                    case "book" -> 1;
                    case "writable_book" -> 2;
                    case "written_book" -> 3;
                    case "enchanted_book" -> 4;
                    default -> 0;
                });
            }
        }
    }
}
