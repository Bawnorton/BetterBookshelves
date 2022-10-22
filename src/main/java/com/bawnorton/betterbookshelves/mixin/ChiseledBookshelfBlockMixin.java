package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.util.Book;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {
    @Shadow @Final public static IntProperty BOOKS_STORED;
    @Shadow @Final public static IntProperty LAST_INTERACTION_BOOK_SLOT;

    @ModifyArg(method = "<init>", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/BlockWithEntity;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings modifySettings(AbstractBlock.Settings settings) {
        return settings.nonOpaque();
    }

    @Redirect(method = "onUse", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/ChiseledBookshelfBlock;tryRemoveBook(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult tryRemoveLookingAtBook(BlockState state, World world, BlockPos pos, PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
        if (!blockEntity.isEmpty()) {
            updateLookingAtIndex((ServerPlayerEntity) player, blockEntity);
            ItemStack itemStack = blockEntity.getLastBook();
            LOGGER.info("Looking at book: " + itemStack.getNbt());
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            int i = blockEntity.getBookCount();
            world.setBlockState(pos, state.with(BOOKS_STORED, i).with(LAST_INTERACTION_BOOK_SLOT, i + 1), 3);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            if(!player.getInventory().insertStack(itemStack)) {
                player.dropItem(itemStack, false);
            }
        }
        return ActionResult.CONSUME;
    }

    @Redirect(method = "onUse", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/ChiseledBookshelfBlock;tryAddBook(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult tryAddLookingAtBook(BlockState state, World world, BlockPos pos, PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity, ItemStack stack) {
        if (!blockEntity.isFull()) {
            updateLookingAtIndex((ServerPlayerEntity) player, blockEntity);
            blockEntity.addBook(stack.split(1));
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if(player.isCreative()) {
                stack.increment(1);
            }
            int i = blockEntity.getBookCount();
            world.setBlockState(pos, state.with(BOOKS_STORED, i).with(LAST_INTERACTION_BOOK_SLOT, i), 3);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        return ActionResult.CONSUME;
    }

    private void updateLookingAtIndex(ServerPlayerEntity player, ChiseledBookshelfBlockEntity blockEntity) {
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
        if (isFrontFace) {
            Book book = Book.getBook(x, y);
            BetterBookshelves.requestIndex = book.index();
        }
    }
}
