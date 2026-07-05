package dev.denismasterherobrine.afterdark.neoforge;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.events.AfterdarkCaveEventManager;
import dev.denismasterherobrine.afterdark.neoforge.loot.lootTables.NeoForgeLootModifier;
import dev.denismasterherobrine.afterdark.neoforge.registry.AfterdarkNeoForgeRegistry;
import dev.denismasterherobrine.afterdark.neoforge.registry.DataGeneratorRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(TheAfterdark.MOD_ID)
public final class TheAfterdarkNeoForge {
    public TheAfterdarkNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::setup);
        eventBus.addListener(DataGeneratorRegistry::gatherData);
        AfterdarkNeoForgeRegistry.register(eventBus);
        NeoForge.EVENT_BUS.addListener(NeoForgeLootModifier::onLootTableLoad);
        NeoForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        TheAfterdark.init();
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
            AfterdarkCaveEventManager.tick(player);
        }
    }
}

