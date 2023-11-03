package com.ultreon.craftmods.networking.test;

import com.ultreon.craftmods.networking.api.Network;
import com.ultreon.craftmods.networking.api.NetworkInit;
import net.fabricmc.loader.api.FabricLoader;

public class TestNetworkInit extends NetworkInit {
    private static Network network;

    public static Network getNetwork() {
        return network;
    }

    @Override
    public void init() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;

        TestNetworkInit.network = new TestNetwork();
    }
}
