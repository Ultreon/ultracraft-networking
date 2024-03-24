package com.ultreon.craftmods.networking.api;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craftmods.networking.api.packet.Packet;

import java.util.function.Function;

public interface IPacketRegisterContext {

    @CanIgnoreReturnValue
    @SuppressWarnings("unchecked")
    <T extends Packet<T>> int register(Function<PacketBuffer, T> construct, T... type);
}
