package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.util.Helper;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin {

    @Redirect(method = "tryRemoveBook", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;getLastBook()Lnet/minecraft/item/ItemStack;"))
    private static ItemStack tryRemoveBook(ChiseledBookshelfBlockEntity instance) {
        int slot = Helper.getLookingAtBook(instance).index();
        if(slot == -1) return ItemStack.EMPTY;
        return instance.getStack(slot);
    }

    @Redirect(method = "tryAddBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;addBook(Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean addBook(ChiseledBookshelfBlockEntity instance, ItemStack stack) {
        int slot = Helper.getLookingAtBook(instance).index();
        if(instance.getStack(slot) == ItemStack.EMPTY) {
            instance.setStack(slot, stack.split(1));
            return true;
        }
        return false;
    }
}
