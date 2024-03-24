package com.ultreon.craftmods.networking.api.packet;

import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craftmods.networking.api.IPacketContext;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public abstract non-sealed class PacketToServer<T extends PacketToServer<T>> extends Packet<T> implements IServerEndpoint {
    @Override
    public final boolean handle(Supplier<IPacketContext> context) {
        IPacketContext ctx = context.get();
        if (ctx.getDestination() == EnvType.SERVER)
            ctx.queue(() -> this.handle(Objects.requireNonNull(ctx.getPlayer(), "Server player was not found while on server side")));
        return true;
    }

    protected abstract void handle(@NotNull ServerPlayer sender);
}
