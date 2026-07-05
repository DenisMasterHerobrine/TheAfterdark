package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.DarkTarConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class DarkTarFeature extends Feature<DarkTarConfiguration> {
    public DarkTarFeature(Codec<DarkTarConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DarkTarConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        DarkTarConfiguration config = context.config();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.origin(), 28);
        if (ceiling == null) {
            return false;
        }

        int length = config.length.sample(random);
        BlockPos cursor = ceiling.below();
        BlockPos last = cursor;
        boolean placed = false;

        for (int i = 0; i < length && BlackHoneyFeatureUtil.canReplace(world, cursor); ++i) {
            world.setBlock(cursor, config.columnProvider.getState(random, cursor), 2);
            placed = true;
            last = cursor;

            if (random.nextFloat() < config.sideChance) {
                Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
                BlockPos sidePos = cursor.relative(side);
                if (BlackHoneyFeatureUtil.canReplace(world, sidePos)) {
                    world.setBlock(sidePos, config.sideProvider.getState(random, sidePos), 2);
                }
            }

            cursor = cursor.below();
        }

        if (placed && random.nextFloat() < config.poolChance) {
            spreadPool(world, random, last, config);
        }
        return placed;
    }

    private void spreadPool(WorldGenLevel world, RandomSource random, BlockPos base, DarkTarConfiguration config) {
        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, base, 1, 10);
        if (floor == null) {
            return;
        }
        int radius = config.poolRadius.sample(random);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                BlockPos target = floor.offset(x, -1, z);
                if (!BlackHoneyFeatureUtil.inWorld(world, target)) {
                    continue;
                }
                if (world.getBlockState(target).isSolid()) {
                    BlockState state = config.poolProvider.getState(random, target);
                    world.setBlock(target, state, 2);
                }
            }
        }
    }
}
