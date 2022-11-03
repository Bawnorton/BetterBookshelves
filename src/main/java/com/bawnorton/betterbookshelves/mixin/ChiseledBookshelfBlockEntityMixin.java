package com.bawnorton.betterbookshelves.mixin;

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin {

    @Inject(method = "getLastBook", at=@At("HEAD"))
    private void getLastBook(CallbackInfoReturnable<ItemStack> cir) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "addBook", at=@At("HEAD"))
    private void addBook(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        throw new UnsupportedOperationException();
    }
}
