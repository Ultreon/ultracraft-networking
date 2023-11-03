package com.ultreon.craftmods.networking.test;

import com.ultreon.craftmods.networking.UcNetworkingMod;
import com.ultreon.craftmods.networking.api.Network;
import com.ultreon.craftmods.networking.api.PacketRegisterContext;

public class TestNetwork extends Network {
    private static final String CHANNEL_NAME = "test_channel";

    public TestNetwork() {
        super(UcNetworkingMod.MOD_ID, TestNetwork.CHANNEL_NAME);
    }

    @Override
    protected void registerPackets(PacketRegisterContext ctx) {
        ctx.register(TestPacket::new);
    }
}
