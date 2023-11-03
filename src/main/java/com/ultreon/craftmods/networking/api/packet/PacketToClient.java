package com.ultreon.craftmods.networking.api.packet;

import com.ultreon.craftmods.networking.api.ModPacketContext;
import net.fabricmc.api.EnvType;

import java.util.function.Supplier;

public abstract non-sealed class PacketToClient<T extends PacketToClient<T>> extends BasePacket<T> implements ClientEndpoint {
    public PacketToClient() {
        super();
    }

    @Override
    public final boolean handle(Supplier<ModPacketContext> context) {
        ModPacketContext ctx = context.get();
        if (ctx.getDestination() == EnvType.CLIENT)
            ctx.queue(this::handle);
        return true;
    }

    protected abstract void handle();
}
