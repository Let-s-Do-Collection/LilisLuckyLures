package net.satisfy.lilis_lucky_lures.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;

import static net.satisfy.lilis_lucky_lures.LilisLuckyLures.MOD_ID;

@Mod(MOD_ID)
public class LilisLuckyLuresForge {
    public LilisLuckyLuresForge() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LilisLuckyLures.init();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class LilisLuckyLuresClient {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> ItemProperties.register(ObjectRegistry.BAMBOO_FISHING_ROD.get(), new ResourceLocation("cast"),
                    (itemStack, clientWorld, livingEntity, seed) -> {
                        if (livingEntity == null) return 0.0F;
                        return livingEntity.getMainHandItem() == itemStack && livingEntity instanceof Player
                                && ((Player) livingEntity).fishing != null ? 1.0F : 0.0F;
                    }));
        }
    }
}
