package dev.denismasterherobrine.afterdark.neoforge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldgenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, AfterdarkRegistry::bootstrapDimensionType);

    public ModWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TheAfterdark.MOD_ID));
    }
}

