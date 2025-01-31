package net.satisfy.lilis_lucky_lures;

import net.satisfy.lilis_lucky_lures.core.registry.*;


public class LilisLuckyLures {
    public static final String MOD_ID = "lilis_lucky_lures";

    public static void init() {
        EntityTypeRegistry.init();
        ObjectRegistry.init();
        RecipeTypeRegistry.init();
        SoundEventRegistry.init();
        TabRegistry.init();
    }
}
