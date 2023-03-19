package com.bawnorton.betterbookshelves.compat;

import net.fabricmc.loader.api.FabricLoader;

public class Compat {
    public static boolean isWanillaLoaded() {
        return FabricLoader.getInstance().isModLoaded("wanilla");
    }

    public static boolean isYACLLoaded() {
        return FabricLoader.getInstance().isModLoaded("yet-another-config-lib");
    }

    public static String getYACLVersion() {
        if (!isYACLLoaded()) return "";
        return FabricLoader.getInstance().getModContainer("yet-another-config-lib").get().getMetadata().getVersion().getFriendlyString();
    }
}
