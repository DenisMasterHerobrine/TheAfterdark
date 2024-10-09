package dev.denismasterherobrine.afterdark.neoforge;

import dev.denismasterherobrine.afterdark.TheAfterdark;

import dev.denismasterherobrine.afterdark.neoforge.registry.AfterdarkNeoForgeRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(TheAfterdark.MOD_ID)
public final class TheAfterdarkNeoForge {
    public TheAfterdarkNeoForge(IEventBus eventBus) {
        eventBus.addListener(TheAfterdarkNeoForge::setup);
        AfterdarkNeoForgeRegistry.register(eventBus);
    }

    private static void setup(final FMLCommonSetupEvent event) {
        TheAfterdark.init();
    }
}
