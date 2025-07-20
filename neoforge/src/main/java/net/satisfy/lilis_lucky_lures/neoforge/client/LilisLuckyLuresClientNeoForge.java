package net.satisfy.lilis_lucky_lures.neoforge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.client.LilisLuckyLuresClient;

@Mod.EventBusSubscriber(modid = LilisLuckyLures.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LilisLuckyLuresClientNeoForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        LilisLuckyLuresClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LilisLuckyLuresClient.onInitializeClient();
    }
}
