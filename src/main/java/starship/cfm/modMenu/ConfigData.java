package starship.cfm.modMenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/fishhelper.json");
    private static ConfigData instance = new ConfigData();
    public boolean enableTreasureReciMsg = false;
    public boolean enableCompactFishmsg = true;
    public boolean enableFishRecordOverlay = true;
    public int fishRecordRenderTextX = 10;
    public int fishRecordRenderTextY = 10;
    public float fishRecordRenderScale = 1;
    public int fishRecordBackgroundAlphaColor = 0x88;
    public int fishRecordTextRGBColor = 0xFFFFFF;
    public boolean fishRecordIconShows = true;
    public boolean fishRecordOverlayAlwaysShows = false;
    public boolean didInfoShowOnce = false;
    public boolean enableAugmentOverlay = true;

    public static ConfigData getInstance() {
        return instance;
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            instance = GSON.fromJson(reader, ConfigData.class);
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public void updateFrom(ConfigData newData) {
        this.enableTreasureReciMsg = newData.enableTreasureReciMsg;
        this.enableCompactFishmsg = newData.enableCompactFishmsg;

        this.enableFishRecordOverlay = newData.enableFishRecordOverlay;
        this.fishRecordRenderTextX = newData.fishRecordRenderTextX;
        this.fishRecordRenderTextY = newData.fishRecordRenderTextY;
        this.fishRecordRenderScale = newData.fishRecordRenderScale;
        this.fishRecordBackgroundAlphaColor = newData.fishRecordBackgroundAlphaColor;
        this.fishRecordTextRGBColor = newData.fishRecordTextRGBColor;
        this.fishRecordIconShows = newData.fishRecordIconShows;
        this.fishRecordOverlayAlwaysShows = newData.fishRecordOverlayAlwaysShows;

        this.enableAugmentOverlay = newData.enableAugmentOverlay;

        this.didInfoShowOnce = newData.didInfoShowOnce;
    }
}
