package com.ultreon.craftmods.networking.impl;

import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.INetwork;
import com.ultreon.craftmods.networking.api.IPacketRegisterContext;
import com.ultreon.craftmods.networking.api.packet.IClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.IServerEndpoint;
import com.ultreon.craftmods.networking.api.packet.Packet;
import com.ultreon.craftmods.networking.impl.packet.ModNetChannel;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
public abstract class Network implements INetwork {
    private final String modId;
    private final String channelName;

    protected ModNetChannel channel;

    @ApiStatus.Internal
    protected Network(String modId, String channelName) {
        this.modId = modId;
        this.channelName = channelName;

        NetworkManager.registerNetwork(this);
    }

    @Deprecated
    @ApiStatus.Internal
    protected Network(String modId, String channelName, @Deprecated int ignoredVersion) {
        this(modId, channelName);
    }

    public final void init() {
        int id = 0;
        this.channel = ModNetChannel.create(new Identifier(this.namespace(), this.channelName()));

        this.registerPackets(new PacketRegisterContext(this.channel, id));
    }

    protected abstract void registerPackets(IPacketRegisterContext ctx);

    public final String channelName() {
        return this.channelName;
    }

    public final String namespace() {
        return this.modId;
    }

    @Override
    public <T extends Packet<T> & IClientEndpoint> void sendPlayer(T packet, ServerPlayer player) {
        this.channel.sendToClient(player, packet);
    }

    @Override
    public <T extends Packet<T> & IClientEndpoint> void sendAll(T packet, ServerPlayer player) {
        this.channel.sendToClients(List.copyOf(player.getWorld().getServer().getPlayers()), packet);
    }

    @Override
    public <T extends Packet<T> & IServerEndpoint> void sendToServer(T packet) {
        this.channel.sendToServer(packet);
    }

    @Override
    public final Identifier getId() {
        return new Identifier(this.namespace(), this.channelName());
    }
}
