package com.bawnorton.betterbookshelves.util;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IChiseledBookshelfBlockEntity {
    ItemStack getBook(int index);
    void setBook(int index, ItemStack stack);
    List<ItemStack> getBooks();
    int getBinaryRepresentation();
}
