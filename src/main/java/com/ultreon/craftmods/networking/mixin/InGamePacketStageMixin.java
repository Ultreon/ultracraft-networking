package com.ultreon.craftmods.networking.mixin;

import com.ultreon.craft.network.stage.InGamePacketStage;
import com.ultreon.craft.network.stage.PacketStage;
import com.ultreon.craftmods.networking.impl.packet.C2SModPacket;
import com.ultreon.craftmods.networking.impl.packet.S2CModPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGamePacketStage.class)
public abstract class InGamePacketStageMixin extends PacketStage {
    @Inject(method = "registerPackets", at = @At("TAIL"))
    public void registerPackets(CallbackInfo ci) {
        this.addServerBound(C2SModPacket::new);
        this.addClientBound(S2CModPacket::new);
    }
}
