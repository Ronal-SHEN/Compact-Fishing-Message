package starship.cfm.augmentTracker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import starship.cfm.CompactFishingMessage;
import starship.cfm.fishMessage.FishMessage;
import starship.cfm.modMenu.ConfigData;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AugmentTracker {
    private static AugmentTracker instance;
    private MinecraftClient client;

    private boolean ifFirstTimeLoading = true;
    private ItemStack hookOverclock = ItemStack.EMPTY;
    private boolean ifHookOCNeedsShow = true;
    private ItemStack magnetOverclock = ItemStack.EMPTY;
    private boolean ifMagnetOCNeedsShow = true;
    private ItemStack rodOverclock = ItemStack.EMPTY;
    private boolean ifRodOCNeedsShow = true;
    private ItemStack unstableOverclock = ItemStack.EMPTY;
    private boolean ifUnstableOCNeedsShow = true;
    private int unstableOCDuration = -1;
    private int unstableOCCooldown = -1;
    private ItemStack supermeOverclock = ItemStack.EMPTY;
    private boolean ifSupermeOCNeedsShow = true;

    private ItemStack bait = ItemStack.EMPTY;
    private boolean ifBaitNeedsShow = true;
    private ItemStack line = ItemStack.EMPTY;
    private boolean ifLineNeedsShow = true;
    private int lineUsageRemain = -1;

    private int tickCounter = 0;

    private Pattern lineUseRemainPattern = Pattern.compile("Uses Remaining:\\s*(\\d+)/(\\d+)");

    public AugmentTracker(CompactFishingMessage cfm) {
        instance = this;
    }

    public static AugmentTracker getInstance() {
        return instance;
    }

    public void tick(MinecraftClient client) {
        this.client = client;
        if (client != null) {
            tickCounter++;
            if (tickCounter % 20 == 0) {
                tickCounter = 0;
                if (unstableOCDuration > 0) unstableOCDuration--;
                if (unstableOCDuration == 0 && unstableOCCooldown > 0) unstableOCCooldown--;
            }

        }
    }

    public void render(DrawContext drawContext) {
        if (client == null || client.options == null || client.player == null || client.world == null) return;
        if (!ConfigData.getInstance().enableAugmentOverlay) return;
        if (!ConfigData.getInstance().fishRecordOverlayAlwaysShows && !FishMessage.getInstance().ifInFishingIsland) return;

        int screenWidth = drawContext.getScaledWindowWidth();
        int screenHeight = drawContext.getScaledWindowHeight();

        int startRightX = screenWidth / 2 + 20;
        int startLeftX = screenWidth / 2 - 80;
        int yPos = screenHeight - 65;
        int blankWidth = 18;

        if (ifHookOCNeedsShow) drawContext.drawItem(hookOverclock, startRightX, yPos);
        if (ifMagnetOCNeedsShow) drawContext.drawItem(magnetOverclock, startRightX + blankWidth, yPos);
        if (ifRodOCNeedsShow) drawContext.drawItem(rodOverclock, startRightX + blankWidth * 2, yPos);
        if (ifSupermeOCNeedsShow) drawContext.drawItem(supermeOverclock, startRightX + blankWidth * 4, yPos);

        if (ifBaitNeedsShow) drawContext.drawItem(bait, startLeftX, yPos);
        if (ifLineNeedsShow) {
            TextRenderer textRenderer = client.textRenderer;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(0.5f, 0.5f, 1f);

            int scaledX = (startLeftX + blankWidth + 5) * 2 + 3;
            int scaledY = (yPos - 2) * 2 + 38;
            if (lineUsageRemain > 0) {
                int xOffset = (lineUsageRemain > 100) ? -4 : (lineUsageRemain >= 10) ? 0 : 1;
                drawContext.drawText(textRenderer, Text.literal(String.valueOf(lineUsageRemain)), scaledX + xOffset, scaledY, 0xFFFFFF, true);
                drawContext.getMatrices().pop();
                drawContext.drawItem(line, startLeftX + blankWidth, yPos);
            } else if (lineUsageRemain == 0) {
                drawContext.getMatrices().pop();
                Identifier plusID = Identifier.of("cfm", "textures/item/add.png");
                Function<Identifier, RenderLayer> renderLayerFunc = id -> RenderLayer.getGuiTextured(plusID);
                drawContext.drawTexture(renderLayerFunc, plusID, startLeftX + blankWidth, yPos, 0f, 0f, 16, 16, 16, 16);

            } else { // == -1
                drawContext.getMatrices().pop();
                drawContext.drawItem(line, startLeftX + blankWidth, yPos);
            }
        }
        if (ifUnstableOCNeedsShow) {
            TextRenderer textRenderer = client.textRenderer;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(0.5f, 0.5f, 1f);

            int scaledX = (startRightX + blankWidth * 3 + 5) * 2 - 5;
            int scaledY = (yPos - 2) * 2 + 40;

            if (unstableOCDuration > 0) { // activate
                int minutes = unstableOCDuration / 60;
                int seconds = unstableOCDuration % 60;
                String timeStr = String.format("%d:%02d", minutes, seconds);
                drawContext.drawText(textRenderer, Text.literal(timeStr), scaledX + (unstableOCDuration < 600 ? 1 : 0), scaledY, 0x7FBEEB, true);
                drawContext.getMatrices().pop();

                drawContext.drawItem(unstableOverclock, startRightX + blankWidth * 3, yPos);
            } else if (unstableOCDuration == 0 && unstableOCCooldown > 0) { // cooldown
                int minutes = unstableOCCooldown / 60;
                int seconds = unstableOCCooldown % 60;
                String timeStr = String.format("%d:%02d", minutes, seconds);
                drawContext.drawText(textRenderer, Text.literal(timeStr), scaledX + (unstableOCCooldown < 600 ? 1 : 0), scaledY, 0xFFFFFF, true);
                drawContext.getMatrices().pop();

                Identifier cooldownID = Identifier.of("cfm", "textures/item/cooldown.png");
                Function<Identifier, RenderLayer> renderLayerFunc = id -> RenderLayer.getGuiTextured(cooldownID);
                drawContext.drawTexture(renderLayerFunc, cooldownID, startRightX + blankWidth * 3, yPos, 0f, 0f, 16, 16, 16, 16);

            } else if (unstableOCDuration == 0 && unstableOCCooldown == 0) { // wait to be activated
                drawContext.getMatrices().pop();
                long gameTime = client.world.getTime();
                int[] frameSequence = {0, 1, 2, 1}; // 帧序列：0=activated1, 1=activated2, 2=activated3
                int frameTime = 4; // 每帧持续时间（游戏刻）
                int currentFrameIndex = (int)((gameTime / frameTime) % frameSequence.length);
                int frame = frameSequence[currentFrameIndex];

                // 根据帧号选择对应的材质
                Identifier waitID;
                switch (frame) {
                    case 1 -> waitID = Identifier.of("cfm", "textures/item/activated1.png");
                    case 2 -> waitID = Identifier.of("cfm", "textures/item/activated2.png");
                    default -> waitID = Identifier.of("cfm", "textures/item/activated0.png");
                }
                Function<Identifier, RenderLayer> renderLayerFunc = id -> RenderLayer.getGuiTextured(waitID);
                drawContext.drawTexture(renderLayerFunc, waitID, startRightX + blankWidth * 3, yPos, 0f, 0f, 16, 16, 16, 16);
            } else //dr == -1 && cd == -1
            {
                drawContext.getMatrices().pop();
                drawContext.drawItem(unstableOverclock, startRightX + blankWidth * 3, yPos);
            }
        }
        // TODO: gcs switch state
    }

    public void detectScreenFishSupplyClose(Screen screen) {
        if (screen instanceof GenericContainerScreen containerScreen) {
            GenericContainerScreenHandler handler = containerScreen.getScreenHandler();
            Inventory inventory = handler.getInventory();
            if (inventory.size() < 40) return;

            hookOverclock = inventory.getStack(12);
            magnetOverclock = inventory.getStack(13);
            rodOverclock = inventory.getStack(14);
            unstableOverclock = inventory.getStack(15);
            supermeOverclock = inventory.getStack(16);
            bait = inventory.getStack(19);
            line = inventory.getStack(37);


            ifHookOCNeedsShow = !hookOverclock.getName().getString().contains("Locked");
            ifMagnetOCNeedsShow = !magnetOverclock.getName().getString().contains("Locked");
            ifRodOCNeedsShow = !rodOverclock.getName().getString().contains("Locked");
            ifUnstableOCNeedsShow = !unstableOverclock.getName().getString().contains("Locked");
            ifBaitNeedsShow = !Objects.equals(bait.getName().getString(), "Bait Slot");
            ifLineNeedsShow = !Objects.equals(line.getName().getString(), "Line Slot");

            if (ifFirstTimeLoading) {
                ifFirstTimeLoading = false;

                List<Text> tooltipLines = supermeOverclock.getTooltip(
                        Item.TooltipContext.DEFAULT, client.player, TooltipType.BASIC);
                if (!tooltipLines.isEmpty() && tooltipLines.size() > 24
                        && tooltipLines.get(23).getString().contains("Grand Champ Supreme rank")) // rank diff
                    ifSupermeOCNeedsShow = false;
            }
            if (ifLineNeedsShow) {
                if (!line.getName().getString().contains("Empty")) {
                    List<Text> tooltipLines = line.getTooltip(
                            Item.TooltipContext.DEFAULT, client.player, TooltipType.BASIC);
                    if (!tooltipLines.isEmpty() && tooltipLines.size() > 15) { // Uses Remaining: 23/50
                        String rawUses = tooltipLines.get(15).getString();
                        Matcher lineUserMatcher = lineUseRemainPattern.matcher(rawUses);
                        if (lineUserMatcher.find()) {
                            lineUsageRemain = Integer.parseInt(lineUserMatcher.group(1));
                        }
                    }
                } else
                    lineUsageRemain = -1;
            }
        }
    }

    public void activateUnstableOC(ItemStack stack) {
        if (client == null || client.player == null || client.world == null) return;
        if (!ifUnstableOCNeedsShow) return;
        List<Text> tooltipLines = stack.getTooltip(
                Item.TooltipContext.DEFAULT, client.player, TooltipType.BASIC);
        if (!tooltipLines.isEmpty() && tooltipLines.size() > 24) {
            String rawIfActivate = tooltipLines.getLast().getString();
            if (rawIfActivate.contains("Currently active") || rawIfActivate.contains("On cooldown")) return;
            //cant use oc when on cooldown or active

            for (Text line : tooltipLines) {
                String raw = line.getString();
                if (raw.contains("Duration: "))
                    unstableOCDuration = 60 * Integer.parseInt(raw.substring(raw.length() - 3, raw.length() - 1).trim());
                if (raw.contains("Cooldown: "))
                    unstableOCCooldown = 1 + 60 * Integer.parseInt(raw.substring(raw.length() - 3, raw.length() - 1).trim());
            }
        }


    }

    public void recordAugment() {
        if (lineUsageRemain > 0) lineUsageRemain--;
    }

    public void detectText(Text message) {
        String msg = message.getString();
        Pattern BAITRUNOUT = Pattern.compile("You've run out of your equipped \\[(.+?) Bait]");
        Matcher matcher = BAITRUNOUT.matcher(msg);
        if (matcher.find()) {
            bait = ItemStack.EMPTY;
        }

    }
    // TODO: pause bait number after buying/unboxing, disable bait render after running out of it(chat)
    // TODO: dynamic sprite
    // TODO: add blank letter when less than 10min
}