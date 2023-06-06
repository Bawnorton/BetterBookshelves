package com.bawnorton.betterbookshelves.render;

import com.bawnorton.betterbookshelves.config.client.Config;
import com.bawnorton.betterbookshelves.util.Book;
import com.bawnorton.betterbookshelves.util.PlayerLookHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.joml.Matrix4f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ChiseledBookshelfBlockEntityRenderer implements BlockEntityRenderer<ChiseledBookshelfBlockEntity> {
    public ChiseledBookshelfBlockEntityRenderer() {
    }

    private void renderText(ChiseledBookshelfBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        MinecraftClient client = MinecraftClient.getInstance();
        Pair<Book, ItemStack> book = PlayerLookHelper.getLookingAtBook(entity);
        Book bookToRender = book.getLeft();
        ItemStack stack = book.getRight();
        if (bookToRender == Book.NONE || stack == ItemStack.EMPTY) return;

        TextRenderer textRenderer = client.textRenderer;
        List<Text> displayText = PlayerLookHelper.getBookText(stack);
        int textSize = Config.getInstance().textSize;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(entity.getCachedState().get(HorizontalFacingBlock.FACING).getRotationQuaternion().rotateX((float) Math.PI / 2));
        matrices.translate((bookToRender.x2 + bookToRender.x1) / 32.0 - 0.5, 0.5 - (bookToRender.y2 + bookToRender.y1) / 32.0, -0.6);
        matrices.scale(textSize / 400.0f, textSize / 400.0f, textSize / 400.0f);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float g = client.options.getTextBackgroundOpacity(0f);
        int j = (int) (g * 255.0F) << 24;
        int y = 0;
        for (Text text : displayText) {
            float h = (float) (-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, y, stack.getItem() == Items.ENCHANTED_BOOK ? 16777045 : 16777215, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, j, 15728880);
            y += 10;
        }
        matrices.pop();
    }

    @Override
    public void render(ChiseledBookshelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (Config.getInstance().textPreview == Config.TextPreview.ON) {
            renderText(entity, matrices, vertexConsumers);
        }
    }
}
