package com.ultreon.craftmods.networking.api;

import com.google.common.collect.Lists;
import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craftmods.networking.api.packet.BasePacket;
import com.ultreon.craftmods.networking.api.packet.ClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.ServerEndpoint;
import com.ultreon.craftmods.networking.impl.NetworkChannel;
import com.ultreon.libs.commons.v0.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
public abstract class Network {
    private final String modId;
    private final String channelName;

    protected NetworkChannel channel;

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
        this.channel = NetworkChannel.create(new Identifier(this.namespace(), this.channelName()));

        this.registerPackets(new PacketRegisterContext(this.channel, id));
    }

    protected abstract void registerPackets(PacketRegisterContext ctx);

    public final String channelName() {
        return this.channelName;
    }

    public final String namespace() {
        return this.modId;
    }

    public <T extends BasePacket<T> & ClientEndpoint> void sendPlayer(T packet, ServerPlayer player) {
        this.channel.sendToPlayer(player, packet);
    }

    public <T extends BasePacket<T> & ClientEndpoint> void sendAll(T packet, ServerPlayer player) {
        this.channel.sendToPlayers(List.copyOf(player.getWorld().getServer().getPlayers()), packet);
    }

    public <T extends BasePacket<T> & ServerEndpoint> void sendToServer(T packet) {
        this.channel.sendToServer(packet);
    }

    public final Identifier getId() {
        return new Identifier(this.namespace(), this.channelName());
    }
}
