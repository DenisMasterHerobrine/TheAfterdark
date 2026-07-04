package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class DarkTarConfiguration implements FeatureConfig {
    public static final Codec<DarkTarConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("column_provider").forGetter(config -> config.columnProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("side_provider").forGetter(config -> config.sideProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("pool_provider").forGetter(config -> config.poolProvider),
            IntProvider.createValidatingCodec(4, 24).fieldOf("length").forGetter(config -> config.length),
            IntProvider.createValidatingCodec(1, 5).fieldOf("pool_radius").forGetter(config -> config.poolRadius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("side_chance").orElse(0.28F).forGetter(config -> config.sideChance),
            Codec.floatRange(0.0F, 1.0F).fieldOf("pool_chance").orElse(0.65F).forGetter(config -> config.poolChance)
    ).apply(fields, DarkTarConfiguration::new));

    public final BlockStateProvider columnProvider;
    public final BlockStateProvider sideProvider;
    public final BlockStateProvider poolProvider;
    public final IntProvider length;
    public final IntProvider poolRadius;
    public final float sideChance;
    public final float poolChance;

    public DarkTarConfiguration(
            BlockStateProvider columnProvider,
            BlockStateProvider sideProvider,
            BlockStateProvider poolProvider,
            IntProvider length,
            IntProvider poolRadius,
            float sideChance,
            float poolChance) {
        this.columnProvider = columnProvider;
        this.sideProvider = sideProvider;
        this.poolProvider = poolProvider;
        this.length = length;
        this.poolRadius = poolRadius;
        this.sideChance = sideChance;
        this.poolChance = poolChance;
    }
}
