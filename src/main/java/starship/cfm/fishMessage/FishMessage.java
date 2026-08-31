package starship.cfm.fishMessage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import starship.cfm.CompactFishingMessage;
import starship.cfm.augmentTracker.AugmentTracker;
import starship.cfm.mixin.MixinChatHudAccessor;
import starship.cfm.modMenu.ConfigData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.min;


public class FishMessage {
//    private static final String CAUGHT_SYMBOL = "\uE170";
//    private static final String TRIGGER_SYMBOL = "\uE018";
//    private static final Pattern CAUGHT_PATTERN =
//            Pattern.compile("\\(" + CAUGHT_SYMBOL + "\\) You caught: \\[(.+?)](?: x(\\d+))?\\s*$");
//    private static final Pattern TRIGGER_PATTERN =
//            Pattern.compile(".*" + TRIGGER_SYMBOL + " (Triggered|Special): .+? (.+)");
    private static final Pattern CAUGHT_PATTERN =
            Pattern.compile("\\([^)]*\\) You caught: \\[(.+?)](?: x(\\d+))?\\s*$");
    private static final Pattern TRIGGER_PATTERN =
            Pattern.compile("^.*? (Triggered|Special): .+? (.+)$");
//    private static final Pattern XP_PATTERN =
//            Pattern.compile(TRIGGER_SYMBOL + " You earned: (\\d+) Island XP");
    private static final Pattern XP_PATTERN =
            Pattern.compile(".* You earned: (\\d+) Island XP");
    private static final Set<String> KNOWN_TRIGGER_NAMES = Set.of(
            "Speedy Rod", "Boosted Rod", "Graceful Rod", "Stable Rod", "Glitched Rod",
            "XP Magnet", "Fish Magnet", "Pearl Magnet", "Treasure Magnet", "Spirit Magnet",
            "Elusive Catch", "Supply Preserve"
    );
    private static final int CATCH_TIMEOUT_TICKS = 20 * 3;
    private static final int LEAVE_ISLAND_RESET_TICKS = 20 * 3;
    private static FishMessage instance;
    private static MinecraftClient client;
    public final RecordOverlay recordOverlay = new RecordOverlay();
    private final boolean ifDebug = false;
    private final FishSession session = new FishSession();
    public boolean ifInFishingIsland = false;
    private int leaveIslandTickCounter = 0;
    private List<ChatHudLine.Visible> chatVisibleMessages;
    private List<ChatHudLine> chatMessages;
    private String chatHistoryFishMessage = "";
    private boolean ifMatch = false;
    private Text pendingFinalMessage = null; // compact msg + earned XP, waiting to replace the XP line

    public FishMessage(CompactFishingMessage cfm) {
        FishMessage.instance = this;
    }

    public static FishMessage getInstance() {
        return instance;
    }

    public void tick(MinecraftClient client) {
        if (client != null && client.player != null && client.world != null) {
            FishMessage.client = client;
            ChatHud chatHud = FishMessage.client.inGameHud.getChatHud();
            chatVisibleMessages = ((MixinChatHudAccessor) chatHud).getVisibleMessages();
            chatMessages = ((MixinChatHudAccessor) chatHud).getMessages();
            this.recordOverlay.tick(client);

            ifInFishingIsland = this.recordOverlay.ifInFishingIsland;
            if (ifInFishingIsland) leaveIslandTickCounter = 0;
            else leaveIslandTickCounter++;

            checkSessionTimeout();
        } else { // left the world / disconnected
            ifInFishingIsland = false;
            leaveIslandTickCounter = LEAVE_ISLAND_RESET_TICKS;
            chatHistoryFishMessage = "";
            if (session.isActive) session.reset();
        }
    }

    private void checkSessionTimeout() {
        if (!session.isActive) return;
        session.idleTickCounter++;
        if (session.idleTickCounter > CATCH_TIMEOUT_TICKS
                || leaveIslandTickCounter > LEAVE_ISLAND_RESET_TICKS) session.reset();
    }

    // A message wider than the chat box wraps into several visible lines. They are stored
    // newest-first, so an entry starts at its endOfEntry line and runs on through the lines
    // that are not endOfEntry; drop the whole group or the leading ones stay on screen until
    // the next refreshVisibleMessages().
    private void removeVisibleEntry(int entryIndex) {
        int entry = -1;
        for (int i = 0; i < chatVisibleMessages.size(); i++) {
            if (!chatVisibleMessages.get(i).endOfEntry()) continue;
            entry++;
            if (entry < entryIndex) continue;
            chatVisibleMessages.remove(i);
            while (i < chatVisibleMessages.size() && !chatVisibleMessages.get(i).endOfEntry())
                chatVisibleMessages.remove(i);
            return;
        }
    }

    public Text sendGameMsg(Text text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return text;
        if (client == null || client.player == null || client.world == null) return text;
        if (!ifInFishingIsland) return text;
        if (!ifMatch) return text;
        if (pendingFinalMessage != null) {
            Text finalMessage = pendingFinalMessage;
            pendingFinalMessage = null;
            return finalMessage;
        }
        if (session.isActive) {
            return session.caughtMessage.copy();
        } else
            return text;

    }

