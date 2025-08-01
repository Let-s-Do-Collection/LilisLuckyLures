package net.satisfy.lilis_lucky_lures.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class SoundEventRegistry {

    private static final Registrar<SoundEvent> SOUND_EVENTS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.SOUND_EVENT).getRegistrar();


    public static void init() {}

    private static RegistrySupplier<SoundEvent> create() {
        ResourceLocation id = LilisLuckyLuresIdentifier.identifier("tea_kettle_boiling");

        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
