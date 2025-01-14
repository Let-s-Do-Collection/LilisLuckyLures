package net.satisfy.lilis_lucky_lures;

import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.ObjectRegistry;
import net.satisfy.lilis_lucky_lures.core.init.SoundEventRegistry;
import net.satisfy.lilis_lucky_lures.core.init.TabRegistry;

public class LilisLuckyLures {
    public static final String MOD_ID = "lilis_lucky_lures";

    public static void init() {
        EntityTypeRegistry.init();
        ObjectRegistry.init();
        SoundEventRegistry.init();
        TabRegistry.init();
    }
}
