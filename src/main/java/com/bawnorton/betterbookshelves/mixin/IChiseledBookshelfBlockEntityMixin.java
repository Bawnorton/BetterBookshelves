package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.access.IChiseledBookshelfBlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class IChiseledBookshelfBlockEntityMixin implements IChiseledBookshelfBlockEntityAccess {

    private List<ItemStack> books;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(BlockPos pos, BlockState state, CallbackInfo ci) {
        books = new ArrayList<>();
        for(int i = 0; i < 6; i++) books.add(ItemStack.EMPTY);
        update();
    }

    private ChiseledBookshelfBlockEntity ths() {
        return (ChiseledBookshelfBlockEntity) (Object) this;
    }

    public void update() {
        BetterBookshelves.BOOKSHELVES.put(ths().getPos(), books);
    }

    @Override
    public ItemStack getBook(int index) {
        ItemStack result = books.set(index, ItemStack.EMPTY);
        update();
        return result;
    }

    @Override
    public boolean setBook(int index, ItemStack stack) {
        if(index < 0 || index > 5 || stack == ItemStack.EMPTY) return false;
        if(stack.isIn(ItemTags.BOOKSHELF_BOOKS)) {
            if(books.get(index) == ItemStack.EMPTY) {
                books.set(index, stack);
                update();
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getBooks() {
        return books;
    }

    @Inject(method = "getLastBook", at=@At("HEAD"))
    private void getLastBook(CallbackInfoReturnable<ItemStack> cir) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "addBook", at=@At("HEAD"))
    private void addBook(ItemStack stack, CallbackInfo ci) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "readNbt", at=@At("HEAD"), cancellable = true)
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        DefaultedList<ItemStack> bookList = DefaultedList.ofSize(6, ItemStack.EMPTY);
        Inventories.readNbt(nbt, bookList);
        books.clear();
        books.addAll(bookList);
        update();
        ci.cancel();
    }

    @Inject(method = "writeNbt", at=@At("HEAD"), cancellable = true)
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(6);
        defaultedList.addAll(books);
        Inventories.writeNbt(nbt, defaultedList, true);
        update();
        ci.cancel();
    }

    @Inject(method ="toInitialChunkDataNbt", at=@At("HEAD"), cancellable = true)
    private void toInitialChunkDataNbt(CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound nbt = new NbtCompound();
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(6);
        defaultedList.addAll(books);
        Inventories.writeNbt(nbt, defaultedList, true);
        update();
        cir.setReturnValue(nbt);
    }

    @Inject(method = "clear", at=@At("HEAD"), cancellable = true)
    public void clear(CallbackInfo ci) {
        for(int i = 0; i < 6; i++) {
            books.set(i, ItemStack.EMPTY);
        }
        ci.cancel();
    }

    @Inject(method = "getBookCount", at=@At("HEAD"), cancellable = true)
    public void getBookCount(CallbackInfoReturnable<Integer> cir) {
        int count = 0;
        for (ItemStack book : books) {
            if(book != ItemStack.EMPTY) count++;
        }
        cir.setReturnValue(count);
    }

    @Inject(method = "isFull", at=@At("HEAD"), cancellable = true)
    public void isFull(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(ths().getBookCount() == 6);
    }

    @Inject(method = "isEmpty", at=@At("HEAD"), cancellable = true)
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(ths().getBookCount() == 0);
    }

}
