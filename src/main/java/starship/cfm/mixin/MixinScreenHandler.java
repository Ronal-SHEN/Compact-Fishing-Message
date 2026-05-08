package starship.cfm.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starship.cfm.augmentTracker.AugmentTracker;
import starship.cfm.trevorOpener.TrevorOpener;

@Mixin(AbstractContainerMenu.class)
public abstract class MixinScreenHandler {
    @Inject(method = "doClick", at = @At("HEAD"))
    private void onSlotClick(int slotId, int button, ContainerInput actionType, Player player, CallbackInfo ci) {
        if (player.level().isClientSide()) {
            if (slotId >= 0 && slotId < 100) { // click inside gui // 48 49 50
                if (TrevorOpener.getInstance().ifSUMMAEYScreenOpened && (slotId == 48 || slotId == 49 || slotId == 50))
                    TrevorOpener.getInstance().detectScreenSUMMARYClose();
                Slot slot = ((AbstractContainerMenu) (Object) this).getSlot(slotId);
                ItemStack itemStack = slot.getItem();
                if (itemStack.isEmpty()) return;
                if (TrevorOpener.getInstance().treasure.namePattern.matcher(itemStack.getHoverName().getString()).find()) {
                    if (actionType == ContainerInput.QUICK_MOVE && button == 0) { // left+shift click
                        TrevorOpener.getInstance().recordTreasure(itemStack.getHoverName().getString(), itemStack.getCount());
                    }

                    if (actionType == ContainerInput.PICKUP && button == 0) { // left click
                        TrevorOpener.getInstance().recordTreasure(itemStack.getHoverName().getString(), 1);

                    }
                }
                if (itemStack.getHoverName().getString().contains("Unstable Overclock")) {
                    if (actionType == ContainerInput.QUICK_MOVE && button == 0)
                        AugmentTracker.getInstance().activateUnstableOC(itemStack);
                }
            }

        }
    }
}
