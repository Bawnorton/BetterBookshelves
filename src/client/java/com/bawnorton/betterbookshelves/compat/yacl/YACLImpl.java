package com.bawnorton.betterbookshelves.compat.yacl;

import com.bawnorton.betterbookshelves.config.client.Config;
import com.bawnorton.betterbookshelves.config.client.ConfigManager;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class YACLImpl {
    public static Screen getScreen() {
        Option<Integer> textSize = Option.createBuilder(int.class)
                .name(Text.literal("Text Size"))
                .tooltip(Text.literal("The type of search to use when searching for blocks"))
                .binding(10, () -> Config.getInstance().textSize, (value) -> Config.getInstance().textSize = value)
                .controller(option -> new IntegerSliderController(option, 5, 20, 1))
                .available(Config.getInstance().textPreview != Config.TextPreview.UNDER_CROSSHAIR)
                .build();
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Better Bookshelves Client Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .tooltip(Text.literal("General settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Main"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Per Book Texture"))
                                        .tooltip(Text.literal("Enable to use a different texture for each book rather than each slot"))
                                        .binding(true, () -> Config.getInstance().perBookTexture, (value) -> Config.getInstance().perBookTexture = value)
                                        .controller(TickBoxController::new)
                                        .build())
                                .option(Option.createBuilder(Config.TextPreview.class)
                                        .name(Text.literal("Text Preview"))
                                        .tooltip(Text.literal("The type of text preview to use"))
                                        .binding(Config.TextPreview.ON, () -> Config.getInstance().textPreview, (value) -> {
                                            Config.getInstance().textPreview = value;
                                            textSize.setAvailable(value != Config.TextPreview.UNDER_CROSSHAIR);
                                        })
                                        .controller(EnumController::new)
                                        .build())
                                .option(textSize)
                                .build())
                        .build())
                .save(ConfigManager::saveConfig)
                .build()
                .generateScreen(null);
    }
}
