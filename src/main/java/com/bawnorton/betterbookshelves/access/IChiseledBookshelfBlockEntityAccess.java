package com.bawnorton.betterbookshelves.access;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IChiseledBookshelfBlockEntityAccess {
    ItemStack getBook(int index);
    boolean setBook(int index, ItemStack stack);
    List<ItemStack> getBooks();
    void update();
}
