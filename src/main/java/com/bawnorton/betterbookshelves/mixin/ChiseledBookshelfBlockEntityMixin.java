package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.util.Helper;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        Helper.updateCache((ChiseledBookshelfBlockEntity) (Object) this);
    }

    @Inject(method = "updateState", at = @At("TAIL"))
    private void updateState(CallbackInfo ci) {
        Helper.updateCache((ChiseledBookshelfBlockEntity) (Object) this);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbt(CallbackInfo ci) {
        Helper.updateCache((ChiseledBookshelfBlockEntity) (Object) this);
    }
}
