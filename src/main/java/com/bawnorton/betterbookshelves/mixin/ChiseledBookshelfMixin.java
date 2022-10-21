package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.Book;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;
import static net.minecraft.block.ChiseledBookshelfBlock.BOOKS_STORED;
import static net.minecraft.block.ChiseledBookshelfBlock.LAST_INTERACTION_BOOK_SLOT;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfMixin {
    @Redirect(method = "onUse", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/ChiseledBookshelfBlock;tryRemoveBook(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult tryRemoveLookingAtBook(BlockState state, World world, BlockPos pos, PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
        if (!blockEntity.isEmpty()) {
            ItemStack itemStack = getLookingAtBook((ServerPlayerEntity) player, blockEntity);
            LOGGER.info("Looking at book: " + itemStack.getNbt());

        }
        return ActionResult.CONSUME;
    }

    private ItemStack getLookingAtBook(ServerPlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d cameraDir = client.getCameraEntity().getRotationVec(1.0F);
        Vec3d cameraPos = client.getCameraEntity().getCameraPosVec(1.0F);
        Vec3d cameraEnd = cameraPos.add(cameraDir.x * 5, cameraDir.y * 5, cameraDir.z * 5);
        RaycastContext raycastContext = new RaycastContext(cameraPos, cameraEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player);
        Vec3d hitPos = client.world.raycast(raycastContext).getPos();
        Vec3d hitPosRel = hitPos.subtract(Vec3d.of(blockEntity.getPos()));
        // get the facing direction of the bookshelf
        BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
        Direction facing = state.get(Properties.HORIZONTAL_FACING).getOpposite();
        // check if the hit position is on the front face of the bookshelf
        boolean isFrontFace = false;
        int x = -1, y = (int) (hitPosRel.y * 16);
        if (facing == Direction.NORTH && hitPosRel.z > 0.99) {
            isFrontFace = true;
            x = (int) (hitPosRel.x * 16);
        } else if (facing == Direction.SOUTH && hitPosRel.z < 0.01) {
            isFrontFace = true;
            x = 16 - (int) (hitPosRel.x * 16);
        } else if (facing == Direction.EAST && hitPosRel.x < 0.01) {
            isFrontFace = true;
            x = (int) (hitPosRel.z * 16);
        } else if (facing == Direction.WEST && hitPosRel.x > 0.99) {
            isFrontFace = true;
            x = 16 - (int) (hitPosRel.z * 16);
        }
        if (!isFrontFace) {
            return ItemStack.EMPTY;
        }
        Book book = Book.getBook(x, y);
        BetterBookshelves.requestIndex = book.index();
        return blockEntity.getLastBook();
    }
}
