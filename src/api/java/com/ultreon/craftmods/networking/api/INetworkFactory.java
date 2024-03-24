package com.ultreon.craftmods.networking.api;

@FunctionalInterface
public interface INetworkFactory {
    void registerPackets(IPacketRegisterContext ctx);
}
