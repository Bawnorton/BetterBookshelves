package com.bawnorton.betterbookshelves.util;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IChiseledBookshelfBlockEntity {
    ItemStack getBook(int index);
    boolean setBook(int index, ItemStack stack);
    List<ItemStack> getBooks();
    void update();
}
