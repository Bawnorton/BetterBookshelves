package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.client.BetterBookshelvesClient;
import com.bawnorton.betterbookshelves.state.property.BetterBookshelvesProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin {

    @Shadow @Final private DefaultedList<ItemStack> inventory;

    @ModifyVariable(method = "updateState", at = @At("STORE"), ordinal = 0)
    private BlockState updateState(BlockState state, int interactedSlot) {
        BetterBookshelvesClient.lastBooks.put(((ChiseledBookshelfBlockEntity)(Object) this).getPos(), new ArrayList<>(){{for(int i = 0; i < ChiseledBookshelfBlockEntity.MAX_BOOKS; i++) add(inventory.get(i));}});
        return state.with(BetterBookshelvesProperties.LAST_BOOK_TYPE, switch(inventory.get(interactedSlot).getItem().toString()) {
            case "book" -> 1;
            case "writable_book" -> 2;
            case "written_book" -> 3;
            case "enchanted_book" -> 4;
            default -> 0;
        });
    }
}
