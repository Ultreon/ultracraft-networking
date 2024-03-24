package com.ultreon.craftmods.networking;

import com.ultreon.craft.ModInit;
import com.ultreon.craft.events.PlayerEvents;
import com.ultreon.craftmods.networking.api.INetworkEntryPoint;
import com.ultreon.craftmods.networking.impl.Network;
import com.ultreon.craftmods.networking.impl.NetworkManager;
import com.ultreon.craftmods.networking.test.TestNetworkEntry;
import com.ultreon.craftmods.networking.test.TestPacket;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
public class UcNetworkingMod implements ModInit {
    public static final String MOD_ID = "uc_networking";
    public static final Logger LOGGER = LoggerFactory.getLogger(UcNetworkingMod.class);

    @Override
    public void onInitialize() {
        // Load the network services using Java's service loader.
        FabricLoader.getInstance().invokeEntrypoints("uc_networking:init", INetworkEntryPoint.class, INetworkEntryPoint::init);

        for (INetworkEntryPoint networkInit : ServiceLoader.load(INetworkEntryPoint.class)) {
            networkInit.init();
        }

        NetworkManager.init();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PlayerEvents.PLAYER_JOINED.subscribe(player -> {
                Network network = TestNetworkEntry.getNetwork();
                network.sendPlayer(new TestPacket(), player);
            });
        }
    }
}
