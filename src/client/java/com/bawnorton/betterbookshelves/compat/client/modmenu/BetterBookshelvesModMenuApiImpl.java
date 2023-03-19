package com.bawnorton.betterbookshelves.compat.client.modmenu;

import com.bawnorton.betterbookshelves.config.client.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class BetterBookshelvesModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigManager.getConfigScreen();
    }
}
