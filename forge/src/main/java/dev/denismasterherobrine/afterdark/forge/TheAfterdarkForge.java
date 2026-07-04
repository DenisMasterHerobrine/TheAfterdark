package dev.denismasterherobrine.afterdark.forge;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.events.AfterdarkCaveEventManager;
import dev.denismasterherobrine.afterdark.forge.registry.AfterdarkForgeRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TheAfterdark.MOD_ID)
public final class TheAfterdarkForge {
    public TheAfterdarkForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        AfterdarkForgeRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        TheAfterdark.init();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof net.minecraft.server.network.ServerPlayerEntity player) {
            AfterdarkCaveEventManager.tick(player);
        }
    }
}
