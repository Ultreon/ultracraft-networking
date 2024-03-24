package com.ultreon.craftmods.networking.api;

import com.ultreon.craft.server.player.ServerPlayer;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import net.fabricmc.api.EnvType;

public interface IPacketContext {
    EnvType getDestination();

    void queue(Runnable function);

    @Nullable
    ServerPlayer getPlayer();
}
