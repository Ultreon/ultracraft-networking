package com.ultreon.craftmods.networking.test;

import com.ultreon.craftmods.networking.api.Network;
import com.ultreon.craftmods.networking.api.NetworkInit;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
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
