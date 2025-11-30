package starship.cfm.fishMessage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
    private static MinecraftClient client;
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

    public void tick(MinecraftClient client) {
        if (client != null && client.player != null && client.world != null) {
            RecordOverlay.client = client;
            getFishingTimeFromScoreBoard();
        }
    }

    public void render(DrawContext drawContext) {
        if (client == null || client.options == null || client.player == null || client.world == null) return;
        if (!ConfigData.getInstance().enableFishRecordOverlay) return;
        if (!ConfigData.getInstance().fishRecordOverlayAlwaysShows && !ifInFishingIsland) return;

        int x = ConfigData.getInstance().fishRecordRenderTextX;
        int y = ConfigData.getInstance().fishRecordRenderTextY;
        float scale = ConfigData.getInstance().fishRecordRenderScale;
        int backgroundColor = ConfigData.getInstance().fishRecordBackgroundAlphaColor << 24;
        int textColor = 0xFF000000 | (ConfigData.getInstance().fishRecordTextRGBColor & 0xFFFFFF);

        TextRenderer textRenderer = client.textRenderer;
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        List<Map.Entry<String, String>> renderEntries = getRenderEntries();

        String sampleLine = renderEntries.size() > 1 ? renderEntries.get(1).getValue() : "";
        int maxWidth = 35 + textRenderer.getWidth(sampleLine);
        float lineHeight = 11.5f;
        int totalHeight = (int) ((lineHeight + 1) * renderEntries.size());

        matrices.pop();
        drawContext.fill(x, y, x + (int) (maxWidth * scale), y + (int) (totalHeight * scale), backgroundColor);
        matrices.push();
        matrices.translate(x, y, 0);
        matrices.scale(scale, scale, 1.0f);

        int i = 0;
        for (Map.Entry<String, String> entry : renderEntries) {
            int lineY = (int) (4 + i * lineHeight);
            i++;
            String icon = entry.getKey();
            String line = entry.getValue();

            if (ConfigData.getInstance().fishRecordIconShows) {
                Text iconText = Text.literal(icon).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                        Identifier.of("cfm", "icon")));
                drawContext.drawText(textRenderer, iconText, 4, lineY, textColor, false);
                drawContext.drawText(textRenderer, line, 15, lineY, textColor, false);
            } else
                drawContext.drawText(textRenderer, line, 4, lineY, textColor, false);

        }

        matrices.pop();
    }

    private List<Map.Entry<String, String>> getRenderEntries() {
        List<Map.Entry<String, String>> entries = new ArrayList<>();

        entries.add(Map.entry("\uE100", "Time: " +
                (fishingTime >= 60 ? (fishingTime / 60) + "h " : "") + (fishingTime % 60) + "m"));
        entries.add(Map.entry("\uE101", "Reel-ins: " + lootReelInTimes));
        entries.add(Map.entry("\uE102", "XP: " + String.format("%,d", xpGained)));
        entries.add(Map.entry("\uE10A", "Junk: " + junkCaught));
        entries.add(Map.entry("\uE104", "Normal: " + normalFishCaught));
        entries.add(Map.entry("\uE105", "Elusive: " + elusiveFishCaught));
        entries.add(Map.entry("\uE106", "Pearl: " + pearlCaught));
        entries.add(Map.entry("\uE107", "Treasure: " + treasureCaught));
        entries.add(Map.entry("\uE108", "Spirit: " + spiritCaught));

        return entries;
    }

    private void getFishingTimeFromScoreBoard() {
        if (client != null && client.player != null && client.world != null) {
            Scoreboard scoreboard = client.player.getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
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
