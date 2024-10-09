package dev.denismasterherobrine.afterdark.neoforge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = TheAfterdark.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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
