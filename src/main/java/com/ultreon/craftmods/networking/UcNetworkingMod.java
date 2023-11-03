package com.ultreon.craftmods.networking;

import com.ultreon.craft.ModInit;
import com.ultreon.craft.events.PlayerEvents;
import com.ultreon.craftmods.networking.api.Network;
import com.ultreon.craftmods.networking.api.NetworkInit;
import com.ultreon.craftmods.networking.api.NetworkManager;
import com.ultreon.craftmods.networking.test.TestNetworkInit;
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
        FabricLoader.getInstance().invokeEntrypoints("uc_networking:init", NetworkInit.class, NetworkInit::init);

        for (NetworkInit networkInit : ServiceLoader.load(NetworkInit.class)) {
            networkInit.init();
        }

        NetworkManager.init();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PlayerEvents.PLAYER_JOINED.listen(player -> {
                Network network = TestNetworkInit.getNetwork();
                network.sendPlayer(new TestPacket(), player);
            });
        }
    }
}
