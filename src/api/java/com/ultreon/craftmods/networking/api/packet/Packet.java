package com.ultreon.craftmods.networking.api.packet;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.ultreon.craft.network.Connection;
import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craftmods.networking.api.IPacketContext;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract sealed class Packet<T extends Packet<T>> permits BiDirectionalPacket, PacketToClient, PacketToServer {
    protected abstract boolean handle(Supplier<IPacketContext> context);

    @CanIgnoreReturnValue
    public final boolean handlePacket(Supplier<IPacketContext> context) {
        try {
            this.handle(context);
        } catch (Throwable throwable) {
            Connection.LOGGER.error("Couldn't handle packet:", throwable);
        }
        return true;
    }

    public abstract void toBytes(PacketBuffer buffer);
}
