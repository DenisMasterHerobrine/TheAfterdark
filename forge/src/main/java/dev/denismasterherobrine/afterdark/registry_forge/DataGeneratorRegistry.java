package dev.denismasterherobrine.afterdark.registry_forge;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TheAfterdark.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorRegistry {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        DataOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModWorldgenProvider(output, lookupProvider));
    }
}
