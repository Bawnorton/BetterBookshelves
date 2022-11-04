package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.util.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    private static void updateState(ChiseledBookshelfBlockEntity instance, int slot) {
        List<ItemStack> inventory = Helper.getInventory(instance);
        int count = 0;
        for(ItemStack stack : inventory) if(!stack.isEmpty()) count++;
        World world = instance.getWorld();
        if(world != null) {
            BlockState newState = instance.getCachedState().with(Properties.BOOKS_STORED, count).with(Properties.LAST_INTERACTION_BOOK_SLOT, instance.size() - slot);
            world.setBlockState(instance.getPos(), newState, Block.NOTIFY_ALL);
        }
    }

    @Redirect(method = "tryRemoveBook", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;getLastBook()Lnet/minecraft/item/ItemStack;"))
    private static ItemStack tryRemoveBook(ChiseledBookshelfBlockEntity instance) {
        int slot = Helper.getLookingAtBook(instance).index();
        if(slot == -1) return ItemStack.EMPTY;
        ItemStack item = instance.getStack(slot);
        instance.setStack(slot, ItemStack.EMPTY);
        updateState(instance, slot);
        return item;
    }

    @Redirect(method = "tryAddBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;addBook(Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean addBook(ChiseledBookshelfBlockEntity instance, ItemStack stack) {
        int slot = Helper.getLookingAtBook(instance).index();
        if(instance.getStack(slot) == ItemStack.EMPTY && slot != -1) {
            instance.setStack(slot, stack.split(1));
            updateState(instance, slot);
            return true;
        }
        return false;
    }
}
