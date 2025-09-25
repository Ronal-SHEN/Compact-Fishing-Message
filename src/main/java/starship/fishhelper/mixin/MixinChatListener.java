package starship.fishhelper.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starship.fishhelper.augmentTracker.AugmentTracker;
import starship.fishhelper.fishMessage.FishMessage;
import starship.fishhelper.trevorOpener.TrevorOpener;

@Mixin(MessageHandler.class)
public abstract class MixinChatListener {
    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void cacheChatData(SignedMessage message, GameProfile sender,
                               MessageType.Parameters params, CallbackInfo ci) {
    }

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void maybeCancelMessage(Text message, boolean overlay, CallbackInfo ci) {
        if (FishMessage.getInstance().shouldChatMsgCancel(message)) {
            ci.cancel();
        }
        if (TrevorOpener.getInstance().shouldChatMsgCancel(message)) {
            ci.cancel();
        }
        AugmentTracker.getInstance().detectText(message);
    }

    @ModifyVariable(
            method = "onGameMessage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text modifyGameMessage(Text original) {
        return FishMessage.getInstance().sendGameMsg(original);
    }
}
