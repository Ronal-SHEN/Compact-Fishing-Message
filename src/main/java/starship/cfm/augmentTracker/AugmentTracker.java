package starship.cfm.augmentTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import starship.cfm.CompactFishingMessage;
import starship.cfm.fishMessage.FishMessage;
import starship.cfm.modMenu.ConfigData;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AugmentTracker {
    private static AugmentTracker instance;
    private Minecraft client;

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

    public void tick(Minecraft client) {
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

    public void render(GuiGraphicsExtractor guiGraphics) {
        if (client == null || client.options == null || client.player == null || client.level == null) return;
        if (!ConfigData.getInstance().enableAugmentOverlay) return;
        if (!ConfigData.getInstance().fishRecordOverlayAlwaysShows && !FishMessage.getInstance().ifInFishingIsland) return;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int startRightX = screenWidth / 2 + 20;
        int startLeftX = screenWidth / 2 - 80;
        int yPos = screenHeight - 65;
        int blankWidth = 18;

        if (ifHookOCNeedsShow) guiGraphics.item(hookOverclock, startRightX, yPos);
        if (ifMagnetOCNeedsShow) guiGraphics.item(magnetOverclock, startRightX + blankWidth, yPos);
        if (ifRodOCNeedsShow) guiGraphics.item(rodOverclock, startRightX + blankWidth * 2, yPos);
        if (ifSupermeOCNeedsShow) guiGraphics.item(supermeOverclock, startRightX + blankWidth * 4, yPos);

        if (ifBaitNeedsShow) guiGraphics.item(bait, startLeftX, yPos);
        if (ifLineNeedsShow) {
            Font font = client.font;
            Matrix3x2fStack matrices = guiGraphics.pose();
            Matrix3x2f backupMatrix = new Matrix3x2f(matrices);

            int scaledX = (int)((startLeftX + blankWidth + 5) * 2 + 3)/2;
            int scaledY = (int)((yPos - 2) * 2 + 38)/2;
            matrices.translate(scaledX, scaledY);
            matrices.scale(0.5f, 0.5f);

            if (lineUsageRemain > 0) {
                int xOffset = (lineUsageRemain > 100) ? -4 : (lineUsageRemain >= 10) ? 0 : 1;
                guiGraphics.text(font,Component.literal(String.valueOf(lineUsageRemain)), 0 + xOffset, 0, 0xFFFFFFFF, true);
                matrices.set(backupMatrix);
                guiGraphics.item(line, startLeftX + blankWidth, yPos);
            } else if (lineUsageRemain == 0) {
                matrices.set(backupMatrix);
                Identifier plusID = Identifier.fromNamespaceAndPath("cfm", "textures/item/add.png");
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,plusID, startLeftX + blankWidth, yPos, 0f, 0f, 16, 16, 16, 16);

            }
            else { // == -1
                matrices.set(backupMatrix);
                guiGraphics.item(line, startLeftX + blankWidth, yPos);
            }
        }
        if (ifUnstableOCNeedsShow) { //
            Font font = client.font;
            Matrix3x2fStack matrices = guiGraphics.pose();
            Matrix3x2f backupMatrix = new Matrix3x2f(matrices);

            int scaledX = (int)((startRightX + blankWidth * 3 + 5) * 2 - 5)/2;
            int scaledY = (int)((yPos - 2) * 2 + 40)/2;
            matrices.translate(scaledX, scaledY);
            matrices.scale(0.5f, 0.5f);

            if (unstableOCDuration > 0) { // activate
                int minutes = unstableOCDuration / 60;
                int seconds = unstableOCDuration % 60;
                String timeStr = String.format("%d:%02d", minutes, seconds);
                guiGraphics.text(font,Component.literal(timeStr), (unstableOCDuration < 600 ? 1 : 0), 0, 0xFF7FBEEB, true);
                matrices.set(backupMatrix);

                guiGraphics.item(unstableOverclock, startRightX + blankWidth * 3, yPos);
            }
            else if (unstableOCDuration == 0 && unstableOCCooldown > 0) { // cooldown
                int minutes = unstableOCCooldown / 60;
                int seconds = unstableOCCooldown % 60;
                String timeStr = String.format("%d:%02d", minutes, seconds);
                guiGraphics.text(font,Component.literal(timeStr), (unstableOCDuration < 600 ? 1 : 0), 0, 0xFFFFFFFF, true);
                matrices.set(backupMatrix);

//                guiGraphics.item(unstableOverclock, startRightX + blankWidth * 3, yPos);
                Identifier cooldownID = Identifier.fromNamespaceAndPath("cfm", "textures/item/cooldown.png");
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,cooldownID, startRightX + blankWidth * 3, yPos, 0f, 0f, 16, 16, 16, 16);

            }
            else if (unstableOCDuration == 0 && unstableOCCooldown == 0) { // wait to be activated
                matrices.set(backupMatrix);
                long currentTime = Util.getMillis();
                int[] frameSequence = {0, 1, 2, 1};
                long frameDurationMs = 200;
                int currentFrameIndex = (int)((currentTime / frameDurationMs) % frameSequence.length);
                int frame = frameSequence[currentFrameIndex];

                Identifier waitID;
                switch (frame) {
                    case 1 -> waitID = Identifier.fromNamespaceAndPath("cfm", "textures/item/activated1.png");
                    case 2 -> waitID = Identifier.fromNamespaceAndPath("cfm", "textures/item/activated2.png");
                    default -> waitID = Identifier.fromNamespaceAndPath("cfm", "textures/item/activated0.png");
                }

                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,waitID, startRightX + blankWidth * 3, yPos, 0f, 0f, 16, 16, 16, 16);
            }
            else //dr == -1 && cd == -1
            {
                matrices.set(backupMatrix);
                guiGraphics.item(unstableOverclock, startRightX + blankWidth * 3, yPos);
            }
        }
        // TODO: gcs switch state
    }

    public void detectScreenFishSupplyClose(Screen screen) {
        if (screen instanceof ContainerScreen containerScreen) {
            ChestMenu handler = (ChestMenu) containerScreen.getMenu();
            Container inventory = handler.getContainer();
            if (inventory.getContainerSize() < 40) return;

            hookOverclock = inventory.getItem(12);
            magnetOverclock = inventory.getItem(13);
            rodOverclock = inventory.getItem(14);
            unstableOverclock = inventory.getItem(15);
            supermeOverclock = inventory.getItem(16);
            bait = inventory.getItem(19);
            line = inventory.getItem(37);


            ifHookOCNeedsShow = !hookOverclock.getHoverName().getString().contains("Locked");
            ifMagnetOCNeedsShow = !magnetOverclock.getHoverName().getString().contains("Locked");
            ifRodOCNeedsShow = !rodOverclock.getHoverName().getString().contains("Locked");
            ifUnstableOCNeedsShow = !unstableOverclock.getHoverName().getString().contains("Locked");
            ifBaitNeedsShow = !Objects.equals(bait.getHoverName().getString(), "Bait Slot");
            ifLineNeedsShow = !Objects.equals(line.getHoverName().getString(), "Line Slot");

            if (ifFirstTimeLoading) {
                ifFirstTimeLoading = false;

                List<Component> tooltipLines = supermeOverclock.getTooltipLines(
                        Item.TooltipContext.EMPTY, client.player, TooltipFlag.Default.NORMAL);
                if (!tooltipLines.isEmpty() && tooltipLines.size() > 24
                        && tooltipLines.get(23).getString().contains("Grand Champ Supreme rank")) // rank diff
                    ifSupermeOCNeedsShow = false;
            }
            if (ifLineNeedsShow) {
                if (!line.getHoverName().getString().contains("Empty")) {
                    List<Component> tooltipLines = line.getTooltipLines(
                            Item.TooltipContext.EMPTY, client.player, TooltipFlag.Default.NORMAL);
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
        if (client == null || client.player == null || client.level == null) return;
        if (!ifUnstableOCNeedsShow) return;
        List<Component> tooltipLines = stack.getTooltipLines(
                Item.TooltipContext.EMPTY, client.player, TooltipFlag.Default.NORMAL);
        if (!tooltipLines.isEmpty() && tooltipLines.size() > 24) {
            String rawIfActivate = tooltipLines.getLast().getString();
            if (rawIfActivate.contains("Currently active") || rawIfActivate.contains("On cooldown")) return;
            //cant use oc when on cooldown or active

            for (Component line : tooltipLines) {
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

    public void detectText(Component message) {
        String msg = message.getString();
        Pattern BAITRUNOUT = Pattern.compile("You've run out of your equipped \\[(.+?) Bait]");  // "[]"
        Matcher matcher = BAITRUNOUT.matcher(msg);
        if (matcher.find()) {
            bait = ItemStack.EMPTY;
        }

    }
    // TODO: pause bait number after buying/unboxing, disable bait render after running out of it(chat)
}
