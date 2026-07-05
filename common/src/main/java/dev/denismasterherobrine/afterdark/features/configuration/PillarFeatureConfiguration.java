package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record PillarFeatureConfiguration(
        LargeDripstoneConfiguration large,
        Optional<BlockStateProvider> pillarProvider)
        implements FeatureConfiguration {

    public static final Codec<PillarFeatureConfiguration> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<PillarFeatureConfiguration, T>> decode(DynamicOps<T> ops, T input) {
            Dynamic<T> dyn = new Dynamic<>(ops, input);
            Optional<BlockStateProvider> pillarOpt = dyn.get("pillar_provider")
                    .result()
                    .flatMap(pillarDyn -> BlockStateProvider.CODEC.decode(ops, pillarDyn.getValue()).result()
                            .map(Pair::getFirst));
            Dynamic<T> baseDyn = dyn.remove("pillar_provider");
            return LargeDripstoneConfiguration.CODEC.decode(ops, baseDyn.getValue())
                    .map(pair -> Pair.of(new PillarFeatureConfiguration(pair.getFirst(), pillarOpt), pair.getSecond()));
        }

        @Override
        public <T> DataResult<T> encode(PillarFeatureConfiguration cfg, DynamicOps<T> ops, T prefix) {
            DataResult<T> baseEnc = LargeDripstoneConfiguration.CODEC.encode(cfg.large(), ops, ops.emptyMap());
            if (cfg.pillarProvider.isEmpty()) {
                return baseEnc;
            }
            return baseEnc.flatMap(
                    baseT -> BlockStateProvider.CODEC.encode(cfg.pillarProvider.get(), ops, ops.emptyMap())
                            .flatMap(pillarT -> {
                                Dynamic<T> merged = new Dynamic<>(ops, baseT).set("pillar_provider", new Dynamic<>(ops, pillarT));
                                return DataResult.success(merged.getValue());
                            }));
        }
    };

    public LargeDripstoneConfiguration base() {
        return large;
    }
}
