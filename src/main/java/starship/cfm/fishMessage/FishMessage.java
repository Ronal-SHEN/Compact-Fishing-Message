package starship.cfm.fishMessage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
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
    private static final Pattern CAUGHT_PATTERN = Pattern.compile("\\(\uE15B\\) You caught: \\[(.+?)](?: x(\\d+))?\\s*$");
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(".*\uE018 (Triggered|Special): .+? (.+)");
    private static final Pattern XP_PATTERN = Pattern.compile("\uE018 You earned: (\\d+) Island XP");
    private static final Set<String> KNOWN_TRIGGER_NAMES = Set.of(
            "Speedy Rod", "Boosted Rod", "Graceful Rod", "Stable Rod", "Glitched Rod",
            "XP Magnet", "Fish Magnet", "Pearl Magnet", "Treasure Magnet", "Spirit Magnet",
            "Elusive Catch", "Supply Preserve"
    );
    private static FishMessage instance;
    private static MinecraftClient client;
    public final RecordOverlay recordOverlay = new RecordOverlay();
    private final boolean ifDebug = false;
    private final FishSession session = new FishSession();
    public boolean ifInFishingIsland = false;
    private List<ChatHudLine.Visible> chatVisibleMessages;
    private List<ChatHudLine> chatMessages;
    private String chatHistoryFishMessage = "";
    private boolean ifMatch = false;

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
        }
    }

    public Text sendGameMsg(Text text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return text;
        if (client == null || client.player == null || client.world == null) return text;
        if (!ifMatch) return text;
        if (session.isActive) {
            return session.caughtMessage.copy();
        } else
            return text;

    }

    public boolean shouldChatMsgCancel(Text text) {
        if (!ConfigData.getInstance().enableCompactFishmsg) return false;
        if (client == null || client.player == null || client.world == null) return false;
        ifMatch = false;
        String msg = text.getString();

        Matcher caughtMatcher = CAUGHT_PATTERN.matcher(msg);
        Matcher triggerMatcher = TRIGGER_PATTERN.matcher(msg);
        Matcher earnedMatcher = XP_PATTERN.matcher(msg);

        if (caughtMatcher.find() && !session.isActive) {
            ifMatch = true;
            session.isActive = true;

            session.catchTime = Util.getMeasuringTimeMs();
            session.lootName = caughtMatcher.group(1).trim();
            String countStr = caughtMatcher.group(2);
            session.lootCount = (countStr != null) ? Integer.parseInt(countStr) : 1;
            session.catType = session.extraCategoryFromName(session.lootName);

            session.caughtMessage = extractCaughtMessage(text.copy());
            return false;

        }

        if (triggerMatcher.find() && session.isActive) {
            ifMatch = true;
            Text icon = extractTriggerIcon(text);
            if (chatVisibleMessages == null || chatMessages == null) return true;
            for (int i = 0; i < min(5, chatMessages.size()); i++) {
                if (chatMessages.get(i).content().getString().contains(session.caughtMessage.getString())) {
                    session.caughtMessage.append(Text.literal(" ")).append(icon);
                    chatMessages.remove(i);
                    if (!chatVisibleMessages.get(i).endOfEntry())
                        for (int j = i + 1; j < min(chatVisibleMessages.size(), 10); j++) {
                            if (chatVisibleMessages.get(j).endOfEntry()) {
                                chatVisibleMessages.remove(j);
                                break;
                            }
                        }
                    else {
                        chatVisibleMessages.remove(i);
                        if (i < chatVisibleMessages.size() && !chatVisibleMessages.get(i).endOfEntry())
                            chatVisibleMessages.remove(i); // if some1's chat box is too thin, making msg 2 line
                    }
                    break;
                }
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
            session.reset();
            return true;
        }
        if (ifMatch && session.isActive && (Util.getMeasuringTimeMs() - session.catchTime) > 1000 * 30) session.reset();

        return false;
    }

    public boolean shouldHistoryChatCancel(Text text) { // false = no change
        if (!ConfigData.getInstance().enableCompactFishmsg) return false;
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

    public Text extractTriggerIcon(Text fullText) {
        String plain = fullText.getString();

        Optional<String> matchedName = KNOWN_TRIGGER_NAMES.stream().filter(plain::contains).findFirst();

        return matchedName.map(FontFactory::get).orElse(Text.empty());
    }

    public MutableText extractCaughtMessage(Text fullText) {
        String msg = fullText.getString();

        boolean ifFound = false;
        MutableText root = Text.empty();
        for (Text msg1 : fullText.getSiblings()) {
            String str1 = msg1.getString();
            if (!str1.contains("\uE15B"))
                root.append(msg1);
            else {
                MutableText root1 = Text.empty();
                for (Text msg2 : msg1.getSiblings()) {
                    String str2 = msg2.getString();

                    if (!str2.contains("\uE15B")) root1.append(msg2);
                    else {
                        if (str2.equals("\uE15B")) {
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
                            if (!str3.equals("\uE15B"))
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
