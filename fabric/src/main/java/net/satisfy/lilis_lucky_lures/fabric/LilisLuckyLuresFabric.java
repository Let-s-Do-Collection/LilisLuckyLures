package net.satisfy.lilis_lucky_lures.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;

public class LilisLuckyLuresFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LilisLuckyLures.init();
    }
}
