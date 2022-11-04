package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.config.Config;
import com.bawnorton.betterbookshelves.util.Book;
import com.bawnorton.betterbookshelves.util.Helper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        if(BetterBookshelves.CONFIG.textPreview != Config.TextPreview.UNDER_CROSSHAIR) return;

        MinecraftClient client = MinecraftClient.getInstance();
        BlockEntity lookingAt = getLookingAtBlockEntity();
        if(lookingAt instanceof ChiseledBookshelfBlockEntity blockEntity) {
            Book lookingAtBook = Helper.getLookingAtBook(blockEntity);
            if(lookingAtBook == Book.NONE) return;
            ItemStack book = blockEntity.getStack(lookingAtBook.index());
            if(book == ItemStack.EMPTY) return;
            List<Text> display = Helper.getBookText(book);
            for(int i = 0; i < display.size(); i++) {
                Text text = display.get(i);
                client.textRenderer.draw(matrices, text, (int)(this.scaledWidth / 2.0 - client.textRenderer.getWidth(text) / 2), (int)(this.scaledHeight / 2.0 + 15 + (i * 10)), book.getItem() == Items.ENCHANTED_BOOK ? 16777045 : 16777215);
            }
        }
        RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
    }

    @Nullable
    private BlockEntity getLookingAtBlockEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;
        HitResult target = client.crosshairTarget;
        if(target != null && target.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) target;
            BlockPos pos = blockHitResult.getBlockPos();
            return client.world.getBlockEntity(pos);
        }
        return null;
    }
}
