package com.ultreon.craftmods.networking.test;

import com.ultreon.craft.network.PacketBuffer;
import com.ultreon.craftmods.networking.UcNetworkingMod;
import com.ultreon.craftmods.networking.api.packet.PacketToClient;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestPacket extends PacketToClient<TestPacket> {
    public TestPacket() {

    }

    public TestPacket(PacketBuffer buffer) {

    }

    @Override
    protected void handle() {
        UcNetworkingMod.LOGGER.info("Successfully received!");
    }

    @Override
    public void toBytes(PacketBuffer buffer) {

    }
}
