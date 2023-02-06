package com.bawnorton.betterbookshelves.state.property;

import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

public class BetterBookshelvesProperties {
    public static final IntProperty LAST_BOOK_TYPE = IntProperty.of("last_book_type", 0, 4);
    public static final IntProperty LAST_INTERACTION_BOOK_SLOT = IntProperty.of("last_interaction_book_slot", 0, 5);
}
