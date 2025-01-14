package net.satisfy.lilis_lucky_lures.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;

@Mod(LilisLuckyLures.MOD_ID)
public class LilisLuckyLuresForge {
    public LilisLuckyLuresForge() {
        EventBuses.registerModEventBus(LilisLuckyLures.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LilisLuckyLures.init();
    }
}
