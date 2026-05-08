package starship.cfm.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starship.cfm.fishMessage.FishMessage;


@Environment(EnvType.CLIENT)
@Mixin(value = ChatComponent.class, priority = 1000)
public abstract class MixinChatHud {
    @Inject(
            method = "addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelMessage(Component m, MessageSignature signatureData, GuiMessageTag indicator, CallbackInfo ci) {
        if (FishMessage.getInstance().shouldHistoryChatCancel(m)) {
            ci.cancel();
        }

    }

    @ModifyVariable(
            method = "addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Component modifyMessage(Component m) {
//        m = FishMessage.getInstance().sendChatMsg(m);
        return m;
    }
}
