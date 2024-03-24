package com.ultreon.craftmods.networking.impl.packet;

import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craft.network.PacketContext;
import com.ultreon.craft.network.server.InGameServerPacketHandler;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.packet.Packet;
import com.ultreon.craftmods.networking.impl.ModPacketContext;
import net.fabricmc.api.EnvType;

public class C2SModPacket extends com.ultreon.craft.network.packets.Packet<InGameServerPacketHandler> {
    private final Identifier channelId;
    private final Packet<?> packet;
    private ModNetChannel channel;

    public C2SModPacket(ModNetChannel channel, Packet<?> packet) {
        this.channel = channel;
        this.channelId = channel.id();
        this.packet = packet;
    }

    public C2SModPacket(PacketBuffer buffer) {
        this.channelId = buffer.readId();
        this.channel = ModNetChannel.getChannel(this.channelId);
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

    public ModNetChannel getChannel() {
        return this.channel;
    }
}
