package net.satisfy.lilis_lucky_lures.core.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.lilis_lucky_lures.client.model.armor.AnglersHatModel;

import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {
    private static final Map<Item, AnglersHatModel> models = new HashMap<>();

    public static Model getHatModel(Item item, ModelPart baseHead) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        AnglersHatModel model = models.computeIfAbsent(item, key -> {
            if (key == ObjectRegistry.ANGLERS_HAT.get()) {
                return new AnglersHatModel<>(modelSet.bakeLayer(AnglersHatModel.LAYER_LOCATION));
            } else {
                return null;
            }
        });

        assert model != null;
        model.copyHead(baseHead);

        return model;
    }
}