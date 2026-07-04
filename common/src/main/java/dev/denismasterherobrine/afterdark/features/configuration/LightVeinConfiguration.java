package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class LightVeinConfiguration implements FeatureConfig {
    public static final Codec<LightVeinConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("vein_provider").forGetter(config -> config.veinProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("accent_provider").forGetter(config -> config.accentProvider),
            IntProvider.createValidatingCodec(6, 40).fieldOf("length").forGetter(config -> config.length),
            IntProvider.createValidatingCodec(1, 6).fieldOf("radius").forGetter(config -> config.radius),
            IntProvider.createValidatingCodec(2, 28).fieldOf("vertical_range").forGetter(config -> config.verticalRange),
            Codec.floatRange(0.0F, 1.0F).fieldOf("accent_chance").orElse(0.18F).forGetter(config -> config.accentChance)
    ).apply(fields, LightVeinConfiguration::new));

    public final BlockStateProvider veinProvider;
    public final BlockStateProvider accentProvider;
    public final IntProvider length;
    public final IntProvider radius;
    public final IntProvider verticalRange;
    public final float accentChance;

    public LightVeinConfiguration(
            BlockStateProvider veinProvider,
            BlockStateProvider accentProvider,
            IntProvider length,
            IntProvider radius,
            IntProvider verticalRange,
            float accentChance) {
        this.veinProvider = veinProvider;
        this.accentProvider = accentProvider;
        this.length = length;
        this.radius = radius;
        this.verticalRange = verticalRange;
        this.accentChance = accentChance;
    }
}
