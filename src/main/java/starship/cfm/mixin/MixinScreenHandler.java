package starship.cfm.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starship.cfm.augmentTracker.AugmentTracker;
import starship.cfm.trevorOpener.TrevorOpener;

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler {
    @Inject(method = "internalOnSlotClick", at = @At("HEAD"))
    private void onSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (player.getEntityWorld().isClient()) {
            if (slotId >= 0 && slotId < 100) { // click inside gui // 48 49 50
                if (TrevorOpener.getInstance().ifSUMMAEYScreenOpened && (slotId == 48 || slotId == 49 || slotId == 50))
                    TrevorOpener.getInstance().detectScreenSUMMARYClose();
                Slot slot = ((ScreenHandler) (Object) this).getSlot(slotId);
                ItemStack itemStack = slot.getStack();
                if (itemStack.isEmpty()) return;
                if (TrevorOpener.getInstance().treasure.namePattern.matcher(itemStack.getName().getString()).find()) {
                    if (actionType == SlotActionType.QUICK_MOVE && button == 0) { // left+shift click
                        TrevorOpener.getInstance().recordTreasure(itemStack.getName().getString(), itemStack.getCount());
                    }

                    if (actionType == SlotActionType.PICKUP && button == 0) { // left click
                        TrevorOpener.getInstance().recordTreasure(itemStack.getName().getString(), 1);

                    }
                }
                if (itemStack.getName().getString().contains("Unstable Overclock")) {
                    if (actionType == SlotActionType.QUICK_MOVE && button == 0)
                        AugmentTracker.getInstance().activateUnstableOC(itemStack);
                }
            }

        }
    }
}
