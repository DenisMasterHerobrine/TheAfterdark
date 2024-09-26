package dev.denismasterherobrine.afterdark.features.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CatchingFallConfiguration implements FeatureConfig {
    public static final Codec<CatchingFallConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(FluidState.CODEC.fieldOf("state").
                    forGetter((v) -> v.state), Codec.BOOL
                    .fieldOf("requires_block_below")
                    .orElse(true)
                    .forGetter((v) -> v.requiresBlockBelow), Codec.INT
                    .fieldOf("rock_count")
                    .orElse(4)
                    .forGetter((v) -> v.rockCount), Codec.INT
                    .fieldOf("hole_count").orElse(1)
                    .forGetter((v) -> v.holeCount), Registries.BLOCK.getCodec().listOf()
                    .fieldOf("validBlocks").xmap(ImmutableSet::copyOf, ImmutableList::copyOf)
                    .forGetter((v) -> (ImmutableSet<Block>)v.validBlocks), Registries.BLOCK.getCodec().listOf()
                    .fieldOf("invalidBlocks").xmap(ImmutableSet::copyOf, ImmutableList::copyOf)
                    .forGetter((v) -> (ImmutableSet<Block>) v.validBlocks), Registries.BLOCK.getCodec()
                    .fieldOf("basinMaterial").forGetter((v) -> v.basinMaterial), Registries.BLOCK.getCodec()
                    .fieldOf("basinMaterial2").forGetter((v) -> v.basinMaterial2))
                    .apply(fields, CatchingFallConfiguration::new));

    public final FluidState state;
    public final boolean requiresBlockBelow;
    public final int rockCount;
    public final int holeCount;
    public final Set<Block> validBlocks;
    public final Set<Block> invalidBlocks;
    public final Block basinMaterial;
    public final Block basinMaterial2;

    public CatchingFallConfiguration(FluidState fluidState, boolean requiresBlockBelow, int rockCount, int holeCount, Set<Block> validBlocks, Set<Block> invalidBlocks, Block basinMaterial, Block basinMaterial2) {
        this.state = fluidState;
        this.requiresBlockBelow = requiresBlockBelow;
        this.rockCount = rockCount;
        this.holeCount = holeCount;
        this.validBlocks = validBlocks;
        this.invalidBlocks = invalidBlocks;
        this.basinMaterial = basinMaterial;
        this.basinMaterial2 = basinMaterial2;
    }
}