package starship.fishhelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starship.fishhelper.augmentTracker.AugmentTracker;
import starship.fishhelper.fishMessage.FishMessage;
import starship.fishhelper.modMenu.ConfigData;
import starship.fishhelper.modMenu.ConfigScreen;
import starship.fishhelper.trevorOpener.TrevorOpener;

public class MCCIFishHelper implements ClientModInitializer {
    public static final String MOD_ID = "compact-fishing-message";
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    public static MCCIFishHelper instance;
    public static KeyBinding openConfigKeybind;
    private FishMessage fishmessage;
    private TrevorOpener trevoropener;
    private AugmentTracker augmenttracker;

    public static MCCIFishHelper getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        logger.info("Initializing MCCIFishHelper");
        instance = this;
        this.loadConfig();
        this.fishmessage = new FishMessage(this);
        this.trevoropener = new TrevorOpener(this);
        this.augmenttracker = new AugmentTracker(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        openConfigKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fishhelper.open_config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.fishhelper"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKeybind.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(ConfigScreen.buildScreen(MCCIFishHelper.getInstance(), null));
                }
            }
        });
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GenericContainerScreen containerScreen) {
                String title = containerScreen.getTitle().getString();
                if (title.contains("INFINIBAG")) {
                    this.trevoropener.detectScreenINFINIBAG();
                    return;
                }

                if (title.contains("SUMMARY")) {
                    this.trevoropener.detectScreenSUMMARYOpen();
                    ScreenEvents.remove(screen).register((screen1) -> {
//                        MCCIFishHelper.logger.info("GUI closed: {}", title);
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
                ClientCommandManager.literal("TreasureOpenEvent")
                    .then(ClientCommandManager.literal("create").executes(context -> {
                        this.trevoropener.eventStart();
                        return 0;
                    }))
                    .then(ClientCommandManager.literal("stop").executes(context -> {
                        this.trevoropener.eventEnd();
                        return 0;
                    }))
            );
        });

        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerAfter(
                IdentifiedLayer.MISC_OVERLAYS,
                Identifier.of("fish-helper", "fish-record-layer"),
                (drawContext, tickCounter) -> {
                    this.fishmessage.recordOverlay.render(drawContext);
                }
            );
            layeredDrawer.attachLayerAfter(
                    Identifier.of("fish-helper", "fish-record-layer"),
                    Identifier.of("fish-helper", "fish-augment-layer"),
                    (drawContext, tickCounter) -> {
                        this.augmenttracker.render(drawContext);
                    }
            );
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ConfigData.getInstance().didInfoShowOnce && client.player != null && client.player.age == 20) {
                ConfigData.getInstance().didInfoShowOnce = true;
                saveConfig();
                Text text = Text.empty()
                    .append(Text.literal("\uE109").setStyle(
                        Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("fish-helper", "icon"))
                    ))
                    .append(Text.literal(" Hi! Thanks for using MCCI Compact Fishing Messages!")
                        .setStyle(Style.EMPTY.withColor(0xCAD0E8)))
                    .append(Text.literal(" (This message will only show up once.) ")
                        .setStyle(Style.EMPTY.withColor(0xD8D8D8).withItalic(true)))
                    .append(Text.literal("All settings can be customized via Mod Menu or by pressing K ")
                        .setStyle(Style.EMPTY.withColor(0xFFDCD1).withBold(true)))
                    .append(Text.literal("Thanks for support again <3. I will (hopefully) update more in the future... Enjoy!")
                        .setStyle(Style.EMPTY.withColor(0xCAD0E8)));

                client.player.sendMessage(text, false);
            }
        });
    }

    public void tick(MinecraftClient client) {
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