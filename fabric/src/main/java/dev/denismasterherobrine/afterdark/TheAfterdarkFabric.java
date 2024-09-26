package dev.denismasterherobrine.afterdark;

import dev.denismasterherobrine.afterdark.registry_fabric.AfterdarkFabricRegistry;
import net.fabricmc.api.ModInitializer;

public final class TheAfterdarkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TheAfterdark.init();
        AfterdarkFabricRegistry.register();
    }
}
