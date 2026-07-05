package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class DoubleBlockConfiguration implements FeatureConfiguration {
    public static final Codec<DoubleBlockConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(BlockStateProvider.CODEC
                    .fieldOf("to_place")
                    .forGetter((v) -> v.toPlace), BlockStateProvider.CODEC
                    .fieldOf("slab_to_place")
                    .forGetter((v) -> v.slabToPlace))
                    .apply(fields, DoubleBlockConfiguration::new));

    public final BlockStateProvider toPlace;
    public final BlockStateProvider slabToPlace;

    public DoubleBlockConfiguration(BlockStateProvider toPlace, BlockStateProvider slabToPlace) {
        this.toPlace = toPlace;
        this.slabToPlace = slabToPlace;
    }

    public BlockStateProvider toPlace() {
        return this.toPlace;
    }
    public BlockStateProvider slabToPlace() {
        return this.slabToPlace;
    }
}