    public boolean shouldChatMsgCancel(Text text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return false;
        if (client == null || client.player == null || client.world == null) return false;
        if (!ifInFishingIsland) return false;
        ifMatch = false;
        String msg = text.getString();

        Matcher caughtMatcher = CAUGHT_PATTERN.matcher(msg);
        Matcher triggerMatcher = TRIGGER_PATTERN.matcher(msg);
        Matcher earnedMatcher = XP_PATTERN.matcher(msg);

        if (caughtMatcher.find()) {
            if (session.isActive) session.reset(); // stale catch: its XP msg never arrived
            ifMatch = true;
            session.isActive = true;

            session.idleTickCounter = 0;
            session.lootName = caughtMatcher.group(1).trim();
            String countStr = caughtMatcher.group(2);
            session.lootCount = (countStr != null) ? Integer.parseInt(countStr) : 1;
            session.catType = session.extraCategoryFromName(session.lootName);

            session.caughtMessage = extractCaughtMessage(text.copy());
            return false;

        }

        if (triggerMatcher.find() && session.isActive && session.caughtMessage != null) {
            ifMatch = true;
            session.idleTickCounter = 0;
            Text icon = extractTriggerIcon(text);
            if (chatVisibleMessages == null || chatMessages == null) return true;
            for (int i = 0; i < min(5, chatMessages.size()); i++) {
                if (!chatMessages.get(i).content().getString().contains(session.caughtMessage.getString())) continue;

                session.caughtMessage.append(Text.literal(" ")).append(icon);
                chatMessages.remove(i);
                removeVisibleEntry(i);
                break;
            }
            session.triggers.add(triggerMatcher.group(2));
            return false;
        }

        if (earnedMatcher.find() && session.isActive) {
            ifMatch = true;
            session.isLast = true;
            session.xpGained = Integer.parseInt(earnedMatcher.group(1).trim());
            recordOverlay.record(session.catType, session.xpGained);
            if (session.triggers.stream().noneMatch(s -> s.contains("Supply Preserve"))) {
                AugmentTracker.getInstance().recordAugment();
            }
            if (appendXpToCaughtMessage()) { // this line becomes the compact msg carrying the XP
                session.reset();
                return false;
            }
            session.reset();
            return true;
        }
        return false;
    }

    public boolean shouldHistoryChatCancel(Text text) { // false = no change
        if (!ConfigData.getInstance().enableCompactFishmsg) return false;
        if (!ifInFishingIsland) return false;
        if (chatVisibleMessages != null || chatMessages != null) return false;
        String plain = text.getString();
        Pattern pattern = Pattern.compile(".*?(\\(.*?\\) You caught: .+)");
        Matcher matcher = pattern.matcher(plain);
        if (matcher.find()) {
//            String x = matcher.group(1);
            if (Objects.equals(chatHistoryFishMessage, ""))
                chatHistoryFishMessage = matcher.group(1);
            else // the second or third time fishmsg shows
                if (matcher.group(1).contains(chatHistoryFishMessage) || matcher.group(1).equals(chatHistoryFishMessage)) {
                    return true;
                } else // new history msg
                    chatHistoryFishMessage = matcher.group(1);
        } else chatHistoryFishMessage = "";
        return false;
    }

    // Replaces the compact catch line already in chat with one ending in " + x XP"; the XP
    // message itself is then modified into that line, so nothing extra shows up.
    private boolean appendXpToCaughtMessage() {
        if (!ConfigData.getInstance().showIslandXpInFishmsg) return false;
        if (session.caughtMessage == null) return false;
        if (chatVisibleMessages == null || chatMessages == null) return false;

        for (int i = 0; i < min(5, chatMessages.size()); i++) {
            if (!chatMessages.get(i).content().getString().contains(session.caughtMessage.getString())) continue;

            pendingFinalMessage = session.caughtMessage.copy()
                    .append(Text.literal(" + ").setStyle(Style.EMPTY.withColor(0xAAAAAA)))
                    .append(Text.literal(String.valueOf(session.xpGained)).setStyle(Style.EMPTY.withColor(0xFFFFFF)))
                    .append(Text.literal(" XP").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
            chatMessages.remove(i);
            removeVisibleEntry(i);
            return true;
        }
        return false;
    }

    public Text extractTriggerIcon(Text fullText) {
        String plain = fullText.getString();

        Optional<String> matchedName = KNOWN_TRIGGER_NAMES.stream().filter(plain::contains).findFirst();

        return matchedName.map(FontFactory::get).orElse(Text.empty());
    }

    public MutableText extractCaughtMessage(Text fullText) {
        String msg = fullText.getString();

        Pattern TEMP_CAUGHT_PATTERN =
                Pattern.compile("\\(([^)]*)\\)\\s+You caught:.*");
        Matcher m = TEMP_CAUGHT_PATTERN.matcher(msg);

        if (!m.find()) return null;
        String CAUGHT_SYMBOL = m.group(1);

        boolean ifFound = false;
        MutableText root = Text.empty();
        for (Text msg1 : fullText.getSiblings()) {
            String str1 = msg1.getString();
            if (!str1.contains(CAUGHT_SYMBOL))
                root.append(msg1);
            else {
                MutableText root1 = Text.empty();
                for (Text msg2 : msg1.getSiblings()) {
                    String str2 = msg2.getString();

                    if (!str2.contains(CAUGHT_SYMBOL)) root1.append(msg2);
                    else {
                        if (str2.equals(CAUGHT_SYMBOL)) {
                            ifFound = true;
                            root1.append(FontFactory.getCategory(session.catType));
//                            break;
                        }
                        if (ifFound) {
                            root1.append(Text.literal(") You caught:").setStyle(Style.EMPTY.withColor(0x23D106)));
                            break;
                        }
                        MutableText root2 = Text.empty();
                        for (Text msg3 : msg2.getSiblings()) {
                            String str3 = msg3.getString();
                            if (!str3.equals(CAUGHT_SYMBOL))
                                root2.append(msg3);
                            else {
                                root2.append(FontFactory.getCategory(session.catType));
                            }
                        }
                        String root2str = root2.getString();
                        root1.append(root2);
                    }
                }
                String root1str = root1.getString();
                root.append(root1);
            }

        }
        return root;
    }

}
// TODO: 3s -> more and check if active
// TODO: 结构优化
