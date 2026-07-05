package dev.denismasterherobrine.afterdark.neoforge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class DataGeneratorRegistry {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModWorldgenProvider(output, lookupProvider));
    }
}

