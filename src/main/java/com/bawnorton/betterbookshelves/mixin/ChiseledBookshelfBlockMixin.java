package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.config.Config;
import com.bawnorton.betterbookshelves.state.property.BetterBookshelvesProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin {
    @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
    private BlockState init(BlockState state) {
        return state.with(BetterBookshelvesProperties.LAST_BOOK_TYPE, 0);
    }

    @Inject(method = "appendProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager$Builder;add([Lnet/minecraft/state/property/Property;)Lnet/minecraft/state/StateManager$Builder;", ordinal = 1, shift = At.Shift.AFTER))
    private void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BetterBookshelvesProperties.LAST_BOOK_TYPE);
    }

    @Inject(method = "getComparatorOutput", at = @At("RETURN"), cancellable = true)
    private void getComparatorOutput(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(Config.getInstance().bookTypeComparatorOutput) {
            cir.setReturnValue(state.get(BetterBookshelvesProperties.LAST_BOOK_TYPE));
        }
    }
}
