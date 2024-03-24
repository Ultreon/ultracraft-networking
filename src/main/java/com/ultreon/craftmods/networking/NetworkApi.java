package com.ultreon.craftmods.networking;

import com.ultreon.craftmods.networking.api.INetwork;
import com.ultreon.craftmods.networking.api.INetworkApi;
import com.ultreon.craftmods.networking.api.INetworkFactory;
import com.ultreon.craftmods.networking.api.IPacketRegisterContext;
import com.ultreon.craftmods.networking.impl.Network;

public class NetworkApi implements INetworkApi {
    @Override
    public INetwork createNetwork(INetworkFactory network, String modId, String channelName) {
        return new Network(modId, channelName) {
            @Override
            protected void registerPackets(IPacketRegisterContext ctx) {
                network.registerPackets(ctx);
            }
        };
    }
}
