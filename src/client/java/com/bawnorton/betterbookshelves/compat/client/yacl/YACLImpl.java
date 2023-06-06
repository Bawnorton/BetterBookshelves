package com.bawnorton.betterbookshelves.compat.client.yacl;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import com.bawnorton.betterbookshelves.config.client.Config;
import com.bawnorton.betterbookshelves.config.client.ConfigManager;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class YACLImpl {
    public static Screen getScreen() {
        Option<Integer> textSize = Option.createBuilder(int.class)
                .name(Text.literal("Text Size"))
                .description(option -> OptionDescription.createBuilder()
                        .text(Text.literal("The type of search to use when searching for blocks"),
                                Text.literal("Disabled if Text Preview is set to Under Crosshair"))
                        .image(
                                option >= 15 ? new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/large.png") :
                                option >= 10 ? new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/on.png") :
                                        new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/small.png"),
                                3840,
                                1940)
                        .build())
                .binding(10, () -> Config.getInstance().textSize, (value) -> Config.getInstance().textSize = value)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(5, 20).step(1))
                .available(Config.getInstance().textPreview == Config.TextPreview.ON)
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
                                        .description(option -> OptionDescription.createBuilder()
                                                .text(Text.literal("Enable to use a different texture for each book rather than each slot"))
                                                .image(option ? new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/per_book.png") : new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/per_slot.png"),
                                                        3840,
                                                        1940)
                                                .build())
                                        .binding(true, () -> Config.getInstance().perBookTexture, (value) -> Config.getInstance().perBookTexture = value)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(Config.TextPreview.class)
                                        .name(Text.literal("Text Preview"))
                                        .description(option -> OptionDescription.createBuilder()
                                                .text(Text.literal("The type of text preview to use"))
                                                .image(
                                                        option == Config.TextPreview.ON ? new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/on.png") :
                                                        option == Config.TextPreview.UNDER_CROSSHAIR ? new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/under_crosshair.png") :
                                                                new Identifier(BetterBookshelves.MOD_ID, "config/screenshots/per_book.png"),
                                                        3840,
                                                        1940
                                                )
                                                .build())
                                        .binding(Config.TextPreview.ON, () -> Config.getInstance().textPreview, (value) -> Config.getInstance().textPreview = value)
                                        .listener((option, value) -> textSize.setAvailable(value == Config.TextPreview.ON))
                                        .controller(option -> EnumControllerBuilder.create(option).enumClass(Config.TextPreview.class))
                                        .build())
                                .option(textSize)
                                .build())
                        .build())
                .save(ConfigManager::saveConfig)
                .build()
                .generateScreen(null);
    }
}
