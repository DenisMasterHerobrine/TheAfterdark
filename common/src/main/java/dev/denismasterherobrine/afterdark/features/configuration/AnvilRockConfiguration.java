package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class AnvilRockConfiguration implements FeatureConfiguration {
    public static final Codec<AnvilRockConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(RuleBasedBlockStateProvider.CODEC
            .fieldOf("material")
            .forGetter(AnvilRockConfiguration::getMaterial), IntProvider.codec(1, 3)
            .fieldOf("radius")
            .forGetter((v) -> v.radius), IntProvider.codec(3, 64)
            .fieldOf("height")
            .forGetter((v) -> v.height), IntProvider.codec(0, 16)
            .fieldOf("stretch")
            .forGetter((v) -> v.stretch))
            .apply(fields, AnvilRockConfiguration::new));

    public final RuleBasedBlockStateProvider material;
    private final IntProvider radius;
    private final IntProvider height;
    private final IntProvider stretch;

    public AnvilRockConfiguration(RuleBasedBlockStateProvider material, IntProvider radius, IntProvider height, IntProvider stretch) {
        this.material = material;
        this.radius = radius;
        this.height = height;
        this.stretch = stretch;
    }

    public RuleBasedBlockStateProvider getMaterial() {return this.material;}
    public IntProvider getRadius() {return this.radius;}
    public IntProvider getHeight() {return this.height;}
    public IntProvider getStretch() {return this.stretch;}
}
