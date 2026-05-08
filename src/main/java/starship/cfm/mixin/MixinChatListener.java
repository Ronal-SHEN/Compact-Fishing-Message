package starship.cfm.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starship.cfm.augmentTracker.AugmentTracker;
import starship.cfm.fishMessage.FishMessage;
import starship.cfm.trevorOpener.TrevorOpener;

@Mixin(ChatListener.class)
public abstract class MixinChatListener {
    @Inject(method = "handlePlayerChatMessage", at = @At("HEAD"))
    private void cacheChatData(PlayerChatMessage message, GameProfile sender,
                               ChatType.Bound params, CallbackInfo ci) {
    }

    @Inject(method = "handleSystemMessage", at = @At("HEAD"), cancellable = true)
    private void maybeCancelMessage(Component message, boolean overlay, CallbackInfo ci) {
        if (FishMessage.getInstance().shouldChatMsgCancel(message)) {
            ci.cancel();
        }
        if (TrevorOpener.getInstance().shouldChatMsgCancel(message)) {
            ci.cancel();
        }
        AugmentTracker.getInstance().detectText(message);
    }

    @ModifyVariable(
            method = "handleSystemMessage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Component modifyGameMessage(Component original) {
        return FishMessage.getInstance().sendGameMsg(original);
    }
}
