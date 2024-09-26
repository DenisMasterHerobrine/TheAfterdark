package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public class VerticalBlobConfiguration implements FeatureConfig {
    public static final Codec<VerticalBlobConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(BlockState.CODEC
                    .fieldOf("blockOn")
                    .forGetter((v) -> v.blockOn), BlockState.CODEC.fieldOf("blockOn2")
                    .forGetter((v) -> v.blockOn2), BlockState.CODEC.fieldOf("blobMaterial")
                    .forGetter((v) -> v.blobMaterial), IntProvider.createValidatingCodec(1, 1024)
                    .fieldOf("blobMass")
                    .forGetter((v) -> v.blobMass), IntProvider.createValidatingCodec(1, 32)
                    .fieldOf("blobWidth")
                    .forGetter((v) -> v.blobWidth), IntProvider.createValidatingCodec(1, 128)
                    .fieldOf("blobHeight")
                    .forGetter((v) -> v.blobHeight))
                    .apply(fields, VerticalBlobConfiguration::new));

    public final BlockState blockOn;
    public final BlockState blockOn2;
    public final BlockState blobMaterial;
    private final IntProvider blobMass;
    private final IntProvider blobWidth;
    private final IntProvider blobHeight;

    public VerticalBlobConfiguration(BlockState blockOn, BlockState blockOn2, BlockState blobMaterial, IntProvider blobMass, IntProvider blobWidth, IntProvider blobHeight) {
        this.blockOn = blockOn;
        this.blockOn2 = blockOn2;
        this.blobMaterial = blobMaterial;
        this.blobMass = blobMass;
        this.blobWidth = blobWidth;
        this.blobHeight = blobHeight;
    }

    public IntProvider getBlobMass() {return this.blobMass;}
    public IntProvider getBlobWidth() {return this.blobWidth;}
    public IntProvider getBlobHeight() {return this.blobHeight;}

}
