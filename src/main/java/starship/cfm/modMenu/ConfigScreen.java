package starship.cfm.modMenu;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import starship.cfm.CompactFishingMessage;

import java.util.List;


public class ConfigScreen {

    public static Screen buildScreen(CompactFishingMessage cfm, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.cfm.config"));

        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.cfm.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigData config = cfm.getConfig();

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.cfm.compact_fishmsg"), config.enableCompactFishmsg)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableCompactFishmsg = newValue)
                .setTooltip(Text.translatable("tooltip.cfm.compact_fishmsg"))
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.cfm.trevor_opener"), config.enableTreasureReciMsg)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.enableTreasureReciMsg = newValue)
                .setTooltip(Text.translatable("tooltip.cfm.trevor_opener"))
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.cfm.record_overlay"), config.enableFishRecordOverlay)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableFishRecordOverlay = newValue)
                .setTooltip(Text.translatable("tooltip.cfm.record_overlay"))
                .build());

        Window window = MinecraftClient.getInstance().getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();

        category.addEntry(entryBuilder.startSubCategory(
                Text.translatable("group.cfm.render_settings"),
                List.of(
                        entryBuilder.startIntSlider(Text.translatable("option.cfm.render_x"), config.fishRecordRenderTextX, 0, scaledWidth)
                                .setDefaultValue(10)
                                .setSaveConsumer(newValue -> config.fishRecordRenderTextX = newValue)
                                .build(),
                        entryBuilder.startIntSlider(Text.translatable("option.cfm.render_y"), config.fishRecordRenderTextY, 0, scaledHeight)
                                .setDefaultValue(10)
                                .setSaveConsumer(newValue -> config.fishRecordRenderTextY = newValue)
                                .build(),
                        entryBuilder.startFloatField(Text.translatable("option.cfm.font_size"), config.fishRecordRenderScale)
                                .setDefaultValue(1.0f)
                                .setSaveConsumer(newValue -> config.fishRecordRenderScale = newValue)
                                .build(),
                        entryBuilder.startIntSlider(Text.translatable("option.cfm.bg_color"), config.fishRecordBackgroundAlphaColor, 0, 255)
                                .setDefaultValue(0x88)
                                .setSaveConsumer(newValue -> config.fishRecordBackgroundAlphaColor = newValue)
                                .build(),
                        entryBuilder.startColorField(Text.translatable("option.cfm.text_color"), config.fishRecordTextRGBColor)
                                .setDefaultValue(0xFFFFFF)
                                .setSaveConsumer(newValue -> config.fishRecordTextRGBColor = newValue)
                                .build(),
                        entryBuilder.startBooleanToggle(Text.translatable("option.cfm.cute_icon"), config.fishRecordIconShows)
                                .setDefaultValue(true)
                                .setSaveConsumer(newValue -> config.fishRecordIconShows = newValue)
                                .build(),
                        entryBuilder.startBooleanToggle(Text.translatable("option.cfm.always_shows"), config.fishRecordOverlayAlwaysShows)
                                .setDefaultValue(false)
                                .setSaveConsumer(newValue -> config.fishRecordOverlayAlwaysShows = newValue)
                                .build()
                )).build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.cfm.augment_overlay"), config.enableAugmentOverlay)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableAugmentOverlay = newValue)
                .setTooltip(Text.translatable("tooltip.cfm.augment_overlay"))
                .build());

        builder.setSavingRunnable(cfm::saveConfig);

        return builder.build();
    }

}
