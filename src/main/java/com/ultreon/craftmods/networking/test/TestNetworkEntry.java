package com.ultreon.craftmods.networking.test;

import com.ultreon.craftmods.networking.api.INetworkEntryPoint;
import com.ultreon.craftmods.networking.impl.Network;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestNetworkEntry implements INetworkEntryPoint {
    private static Network network;

    public static Network getNetwork() {
        return network;
    }

    @Override
    public void init() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;

        TestNetworkEntry.network = new TestNetwork();
    }
}
