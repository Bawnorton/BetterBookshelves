package com.bawnorton.betterbookshelves.mixin.client;

import com.bawnorton.betterbookshelves.config.Config;
import com.bawnorton.betterbookshelves.util.PlayerLookHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        if(Config.getInstance().textPreview != Config.TextPreview.UNDER_CROSSHAIR) return;
        MinecraftClient client = MinecraftClient.getInstance();

        ItemStack book = PlayerLookHelper.getLookingAtBook(null).getRight();
        if(book == ItemStack.EMPTY) return;

        List<Text> display = PlayerLookHelper.getBookText(book);
        for(int i = 0; i < display.size(); i++) {
            Text text = display.get(i);
            client.textRenderer.draw(matrices, text, (int)(this.scaledWidth / 2.0 - client.textRenderer.getWidth(text) / 2), (int)(this.scaledHeight / 2.0 + 15 + (i * 10)), book.getItem() == Items.ENCHANTED_BOOK ? 16777045 : 16777215);
        }
        RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
    }
}
