package com.ultreon.craftmods.networking.impl;

import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craft.network.PacketContext;
import com.ultreon.craft.network.packets.Packet;
import com.ultreon.craft.network.server.InGameServerPacketHandler;
import com.ultreon.craftmods.networking.api.ModPacketContext;
import com.ultreon.craftmods.networking.api.packet.BasePacket;
import com.ultreon.libs.commons.v0.Identifier;
import net.fabricmc.api.EnvType;

public class C2SModPacket extends Packet<InGameServerPacketHandler> {
    private final Identifier channelId;
    private final BasePacket<?> packet;
    private NetworkChannel channel;

    public C2SModPacket(NetworkChannel channel, BasePacket<?> packet) {
        this.channel = channel;
        this.channelId = channel.id();
        this.packet = packet;
    }

    public C2SModPacket(PacketBuffer buffer) {
        this.channelId = buffer.readId();
        this.channel = NetworkChannel.getChannel(this.channelId);
        this.packet = this.channel.getDecoder(buffer.readUnsignedShort()).apply(buffer);
    }

    @Override
    public void toBytes(PacketBuffer buffer) {
        buffer.writeId(this.channelId);
        buffer.writeShort(this.channel.getId(this.packet));

        this.packet.toBytes(buffer);
    }

    @Override
    public void handle(PacketContext ctx, InGameServerPacketHandler handler) {
        this.packet.handlePacket(() -> new ModPacketContext(this.channel, handler.context().getPlayer(), handler.context().getConnection(), EnvType.SERVER));
    }

    public NetworkChannel getChannel() {
        return this.channel;
    }
}
