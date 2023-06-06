package com.bawnorton.betterbookshelves.compat.client;

import net.fabricmc.loader.api.FabricLoader;

public class Compat {
    public static boolean isYACLLoaded() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}
