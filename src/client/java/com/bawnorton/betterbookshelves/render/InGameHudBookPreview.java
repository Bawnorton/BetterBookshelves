package com.bawnorton.betterbookshelves.render;

import com.bawnorton.betterbookshelves.config.client.Config;
import com.bawnorton.betterbookshelves.util.PlayerLookHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class InGameHudBookPreview {
    public static void renderCrosshair(DrawContext context, @SuppressWarnings("unused") float tickDelta) {
        if(Config.getInstance().textPreview == Config.TextPreview.UNDER_CROSSHAIR) {
            MinecraftClient client = MinecraftClient.getInstance();

            ItemStack book = PlayerLookHelper.getLookingAtBook(null).getRight();
            if(book == ItemStack.EMPTY) return;

            List<Text> display = PlayerLookHelper.getBookText(book);
            for(int i = 0; i < display.size(); i++) {
                Text text = display.get(i);
                context.drawText(client.textRenderer, text, (int)(client.getWindow().getScaledWidth() / 2.0 - client.textRenderer.getWidth(text) / 2), (int)(client.getWindow().getScaledHeight() / 2.0 + 15 + (i * 10)), book.getItem() == Items.ENCHANTED_BOOK ? 16777045 : 16777215, false);
            }
        }
    }
}
