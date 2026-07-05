package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class LightVeinConfiguration implements FeatureConfiguration {
    public static final Codec<LightVeinConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(
            BlockStateProvider.CODEC.fieldOf("vein_provider").forGetter(config -> config.veinProvider),
            BlockStateProvider.CODEC.fieldOf("accent_provider").forGetter(config -> config.accentProvider),
            IntProvider.codec(6, 40).fieldOf("length").forGetter(config -> config.length),
            IntProvider.codec(1, 6).fieldOf("radius").forGetter(config -> config.radius),
            IntProvider.codec(2, 28).fieldOf("vertical_range").forGetter(config -> config.verticalRange),
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
