package com.ultreon.craftmods.networking.impl;

import com.google.errorprone.annotations.CheckReturnValue;
import com.ultreon.craft.network.Connection;
import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craftmods.networking.api.ModPacketContext;
import com.ultreon.craftmods.networking.api.packet.BasePacket;
import com.ultreon.craftmods.networking.api.packet.ClientEndpoint;
import com.ultreon.craftmods.networking.api.packet.ServerEndpoint;
import com.ultreon.libs.commons.v0.Identifier;
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

public class NetworkChannel {
    private static final Map<Identifier, NetworkChannel> CHANNELS = new HashMap<>();
    private final Identifier key;
    private int curId;
    private final Reference2IntMap<Class<? extends BasePacket<?>>> idMap = new Reference2IntArrayMap<>();
    private final Map<Class<? extends BasePacket<?>>, BiConsumer<? extends BasePacket<?>, PacketBuffer>> encoders = new HashMap<>();
    private final Int2ReferenceMap<Function<PacketBuffer, ? extends BasePacket<?>>> decoders = new Int2ReferenceArrayMap<>();
    private final Map<Class<? extends BasePacket<?>>, BiConsumer<? extends BasePacket<?>, Supplier<ModPacketContext>>> consumers = new HashMap<>();

    @Environment(EnvType.CLIENT)
    private Connection c2sConnection;

    private NetworkChannel(Identifier key) {
        this.key = key;
    }

    public static NetworkChannel create(Identifier id) {
        NetworkChannel channel = new NetworkChannel(id);
        NetworkChannel.CHANNELS.put(id, channel);
        return channel;
    }

    @CheckReturnValue
    public static NetworkChannel getChannel(Identifier channelId) {
        return NetworkChannel.CHANNELS.get(channelId);
    }

    @Environment(EnvType.CLIENT)
    public void setC2sConnection(Connection connection) {
        this.c2sConnection = connection;
    }

    public Identifier id() {
        return this.key;
    }

    public <T extends BasePacket<T>> void register(Class<T> clazz, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<ModPacketContext>> packetConsumer) {
        this.curId++;
        this.idMap.put(clazz, this.curId);
        this.encoders.put(clazz, encoder);
        this.decoders.put(this.curId, decoder);
        this.consumers.put(clazz, packetConsumer);
    }

    public void queue(Runnable task) {

    }

    public <T extends BasePacket<T> & ClientEndpoint> void sendToPlayer(ServerPlayer player, T modPacket) {
        player.connection.send(new S2CModPacket(this, modPacket));
    }

    public <T extends BasePacket<T> & ClientEndpoint> void sendToPlayers(List<ServerPlayer> players, T modPacket) {
        for (int i = 0; i < players.size(); i++) {
            ServerPlayer player = players.get(i);
            player.connection.send(new S2CModPacket(this, modPacket), false);
            if (i == players.size() - 1) {
                player.connection.send(new S2CModPacket(this, modPacket), true);
            }
        }
    }

    public <T extends BasePacket<T> & ServerEndpoint> void sendToServer(T modPacket) {
        this.c2sConnection.send(new C2SModPacket(this, modPacket));
    }

    public Function<PacketBuffer, ? extends BasePacket<?>> getDecoder(int id) {
        return this.decoders.get(id);
    }

    public BiConsumer<? extends BasePacket<?>, PacketBuffer> getEncoder(Class<? extends BasePacket<?>> type) {
        return this.encoders.get(type);
    }

    public BiConsumer<? extends BasePacket<?>, Supplier<ModPacketContext>> getConsumer(Class<? extends BasePacket<?>> type) {
        return this.consumers.get(type);
    }

    public int getId(BasePacket<?> packet) {
        return this.idMap.getInt(packet.getClass());
    }
}
