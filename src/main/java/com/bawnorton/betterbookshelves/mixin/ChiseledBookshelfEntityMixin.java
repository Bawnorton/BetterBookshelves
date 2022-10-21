package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Hashtable;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

@Mixin(ChiseledBookshelfBlockEntity.class)
public class ChiseledBookshelfEntityMixin {
    private static final Hashtable<ChiseledBookshelfBlockEntity, DefaultedList<ItemStack>> bookWrapper = new Hashtable<>();

    private DefaultedList<ItemStack> getBookList() {
        DefaultedList<ItemStack> defaultedList = bookWrapper.get(((ChiseledBookshelfBlockEntity)(Object)this));
        if (defaultedList == null) {
            defaultedList = DefaultedList.ofSize(6, ItemStack.EMPTY);
            bookWrapper.put(((ChiseledBookshelfBlockEntity)(Object)this), defaultedList);
        }
        return defaultedList;
    }

    @Inject(method = "getLastBook", at=@At("HEAD"), cancellable = true)
    private void getLastBook(CallbackInfoReturnable<ItemStack> cir) {
        if(BetterBookshelves.requestIndex != -1) {
            try {
                ItemStack book = getBookList().set(BetterBookshelves.requestIndex, ItemStack.EMPTY);
                cir.setReturnValue(book);
            } catch (ArrayIndexOutOfBoundsException e) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
            BetterBookshelves.requestIndex = -1;
        }
    }

    @Inject(method = "addBook", at=@At("HEAD"), cancellable = true)
    private void addBook(ItemStack stack, CallbackInfo ci) {
        if(stack.isIn(ItemTags.BOOKSHELF_BOOKS)) {
            if(getBookList().get(BetterBookshelves.requestIndex) == ItemStack.EMPTY) {
                getBookList().set(BetterBookshelves.requestIndex, stack);
            }
        }
        ci.cancel();
    }

    @Inject(method = "readNbt", at=@At("HEAD"), cancellable = true)
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        Inventories.readNbt(nbt, getBookList());
        ci.cancel();
    }

    @Inject(method = "writeNbt", at=@At("HEAD"), cancellable = true)
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        Inventories.writeNbt(nbt, getBookList(), true);
        ci.cancel();
    }

    @Inject(method ="toInitialChunkDataNbt", at=@At("HEAD"), cancellable = true)
    private void toInitialChunkDataNbt(CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, getBookList(), true);
        cir.setReturnValue(nbt);
    }

    @Inject(method = "clear", at=@At("HEAD"), cancellable = true)
    public void clear(CallbackInfo ci) {
        getBookList().clear();
        ci.cancel();
    }

    @Inject(method = "getBookCount", at=@At("HEAD"), cancellable = true)
    public void getBookCount(CallbackInfoReturnable<Integer> cir) {
        int count = 0;
        for(ItemStack stack : getBookList()) {
            if(stack != ItemStack.EMPTY) {
                count++;
            }
        }
        cir.setReturnValue(count);
    }

    @Inject(method = "isFull", at=@At("HEAD"), cancellable = true)
    public void isFull(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(((ChiseledBookshelfBlockEntity)(Object)(this)).getBookCount() == 6);
    }

    @Inject(method = "isEmpty", at=@At("HEAD"), cancellable = true)
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(((ChiseledBookshelfBlockEntity)(Object)(this)).getBookCount() == 0);
    }
}
