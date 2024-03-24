package com.ultreon.craftmods.networking.impl.packet;

import com.google.errorprone.annotations.CheckReturnValue;
import com.ultreon.craft.network.Connection;
import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craft.util.Identifier;
import com.ultreon.craftmods.networking.api.INetChannel;
import com.ultreon.craftmods.networking.api.IPacketContext;
import com.ultreon.craftmods.networking.api.packet.IClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.IServerEndpoint;
import com.ultreon.craftmods.networking.api.packet.Packet;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetChannel implements INetChannel {
    private static final Map<Identifier, ModNetChannel> CHANNELS = new HashMap<>();
    private final Identifier key;
    private int curId;
    private final Reference2IntMap<Class<? extends Packet<?>>> idMap = new Reference2IntArrayMap<>();
    private final Map<Class<? extends Packet<?>>, BiConsumer<? extends Packet<?>, PacketBuffer>> encoders = new HashMap<>();
    private final Int2ReferenceMap<Function<PacketBuffer, ? extends Packet<?>>> decoders = new Int2ReferenceArrayMap<>();
    private final Map<Class<? extends Packet<?>>, BiConsumer<? extends Packet<?>, Supplier<IPacketContext>>> consumers = new HashMap<>();

    @Environment(EnvType.CLIENT)
    private Connection c2sConnection;

    private ModNetChannel(Identifier key) {
        this.key = key;
    }

    public static ModNetChannel create(Identifier id) {
        ModNetChannel channel = new ModNetChannel(id);
        ModNetChannel.CHANNELS.put(id, channel);
        return channel;
    }

    @CheckReturnValue
    public static ModNetChannel getChannel(Identifier channelId) {
        return ModNetChannel.CHANNELS.get(channelId);
    }

    @Environment(EnvType.CLIENT)
    public void setC2sConnection(Connection connection) {
        this.c2sConnection = connection;
    }

    @Override
    public Identifier id() {
        return this.key;
    }

    public <T extends Packet<T>> void register(Class<T> clazz, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<IPacketContext>> packetConsumer) {
        this.curId++;
        this.idMap.put(clazz, this.curId);
        this.encoders.put(clazz, encoder);
        this.decoders.put(this.curId, decoder);
        this.consumers.put(clazz, packetConsumer);
    }

    public void queue(Runnable task) {

    }

    @Override
    public <T extends Packet<T> & IClientEndpoint> void sendToClient(ServerPlayer player, T modPacket) {
        player.connection.send(new S2CModPacket(this, modPacket));
    }

    @Override
    public <T extends Packet<T> & IClientEndpoint> void sendToClients(List<ServerPlayer> players, T modPacket) {
        for (int i = 0; i < players.size(); i++) {
            ServerPlayer player = players.get(i);
            player.connection.send(new S2CModPacket(this, modPacket), false);
            if (i == players.size() - 1) {
                player.connection.send(new S2CModPacket(this, modPacket), true);
            }
        }
    }

    @Override
    public <T extends Packet<T> & IServerEndpoint> void sendToServer(T modPacket) {
        this.c2sConnection.send(new C2SModPacket(this, modPacket));
    }

    public Function<PacketBuffer, ? extends Packet<?>> getDecoder(int id) {
        return this.decoders.get(id);
    }

    public BiConsumer<? extends Packet<?>, PacketBuffer> getEncoder(Class<? extends Packet<?>> type) {
        return this.encoders.get(type);
    }

    public BiConsumer<? extends Packet<?>, Supplier<IPacketContext>> getConsumer(Class<? extends Packet<?>> type) {
        return this.consumers.get(type);
    }

    public int getId(Packet<?> packet) {
        return this.idMap.getInt(packet.getClass());
    }
}
