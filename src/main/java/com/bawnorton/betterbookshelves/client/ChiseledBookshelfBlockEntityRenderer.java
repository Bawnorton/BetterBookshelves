package com.bawnorton.betterbookshelves.client;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.util.Book;
import com.bawnorton.betterbookshelves.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChiseledBookshelfBlockEntityRenderer implements BlockEntityRenderer<ChiseledBookshelfBlockEntity> {
    public ChiseledBookshelfBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(ChiseledBookshelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Book bookToRender = getRenderBook(entity);
        if(bookToRender != Book.NONE) {
            int bookIndex = bookToRender.index();
            ItemStack book = BetterBookshelves.bookshelves.get(entity.getPos()).get(bookIndex);
            if(book == ItemStack.EMPTY) return;
            assert entity.getWorld() != null;
            BlockState state = entity.getWorld().getBlockState(entity.getPos());
            if(!state.isOf(Blocks.CHISELED_BOOKSHELF)) return;
            Direction rotation = state.get(Properties.HORIZONTAL_FACING);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            List<Text> displayText = new ArrayList<>();
            displayText.add(book.getName());
            if(book.getItem() == Items.ENCHANTED_BOOK) {
                NbtCompound tag = book.getNbt();
                if(tag != null && tag.contains("StoredEnchantments")) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.fromNbt(tag.getList("StoredEnchantments", 10));
                    for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        displayText.add(entry.getKey().getName(entry.getValue()));
                    }
                }
            }
            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);
            matrices.multiply(new Quaternion(0, VectorHelper.getRotation(rotation), 0, true));
            matrices.translate((32 - (bookToRender.x2 + bookToRender.x1)) / 32.0 - 0.5, (bookToRender.y2 + bookToRender.y1) / 32.0 - 0.5, -0.6);
            matrices.scale(-0.025f, -0.025f, 0.025f);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0f);
            int j = (int)(g * 255.0F) << 24;
            int y = 0;
            for(Text text: displayText) {
                float h = (float)(-textRenderer.getWidth(text) / 2);
                textRenderer.draw(text, h, y, book.getItem() == Items.ENCHANTED_BOOK ? 16777045 : 16777215, false, matrix4f, vertexConsumers, true, j, light);
                y += 10;
            }
            matrices.pop();
        }
    }

    private Book getRenderBook(ChiseledBookshelfBlockEntity entity) {
        assert MinecraftClient.getInstance().player != null;
        Vec3d relPos = VectorHelper.getLookOnBlockCoords(MinecraftClient.getInstance().player, entity);
        if(relPos == null) return Book.NONE;
        boolean isFrontFace = VectorHelper.isOnFrontFace(entity, relPos);
        if (!isFrontFace) return Book.NONE;
        int x = VectorHelper.getRelativeX(entity, relPos);
        int y = VectorHelper.getRelativeY(relPos);
        return Book.getBook(x, y);
    }
}
