package com.ultreon.craftmods.networking.impl;

import com.ultreon.craft.network.Connection;
import com.ultreon.craft.network.PacketContext;
import com.ultreon.craft.server.player.ServerPlayer;
import com.ultreon.craftmods.networking.api.IPacketContext;
import com.ultreon.craftmods.networking.impl.packet.ModNetChannel;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModPacketContext extends PacketContext implements IPacketContext {
    @NotNull
    private final ModNetChannel channel;

    public ModPacketContext(@NotNull ModNetChannel channel, @Nullable ServerPlayer player, @NotNull Connection connection, @NotNull EnvType environment) {
        super(player, connection, environment);
        this.channel = channel;
    }

    public @NotNull ModNetChannel getChannel() {
        return this.channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModPacketContext that = (ModPacketContext) o;
        return Objects.equals(this.getChannel(), that.getChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getChannel());
    }
}
