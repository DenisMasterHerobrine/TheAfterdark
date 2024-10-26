package dev.denismasterherobrine.afterdark.forge;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.forge.registry.AfterdarkForgeRegistry;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TheAfterdark.MOD_ID)
public final class TheAfterdarkForge {
    public TheAfterdarkForge(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::setup);
        AfterdarkForgeRegistry.register(context.getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        TheAfterdark.init();
    }
}
