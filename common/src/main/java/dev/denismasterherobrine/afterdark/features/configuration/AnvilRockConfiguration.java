package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;

public class AnvilRockConfiguration implements FeatureConfig {
    public static final Codec<AnvilRockConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(PredicatedStateProvider.CODEC
            .fieldOf("material")
            .forGetter(AnvilRockConfiguration::getMaterial), IntProvider.createValidatingCodec(1, 3)
            .fieldOf("radius")
            .forGetter((v) -> v.radius), IntProvider.createValidatingCodec(3, 64)
            .fieldOf("height")
            .forGetter((v) -> v.height), IntProvider.createValidatingCodec(0, 16)
            .fieldOf("stretch")
            .forGetter((v) -> v.stretch))
            .apply(fields, AnvilRockConfiguration::new));

    public final PredicatedStateProvider material;
    private final IntProvider radius;
    private final IntProvider height;
    private final IntProvider stretch;

    public AnvilRockConfiguration(PredicatedStateProvider material, IntProvider radius, IntProvider height, IntProvider stretch) {
        this.material = material;
        this.radius = radius;
        this.height = height;
        this.stretch = stretch;
    }

    public PredicatedStateProvider getMaterial() {return this.material;}
    public IntProvider getRadius() {return this.radius;}
    public IntProvider getHeight() {return this.height;}
    public IntProvider getStretch() {return this.stretch;}
}
