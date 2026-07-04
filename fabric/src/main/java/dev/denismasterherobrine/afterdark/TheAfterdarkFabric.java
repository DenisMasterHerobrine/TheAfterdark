package dev.denismasterherobrine.afterdark;

import dev.denismasterherobrine.afterdark.events.AfterdarkCaveEventManager;
import dev.denismasterherobrine.afterdark.registry_fabric.AfterdarkFabricRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public final class TheAfterdarkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TheAfterdark.init();
        AfterdarkFabricRegistry.register();
        ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerManager().getPlayerList().forEach(AfterdarkCaveEventManager::tick));
    }
}
