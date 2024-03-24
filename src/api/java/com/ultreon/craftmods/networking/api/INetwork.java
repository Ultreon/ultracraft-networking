package com.ultreon.craftmods.networking.api;

import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.packet.IClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.IServerEndpoint;
import com.ultreon.craftmods.networking.api.packet.Packet;

public interface INetwork {
    <T extends Packet<T> & IClientEndpoint> void sendPlayer(T packet, ServerPlayer player);

    <T extends Packet<T> & IClientEndpoint> void sendAll(T packet, ServerPlayer player);

    <T extends Packet<T> & IServerEndpoint> void sendToServer(T packet);

    Identifier getId();
}
