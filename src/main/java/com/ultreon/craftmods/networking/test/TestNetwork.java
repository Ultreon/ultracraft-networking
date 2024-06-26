package com.ultreon.craftmods.networking.test;

import com.ultreon.craftmods.networking.UcNetworkingMod;
import com.ultreon.craftmods.networking.api.IPacketRegisterContext;
import com.ultreon.craftmods.networking.impl.Network;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestNetwork extends Network {
    private static final String CHANNEL_NAME = "test_channel";

    public TestNetwork() {
        super(UcNetworkingMod.MOD_ID, TestNetwork.CHANNEL_NAME);
    }

    @Override
    protected void registerPackets(IPacketRegisterContext ctx) {
        ctx.register(TestPacket::new);
    }
}
