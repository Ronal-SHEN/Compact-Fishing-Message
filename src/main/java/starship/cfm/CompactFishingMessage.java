package starship.cfm;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starship.cfm.augmentTracker.AugmentTracker;
import starship.cfm.fishMessage.FishMessage;
import starship.cfm.modMenu.ConfigData;
import starship.cfm.modMenu.ConfigScreen;
import starship.cfm.trevorOpener.TrevorOpener;

public class CompactFishingMessage implements ClientModInitializer {
    public static final String MOD_ID = "compact-fishing-message";
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    public static CompactFishingMessage instance;
    public static KeyMapping openConfigKeybind;
    private FishMessage fishmessage;
    private TrevorOpener trevoropener;
    private AugmentTracker augmenttracker;

    public static CompactFishingMessage getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        this.loadConfig();
        this.fishmessage = new FishMessage(this);
        this.trevoropener = new TrevorOpener(this);
        this.augmenttracker = new AugmentTracker(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        openConfigKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.cfm.open_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("cfm", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKeybind.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    client.setScreen(ConfigScreen.buildScreen(CompactFishingMessage.getInstance(), null));
                }
            }
        });
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof ContainerScreen containerScreen) {
                String title = containerScreen.getTitle().getString();
                if (title.contains("INFINIBAG")) {
                    this.trevoropener.detectScreenINFINIBAG();
                    return;
                }

                if (title.contains("SUMMARY")) {
                    this.trevoropener.detectScreenSUMMARYOpen();
                    ScreenEvents.remove(screen).register((screen1) -> {
                        this.trevoropener.detectScreenSUMMARYClose();
                    });
                }

                if (title.contains("FISHING SUPPLIES")) {
                    ScreenEvents.remove(screen).register((screen1) -> {
                        this.augmenttracker.detectScreenFishSupplyClose(screen1);
                    });
                }
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommands.literal("TreasureOpenEvent")
                            .then(ClientCommands.literal("create").executes(context -> {
                                this.trevoropener.eventStart();
                                return 0;
                            }))
                            .then(ClientCommands.literal("stop").executes(context -> {
                                this.trevoropener.eventEnd();
                                return 0;
                            }))
            );
        });

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath("fish-helper", "fish-record-layer"),
                (drawContext, tickDelta) -> {
                    this.fishmessage.recordOverlay.render(drawContext);
                }
        );
        HudElementRegistry.attachElementAfter(
                Identifier.fromNamespaceAndPath("fish-helper", "fish-record-layer"),
                Identifier.fromNamespaceAndPath("fish-helper", "fish-augment-layer"),
                (drawContext, tickDelta) -> {
                    this.augmenttracker.render(drawContext);
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ConfigData.getInstance().didInfoShowOnce && client.player != null && client.player.tickCount == 20) {
                ConfigData.getInstance().didInfoShowOnce = true;
                saveConfig();
                Component text = Component.empty()
                        .append(Component.literal("").setStyle(
                                Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))
                        ))
                        .append(Component.literal(" Hi! Thanks for using MCCI Compact Fishing Messages!")
                                .setStyle(Style.EMPTY.withColor(0xCAD0E8)))
                        .append(Component.literal(" (This message will only show up once.) ")
                                .setStyle(Style.EMPTY.withColor(0xD8D8D8).withItalic(true)))
                        .append(Component.literal("All settings can be customized via Mod Menu or by pressing K ")
                                .setStyle(Style.EMPTY.withColor(0xFFDCD1).withBold(true)))
                        .append(Component.literal("Thanks for support again <3. I will (hopefully) update more in the future... Enjoy!")
                                .setStyle(Style.EMPTY.withColor(0xCAD0E8)));

                client.player.sendSystemMessage(text);
            }
        });
    }

    public void tick(Minecraft client) {
        this.fishmessage.tick(client);
        this.trevoropener.tick(client);
        this.augmenttracker.tick(client);
    }

    public void loadConfig() {
        ConfigData.load();
    }

    public void saveConfig() {
        ConfigData.save();
    }

    public ConfigData getConfig() {
        return ConfigData.getInstance();
    }

    // TODO: open config file button
    // TODO: rename
}
