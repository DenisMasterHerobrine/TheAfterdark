package dev.denismasterherobrine.afterdark.features.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public class SpiralConfiguration implements FeatureConfig {
    public static final Codec<SpiralConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(Registries.BLOCK.getCodec().listOf()
                    .fieldOf("validBlocks").xmap(ImmutableSet::copyOf, ImmutableList::copyOf)
                    .forGetter((v) -> (ImmutableSet<Block>)v.validBlocks), BlockState.CODEC
                    .fieldOf("stemMaterial")
                    .forGetter((v) -> v.stemMaterial), BlockState.CODEC
                    .fieldOf("leafMaterial")
                    .forGetter((v) -> v.leafMaterial), IntProvider.createValidatingCodec(1, 1024)
                    .fieldOf("blobMass")
                    .forGetter((v) -> v.blobMass), IntProvider.createValidatingCodec(1, 32)
                    .fieldOf("blobWidth")
                    .forGetter((v) -> v.blobWidth), IntProvider.createValidatingCodec(1, 128)
                    .fieldOf("blobHeight")
                    .forGetter((v) -> v.blobHeight))
                    .apply(fields, SpiralConfiguration::new));

    public final Set<Block> validBlocks;
    public final BlockState stemMaterial;
    public final BlockState leafMaterial;
    private final IntProvider blobMass;
    private final IntProvider blobWidth;
    private final IntProvider blobHeight;

    public SpiralConfiguration(Set<Block> validBlocks, BlockState stemMaterial, BlockState leafMaterial, IntProvider blobMass, IntProvider blobWidth, IntProvider blobHeight) {
        this.validBlocks = validBlocks;
        this.stemMaterial = stemMaterial;
        this.leafMaterial = leafMaterial;
        this.blobMass = blobMass;
        this.blobWidth = blobWidth;
        this.blobHeight = blobHeight;
    }

    public IntProvider getBlobMass() {return this.blobMass;}
    public IntProvider getBlobWidth() {return this.blobWidth;}
    public IntProvider getBlobHeight() {return this.blobHeight;}

}
