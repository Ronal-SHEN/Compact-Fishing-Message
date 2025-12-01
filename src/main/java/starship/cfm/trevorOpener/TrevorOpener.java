package starship.cfm.trevorOpener;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import starship.cfm.CompactFishingMessage;
import starship.cfm.modMenu.ConfigData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrevorOpener {
    private static final String RECEIVE_SYMBOL = "\uE15D";
    private static final Pattern RECEIVE_PATTERN = Pattern.compile(
            ".*\\(" + RECEIVE_SYMBOL + "\\) You receive: \\[(.+?)](?: x(\\d{1,3}(?:,\\d{3})*))?\\s*$"
    );
    public static EventState eventState = EventState.INACTIVE; // only 1 event
    public static boolean ifTreasureOnClick = false;
    public static boolean ifTreasureRlyOpened = false;
    public static boolean ifReceivedMsgAfterOpen = false;
    private static TrevorOpener instance;
    private static MinecraftClient client;
    public final Treasure treasure;
    private final Lure lure;
    private final Tech tech;
    private final BaitLine baitLine;
    private final Augment augment;
    private final Cosmetic cosmetic;
    public long treasureMenuOpenedTime = 0;
    public long treasureMenuClosedTime = 0;
    public boolean ifSUMMAEYScreenOpened = false;
    public String pendingTreasureName = "";
    public int pendingTreasureCount = 0;

    public TrevorOpener(CompactFishingMessage cfm) {
        instance = this;
        this.treasure = new Treasure();
        this.lure = new Lure();
        this.tech = new Tech();
        this.baitLine = new BaitLine();
        this.augment = new Augment();
        this.cosmetic = new Cosmetic();

    }

    public static TrevorOpener getInstance() {
        return instance;
    }

    public void tick(MinecraftClient client) {
        if (client != null && client.player != null && client.world != null) {
            TrevorOpener.client = client;
            if (!ifTreasureOnClick && treasureMenuClosedTime != 0
                    && (Util.getMeasuringTimeMs() - treasureMenuClosedTime) > 10 * 1000) { // its fine to set time long
                ifReceivedMsgAfterOpen = false;
            }
//            client.player.sendMessage(Text.of("ifTreasureOnClick: " + ifTreasureOnClick
//                    + "    ifTreasureRlyOpened: " + ifTreasureRlyOpened
//                     + "    ifReceivedMsgAfterOpen: " + ifReceivedMsgAfterOpen), true);

        }
    }

    public void recordTreasure(String name, int count) {
        if (client != null && client.player != null && client.world != null) {
//            this.treasure.record(name, count);
            pendingTreasureName = name;
            pendingTreasureCount = count;
            ifTreasureOnClick = true;
            ifTreasureRlyOpened = false;
        }
    }

    public boolean shouldChatMsgCancel(Text text) {
        if (eventState == EventState.INACTIVE) return false;
        if (!ifReceivedMsgAfterOpen && !ConfigData.getInstance().enableTreasureReciMsg) return false;

        Matcher matcher = RECEIVE_PATTERN.matcher(text.getString());
        if (!matcher.find()) return false;
        String name = matcher.group(1).trim();
        String countStr = matcher.group(2);
        int count = (countStr != null) ? Integer.parseInt(countStr.replace(",", "")) : 1;

        if (baitLine.namePattern.matcher(name).find()) {
            baitLine.record(name, count);
        } else if (augment.namePattern.matcher(name).find()) {
            augment.record(name, count);
        } else if (tech.namePattern.matcher(name).find()) {
            tech.record(name, count);
        } else if (lure.namePattern.matcher(name).find()) {
            lure.record(name, count);
        } else if (cosmetic.namePattern.matcher(name).find()) {
            cosmetic.record(name, count);
        } else
            return false;
        return !ConfigData.getInstance().enableTreasureReciMsg;
    }

    public void eventStart() {
        if (client == null || client.world == null || client.player == null) return;
        if (eventState == EventState.INACTIVE) {
            client.player.sendMessage(Text.literal("TreasureOpen event created.")
                    .formatted(Formatting.DARK_GREEN).formatted(Formatting.BOLD), false);

            this.resetEvent();
            eventState = EventState.ACTIVE;
        } else {
            client.player.sendMessage(Text.literal("You already have an active event!")
                    .formatted(Formatting.DARK_RED).formatted(Formatting.BOLD), false);
        }

    }

    public void eventEnd() {
        if (client == null || client.world == null || client.player == null) return;
        if (eventState == EventState.INACTIVE) {
            client.player.sendMessage(Text.literal("There is no active event to stop!")
                    .formatted(Formatting.DARK_RED).formatted(Formatting.BOLD), false);
        } else {
            if (treasure.summary().getString().isBlank()) {
                client.player.sendMessage(Text.literal("You have opened Nothing!")
                        .formatted(Formatting.BOLD).formatted(Formatting.DARK_RED), false);
            } else {
                eventState = EventState.INACTIVE;
                client.player.sendMessage(Text.literal("You have opened: ")
                        .formatted(Formatting.BOLD).formatted(Formatting.DARK_GREEN), false);
                client.player.sendMessage(treasure.summary(), false);

                client.player.sendMessage(Text.literal("You have received: ")
                        .formatted(Formatting.BOLD).formatted(Formatting.DARK_GREEN), false);
                client.player.sendMessage(Text.literal("————————————————————————————————————")
                        .formatted(Formatting.DARK_GRAY), false);

                client.player.sendMessage(Text.literal("Bait&Line: ").formatted(Formatting.DARK_GREEN), false);
                client.player.sendMessage(baitLine.summary(), false); // bait&line cant be empty
                if (!augment.summary().getString().isBlank()) {
                    client.player.sendMessage(Text.literal("Augment: ").formatted(Formatting.DARK_GREEN), false);
                    client.player.sendMessage(augment.summary(), false);
                }
                if (!lure.summary().getString().isBlank()) {
                    client.player.sendMessage(Text.literal("Lure: ").formatted(Formatting.DARK_GREEN), false);
                    client.player.sendMessage(lure.summary(), false);
                }
                if (!tech.summary().getString().isBlank()) {
                    client.player.sendMessage(Text.literal("Tech: ").formatted(Formatting.DARK_GREEN), false);
                    client.player.sendMessage(tech.summary(), false);
                }
                if (!cosmetic.summary().getString().isBlank()) {
                    client.player.sendMessage(Text.literal("Cosmetic: ").formatted(Formatting.DARK_GREEN), false);
                    client.player.sendMessage(cosmetic.summary(), false);
                }
                client.player.sendMessage(Text.literal("————————————————————————————————————")
                        .formatted(Formatting.DARK_GRAY), false);
            }

        }
    }

    public void resetEvent() {
        this.treasure.reset();
        this.lure.reset();
        this.tech.reset();
        this.baitLine.reset();
        this.augment.reset();
        this.cosmetic.reset();
        ifTreasureOnClick = false;
        ifTreasureRlyOpened = false;
        ifSUMMAEYScreenOpened = false;
        ifReceivedMsgAfterOpen = false;
        treasureMenuOpenedTime = 0;
        treasureMenuClosedTime = 0;
        pendingTreasureName = "";
        pendingTreasureCount = 0;
    }

    public void detectScreenINFINIBAG() {
        if (client == null || client.world == null || client.player == null) {
        }
//        client.player.sendMessage(Text.of("title is: "+ title), false);
//        ifTreasureOnClick = false; // could be somewhere else
//        ifTreasureRlyOpened = false;
//        treasureOpenedTime = 0;
//        pendingTreasureName = "";
//        pendingTreasureCount = 0;
    }

    public void detectScreenSUMMARYOpen() {
        if (client == null || client.world == null || client.player == null) return;
        if (ifTreasureOnClick) { // dont need to detect scenario like: click chest but not open
            ifTreasureRlyOpened = true;
            treasureMenuOpenedTime = Util.getMeasuringTimeMs();
            this.treasure.record(pendingTreasureName, pendingTreasureCount);
            pendingTreasureName = "";
            pendingTreasureCount = 0;

            ifSUMMAEYScreenOpened = true;

            ifReceivedMsgAfterOpen = true;
        }
    }

    public void detectScreenSUMMARYClose() {
        if (client == null || client.world == null || client.player == null) return;
        if (ifTreasureOnClick && ifTreasureRlyOpened) {
            ifSUMMAEYScreenOpened = false;
            treasureMenuClosedTime = Util.getMeasuringTimeMs();
//          ifReceivedMsgAfterOpen = true;
            ifTreasureOnClick = false;

        }
    }

    public enum EventState {
        ACTIVE, INACTIVE
    }
}
// linebait换行