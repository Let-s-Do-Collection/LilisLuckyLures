package net.satisfy.lilis_lucky_lures.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;

import static net.satisfy.lilis_lucky_lures.LilisLuckyLures.MOD_ID;

@Mod(MOD_ID)
public class LilisLuckyLuresNeoForge {
    public LilisLuckyLuresNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        EventBusesHooks.whenAvailable(LilisLuckyLures.MOD_ID, IEventBus::start);
        LilisLuckyLures.init();
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class LilisLuckyLuresClient {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> ItemProperties.register(ObjectRegistry.BAMBOO_FISHING_ROD.get(), ResourceLocation.parse("cast"),
                    (itemStack, clientWorld, livingEntity, seed) -> {
                        if (livingEntity == null) return 0.0F;
                        return livingEntity.getMainHandItem() == itemStack && livingEntity instanceof Player
                                && ((Player) livingEntity).fishing != null ? 1.0F : 0.0F;
                    }));
        }
    }
}
