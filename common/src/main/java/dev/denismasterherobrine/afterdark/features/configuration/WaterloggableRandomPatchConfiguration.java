package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class WaterloggableRandomPatchConfiguration implements FeatureConfig {
    public static final Codec<WaterloggableRandomPatchConfiguration> CODEC = RecordCodecBuilder.create(
            (fields) -> fields.group(Codecs.POSITIVE_INT
                    .fieldOf("tries")
                    .orElse(128)
                    .forGetter((v) -> v.tries), Codecs.POSITIVE_INT
                    .fieldOf("xz_spread").orElse(7)
                    .forGetter((v) -> v.xz_spread), Codecs.POSITIVE_INT
                    .fieldOf("y_spread").orElse(3)
                    .forGetter((v) -> v.y_spread), BlockStateProvider.TYPE_CODEC
                    .fieldOf("to_place")
                    .forGetter((v) -> v.to_place))
                    .apply(fields, WaterloggableRandomPatchConfiguration::new));

    public final Integer tries;
    public final Integer xz_spread;
    public final Integer y_spread;
    public final BlockStateProvider to_place;

    public WaterloggableRandomPatchConfiguration(Integer tries, Integer xz_spread, Integer y_spread, BlockStateProvider to_place) {
        this.tries = tries;
        this.xz_spread = xz_spread;
        this.y_spread = y_spread;
        this.to_place = to_place;
    }
}
