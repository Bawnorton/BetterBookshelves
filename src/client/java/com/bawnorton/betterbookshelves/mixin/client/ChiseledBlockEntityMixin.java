package com.bawnorton.betterbookshelves.mixin.client;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBlockEntityMixin implements RenderAttachmentBlockEntity {
    @Shadow public abstract ItemStack getStack(int slot);

    @Override
    public @Nullable Object getRenderAttachmentData() {
        return List.of(getStack(0), getStack(1), getStack(2), getStack(3), getStack(4), getStack(5));
    }
}
