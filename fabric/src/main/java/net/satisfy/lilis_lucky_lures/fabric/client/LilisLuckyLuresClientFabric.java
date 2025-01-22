package net.satisfy.lilis_lucky_lures.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.lilis_lucky_lures.client.LilisLuckyLuresClient;

public class LilisLuckyLuresClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LilisLuckyLuresClient.preInitClient();
        LilisLuckyLuresClient.onInitializeClient();
    }
}
