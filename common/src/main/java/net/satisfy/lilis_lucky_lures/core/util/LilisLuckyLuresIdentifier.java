package net.satisfy.lilis_lucky_lures.core.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;

public class LilisLuckyLuresIdentifier {

    public static ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(LilisLuckyLures.MOD_ID, path);
    }
}
