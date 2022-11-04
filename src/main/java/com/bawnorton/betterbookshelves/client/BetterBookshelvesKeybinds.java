package com.bawnorton.betterbookshelves.client;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public abstract class BetterBookshelvesKeybinds {
    public static final KeyBinding OPEN_CONFIG = new KeyBinding("key.betterbookshelves.open_config", GLFW.GLFW_KEY_O, "key.categories.misc");
}
