package net.satisfy.lilis_lucky_lures.fabric.core.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.satisfy.lilis_lucky_lures.core.compat.rei.LilisLuckyLuresREIClientPlugin;


public class LilisLuckyLuresReiClientPluginFabric implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        LilisLuckyLuresREIClientPlugin.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        LilisLuckyLuresREIClientPlugin.registerDisplays(registry);
    }
}
