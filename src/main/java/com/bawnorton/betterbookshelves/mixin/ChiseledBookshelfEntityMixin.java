package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

@Mixin(ChiseledBookshelfBlockEntity.class)
public class ChiseledBookshelfEntityMixin {
    @Shadow @Final private Deque<ItemStack> books;

    @Inject(method = "getLastBook", at=@At("HEAD"), cancellable = true)
    private void getLastBook(CallbackInfoReturnable<ItemStack> cir) {
        if(BetterBookshelves.requestIndex != -1) {
            ItemStack[] items = books.toArray(ItemStack[]::new);
            try {
                ItemStack book = items[BetterBookshelves.requestIndex];
                cir.setReturnValue(book);
            } catch (ArrayIndexOutOfBoundsException e) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
            BetterBookshelves.requestIndex = -1;
        }
    }
}
