package net.satisfy.lilis_lucky_lures.platform.fabric;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
