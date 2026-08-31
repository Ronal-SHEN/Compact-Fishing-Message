package starship.cfm.fishMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
//    private static final String CAUGHT_SYMBOL = "";
//    private static final String TRIGGER_SYMBOL = "";
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
    private static Minecraft client;
    public final RecordOverlay recordOverlay = new RecordOverlay();
    private final boolean ifDebug = false;
    private final FishSession session = new FishSession();
    public boolean ifInFishingIsland = false;
    private int leaveIslandTickCounter = 0;
    private List<GuiMessage.Line> chatVisibleMessages;
    private List<GuiMessage> chatMessages;
    private String chatHistoryFishMessage = "";
    private boolean ifMatch = false;
    private Component pendingFinalMessage = null; // compact msg + earned XP, waiting to replace the XP line

    public FishMessage(CompactFishingMessage cfm) {
        FishMessage.instance = this;
    }

    public static FishMessage getInstance() {
        return instance;
    }

    public void tick(Minecraft client) {
        if (client != null && client.player != null && client.level != null) {
            FishMessage.client = client;
            ChatComponent chatHud = FishMessage.client.gui.getChat();
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

    public Component sendGameMsg(Component text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return text;
        if (client == null || client.player == null || client.level == null) return text;
        if (!ifInFishingIsland) return text;
        if (!ifMatch) return text;
        if (pendingFinalMessage != null) {
            Component finalMessage = pendingFinalMessage;
            pendingFinalMessage = null;
            return finalMessage;
        }
        if (session.isActive) {
            return session.caughtMessage.copy();
        } else
            return text;

    }

    public boolean shouldChatMsgCancel(Component text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return false;
        if (client == null || client.player == null || client.level == null) return false;
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
            Component icon = extractTriggerIcon(text);
            if (chatVisibleMessages == null || chatMessages == null) return true;
            for (int i = 0; i < min(5, chatMessages.size()); i++) {
                GuiMessage caught = chatMessages.get(i);
                if (!caught.content().getString().contains(session.caughtMessage.getString())) continue;

                session.caughtMessage.append(Component.literal(" ")).append(icon);
                chatMessages.remove(i);
                // every wrapped line keeps a ref to the message it came from, so drop them by
                // owner: a message wider than the chat box makes more than one line, and its
                // leading ones used to stay on screen until the next refreshTrimmedMessages()
                chatVisibleMessages.removeIf(visibleLine -> visibleLine.parent() == caught);
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

    public boolean shouldHistoryChatCancel(Component text) { // false = no change
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

    // Replaces the compact catch line already in chat with one ending in " +x XP"; the XP
    // message itself is then modified into that line, so nothing extra shows up.
    private boolean appendXpToCaughtMessage() {
        if (!ConfigData.getInstance().showIslandXpInFishmsg) return false;
        if (session.caughtMessage == null) return false;
        if (chatVisibleMessages == null || chatMessages == null) return false;

        for (int i = 0; i < min(5, chatMessages.size()); i++) {
            GuiMessage caught = chatMessages.get(i);
            if (!caught.content().getString().contains(session.caughtMessage.getString())) continue;

            pendingFinalMessage = session.caughtMessage.copy().append(
                    Component.literal(" +" + session.xpGained + " XP")
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA)));
            chatMessages.remove(i);
            chatVisibleMessages.removeIf(visibleLine -> visibleLine.parent() == caught);
            return true;
        }
        return false;
    }

    public Component extractTriggerIcon(Component fullText) {
        String plain = fullText.getString();

        Optional<String> matchedName = KNOWN_TRIGGER_NAMES.stream().filter(plain::contains).findFirst();

        return matchedName.map(FontFactory::get).orElse(Component.empty());
    }

    public MutableComponent extractCaughtMessage(Component fullText) {
        String msg = fullText.getString();

        Pattern TEMP_CAUGHT_PATTERN =
                Pattern.compile("\\(([^)]*)\\)\\s+You caught:.*");
        Matcher m = TEMP_CAUGHT_PATTERN.matcher(msg);

        if (!m.find()) return null;
        String CAUGHT_SYMBOL = m.group(1);

        boolean ifFound = false;
        MutableComponent root = Component.empty();
        for (Component msg1 : fullText.getSiblings()) {
            String str1 = msg1.getString();
            if (!str1.contains(CAUGHT_SYMBOL))
                root.append(msg1);
            else {
                MutableComponent root1 = Component.empty();
                for (Component msg2 : msg1.getSiblings()) {
                    String str2 = msg2.getString();

                    if (!str2.contains(CAUGHT_SYMBOL)) root1.append(msg2);
                    else {
                        if (str2.equals(CAUGHT_SYMBOL)) {
                            ifFound = true;
                            root1.append(FontFactory.getCategory(session.catType));
//                            break;
                        }
                        if (ifFound) {
                            root1.append(Component.literal(") You caught:").setStyle(Style.EMPTY.withColor(0x23D106)));
                            break;
                        }
                        MutableComponent root2 = Component.empty();
                        for (Component msg3 : msg2.getSiblings()) {
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
