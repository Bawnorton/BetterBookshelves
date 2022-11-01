package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.Config;
import com.bawnorton.betterbookshelves.config.ConfigManager;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetterBookshelves implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("betterbookshelves");
	public static final Map<BlockPos, List<ItemStack>> BOOKSHELVES = new HashMap<>();
	public static Config CONFIG = new Config();
	@Override
	public void onInitialize() {
		LOGGER.info("Loaded BetterBookshelves");
		ConfigManager.loadConfig();
	}
}

