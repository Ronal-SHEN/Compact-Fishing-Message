package starship.cfm.fishMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import starship.cfm.modMenu.ConfigData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordOverlay {
    private static final Pattern MCCI_TITLE_PATTERN = Pattern.compile("MCCI: (.+)");
    private static final Set<String> ISLAND_NAMES = Set.of(
            "VERDANT WOODS", "FLORAL FOREST", "DARK GROVE", "SUNKEN SWAMP",
            "TROPICAL OVERGROWTH", "CORAL SHORES", "TWISTED SWAMP", "MIRRORED OASIS",
            "ANCIENT SANDS", "BLAZING CANYON", "ASHEN WASTES", "VOLCANIC SPRINGS");
    private static Minecraft client;
    public boolean ifInFishingIsland = false;
    private int tickCounter = 0;
    private int linboTickCounter = 0;

    private int fishingTime = 0;
    private int lootReelInTimes = 0;
    private int junkCaught = 0;
    private int normalFishCaught = 0;
    private int elusiveFishCaught = 0;
    private int pearlCaught = 0;
    private int treasureCaught = 0;
    private int spiritCaught = 0;
    private int xpGained = 0;

    public void tick(Minecraft client) {
        if (client != null && client.player != null && client.level != null) {
            RecordOverlay.client = client;
            getFishingTimeFromScoreBoard();
        }
    }

    public void render(GuiGraphicsExtractor guiGraphics) {
        if (client == null || client.options == null || client.player == null || client.level == null) return;
        if (!ConfigData.getInstance().enableFishRecordOverlay) return;
        if (!ConfigData.getInstance().fishRecordOverlayAlwaysShows && !ifInFishingIsland) return;

        int x = ConfigData.getInstance().fishRecordRenderTextX;
        int y = ConfigData.getInstance().fishRecordRenderTextY;
        float scale = ConfigData.getInstance().fishRecordRenderScale;
        int backgroundColor = ConfigData.getInstance().fishRecordBackgroundAlphaColor << 24;
        int textColor = 0xFF000000 | (ConfigData.getInstance().fishRecordTextRGBColor & 0xFFFFFF);

        Font font = client.font;
        Matrix3x2fStack matrices = guiGraphics.pose();
        Matrix3x2f backupMatrix = new Matrix3x2f(matrices);

        List<Map.Entry<String, String>> renderEntries = getRenderEntries();

        String sampleLine = renderEntries.size() > 1 ? renderEntries.get(1).getValue() : "";
        int maxWidth = 35 + font.width(sampleLine);
        float lineHeight = 11.5f;
        int totalHeight = (int) ((lineHeight + 1) * renderEntries.size());

        guiGraphics.fill(x, y, x + (int) (maxWidth * scale), y + (int) (totalHeight * scale), backgroundColor);
        matrices.translate(x, y);
        matrices.scale(scale, scale);

        int i = 0;
        for (Map.Entry<String, String> entry : renderEntries) {
            int lineY = (int) (4 + i * lineHeight);
            i++;
            String icon = entry.getKey();
            String line = entry.getValue();

            if (ConfigData.getInstance().fishRecordIconShows) {
                Component iconText = Component.literal(icon).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon"))));
                guiGraphics.text(font, iconText, 4, lineY, textColor, false);
                guiGraphics.text(font, line, 15, lineY, textColor, false);
            } else
                guiGraphics.text(font, line, 4, lineY, textColor, false);

        }
        matrices.set(backupMatrix);

    }

    private List<Map.Entry<String, String>> getRenderEntries() {
        List<Map.Entry<String, String>> entries = new ArrayList<>();

        entries.add(Map.entry("", "Time: " +
                (fishingTime >= 60 ? (fishingTime / 60) + "h " : "") + (fishingTime % 60) + "m"));
        entries.add(Map.entry("", "Reel-ins: " + lootReelInTimes));
        entries.add(Map.entry("", "XP: " + String.format("%,d", xpGained)));
        entries.add(Map.entry("", "Junk: " + junkCaught));
        entries.add(Map.entry("", "Normal: " + normalFishCaught));
        entries.add(Map.entry("", "Elusive: " + elusiveFishCaught));
        entries.add(Map.entry("", "Pearl: " + pearlCaught));
        entries.add(Map.entry("", "Treasure: " + treasureCaught));
        entries.add(Map.entry("", "Spirit: " + spiritCaught));

        return entries;
    }

    private void getFishingTimeFromScoreBoard() {
        if (client != null && client.player != null && client.level != null) {
            Scoreboard scoreboard = client.level.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
            if (objective != null) {
                String objectiveName = objective.getDisplayName().getString();
                Matcher matcher = MCCI_TITLE_PATTERN.matcher(objectiveName);
                if (matcher.find()) {
                    String islandName = matcher.group(1);
                    ifInFishingIsland = ISLAND_NAMES.contains(islandName);
                } else {
                    if (linboTickCounter >= 20 * 3) {
                        ifInFishingIsland = false;
                    } else
                        linboTickCounter++;
                }
            } else {
                if (linboTickCounter >= 20 * 3) {
                    ifInFishingIsland = false;
                    linboTickCounter = 0;
                } else
                    linboTickCounter++;
            }

            if (ifInFishingIsland) {
                tickCounter++;
                if (tickCounter >= 1200) {
                    fishingTime++;
                    tickCounter = 0;
                }
            }

        }
    }

    public void record(FontFactory.CategoryType type, int xp) {
        lootReelInTimes += 1;
        xpGained += xp;
        switch (type) {
            case JUNK -> junkCaught++;
            case PEARL -> pearlCaught++;
            case TREASURE -> treasureCaught++;
            case SPIRIT -> spiritCaught++;
            case ELUSIVE_FISH -> elusiveFishCaught++;
            case NORMAL_FISH -> normalFishCaught++;
        }
    }
}
