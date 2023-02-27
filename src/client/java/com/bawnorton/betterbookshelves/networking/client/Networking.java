package com.bawnorton.betterbookshelves.networking.client;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Networking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(BetterBookshelves.MOD_ID, "update_block"), (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            client.execute(() -> {
                assert client.world != null;
                client.world.updateListeners(pos, client.world.getBlockState(pos), client.world.getBlockState(pos), Block.NOTIFY_LISTENERS);
            });
        });
    }
}
