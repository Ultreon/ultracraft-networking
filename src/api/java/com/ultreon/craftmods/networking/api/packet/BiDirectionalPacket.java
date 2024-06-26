package com.ultreon.craftmods.networking.api.packet;

import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craftmods.networking.api.IPacketContext;

import java.util.function.Supplier;

public abstract non-sealed class BiDirectionalPacket<T extends BiDirectionalPacket<T>> extends Packet<T> implements IClientEndpoint, IServerEndpoint {
    public BiDirectionalPacket() {
        super();
    }

    @Override
    public final boolean handle(Supplier<IPacketContext> context) {
        IPacketContext ctx = context.get();
        switch (ctx.getDestination()) {
            case CLIENT -> ctx.queue(this::handleClient);
            case SERVER -> ctx.queue(() -> this.handleServer(ctx.getPlayer()));
            default -> {
            }
        }
        return true;
    }

    protected abstract void handleClient();

    protected abstract void handleServer(ServerPlayer sender);
}
