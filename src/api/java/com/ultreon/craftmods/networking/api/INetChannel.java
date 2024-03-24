package com.ultreon.craftmods.networking.api;

import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.packet.IClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.IServerEndpoint;
import com.ultreon.craftmods.networking.api.packet.Packet;

import java.util.List;

public interface INetChannel {
    Identifier id();

    <T extends Packet<T> & IClientEndpoint> void sendToClient(ServerPlayer player, T modPacket);

    <T extends Packet<T> & IClientEndpoint> void sendToClients(List<ServerPlayer> players, T modPacket);

    <T extends Packet<T> & IServerEndpoint> void sendToServer(T modPacket);
}
