package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.LargeDripstoneFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record PillarFeatureConfiguration(
        LargeDripstoneFeatureConfig large,
        Optional<BlockStateProvider> pillarProvider)
        implements FeatureConfig {

    public static final Codec<PillarFeatureConfiguration> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<PillarFeatureConfiguration, T>> decode(DynamicOps<T> ops, T input) {
            Dynamic<T> dyn = new Dynamic<>(ops, input);
            Optional<BlockStateProvider> pillarOpt = dyn.get("pillar_provider")
                    .result()
                    .flatMap(pillarDyn -> BlockStateProvider.TYPE_CODEC.decode(ops, pillarDyn.getValue()).result()
                            .map(Pair::getFirst));
            Dynamic<T> baseDyn = dyn.remove("pillar_provider");
            return LargeDripstoneFeatureConfig.CODEC.decode(ops, baseDyn.getValue())
                    .map(pair -> Pair.of(new PillarFeatureConfiguration(pair.getFirst(), pillarOpt), pair.getSecond()));
        }

        @Override
        public <T> DataResult<T> encode(PillarFeatureConfiguration cfg, DynamicOps<T> ops, T prefix) {
            DataResult<T> baseEnc = LargeDripstoneFeatureConfig.CODEC.encode(cfg.large(), ops, ops.emptyMap());
            if (cfg.pillarProvider.isEmpty()) {
                return baseEnc;
            }
            return baseEnc.flatMap(
                    baseT -> BlockStateProvider.TYPE_CODEC.encode(cfg.pillarProvider.get(), ops, ops.emptyMap())
                            .flatMap(pillarT -> {
                                Dynamic<T> merged = new Dynamic<>(ops, baseT).set("pillar_provider", new Dynamic<>(ops, pillarT));
                                return DataResult.success(merged.getValue());
                            }));
        }
    };

    public LargeDripstoneFeatureConfig base() {
        return large;
    }
}
