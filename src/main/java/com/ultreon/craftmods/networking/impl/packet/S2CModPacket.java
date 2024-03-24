package com.ultreon.craftmods.networking.impl.packet;

import com.ultreon.craft.client.network.InGameClientPacketHandlerImpl;
import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craft.network.PacketContext;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.packet.Packet;
import com.ultreon.craftmods.networking.impl.ModPacketContext;
import net.fabricmc.api.EnvType;

public class S2CModPacket extends com.ultreon.craft.network.packets.Packet<InGameClientPacketHandlerImpl> {
    private final Identifier channelId;
    private final Packet<?> packet;
    private final ModNetChannel channel;

    public S2CModPacket(ModNetChannel channel, Packet<?> packet) {
        this.channel = channel;
        this.channelId = channel.id();
        this.packet = packet;
    }

    public S2CModPacket(PacketBuffer buffer) {
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
    public void handle(PacketContext ctx, InGameClientPacketHandlerImpl handler) {
        this.packet.handlePacket(() -> new ModPacketContext(this.channel, null, handler.context().getConnection(), EnvType.CLIENT));
    }
}
