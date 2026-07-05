package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class WaterloggableRandomPatchConfiguration implements FeatureConfiguration {
    public static final Codec<WaterloggableRandomPatchConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(ExtraCodecs.POSITIVE_INT
                    .fieldOf("tries")
                    .orElse(128)
                    .forGetter((v) -> v.tries), ExtraCodecs.POSITIVE_INT
                    .fieldOf("xz_spread").orElse(7)
                    .forGetter((v) -> v.xz_spread), ExtraCodecs.POSITIVE_INT
                    .fieldOf("y_spread").orElse(3)
                    .forGetter((v) -> v.y_spread), BlockStateProvider.CODEC
                    .fieldOf("to_place")
                    .forGetter((v) -> v.to_place))
                    .apply(fields, WaterloggableRandomPatchConfiguration::new));

    public final int tries;
    public final int xz_spread;
    public final int y_spread;
    public final BlockStateProvider to_place;

    public WaterloggableRandomPatchConfiguration(int tries, int xz_spread, int y_spread, BlockStateProvider to_place) {
        this.tries = tries;
        this.xz_spread = xz_spread;
        this.y_spread = y_spread;
        this.to_place = to_place;
    }
}
