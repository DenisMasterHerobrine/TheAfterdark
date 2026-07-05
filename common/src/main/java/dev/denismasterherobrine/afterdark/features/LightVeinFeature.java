package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.LightVeinConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LightVeinFeature extends Feature<LightVeinConfiguration> {
    public LightVeinFeature(Codec<LightVeinConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LightVeinConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        LightVeinConfiguration config = context.config();
        BlockPos anchor = findAnchor(world, context.origin(), config.verticalRange.sample(random));
        if (anchor == null) {
            return false;
        }

        int length = config.length.sample(random);
        int radius = config.radius.sample(random);
        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        BlockPos cursor = anchor;
        boolean placed = false;

        for (int i = 0; i < length; ++i) {
            cursor = cursor.relative(direction);
            if (random.nextInt(4) == 0) {
                cursor = cursor.offset(0, random.nextBoolean() ? 1 : -1, 0);
            }
            placed |= placeRing(world, random, cursor, radius, config);
            if (random.nextInt(3) == 0) {
                direction = direction.getClockWise();
            } else if (random.nextInt(5) == 0) {
                direction = direction.getCounterClockWise();
            }
        }

        return placed;
    }

    private BlockPos findAnchor(WorldGenLevel world, BlockPos origin, int verticalRange) {
        for (int y = 0; y <= verticalRange; ++y) {
            BlockPos candidate = origin.above(y);
            if (BlackHoneyFeatureUtil.canReplace(world, candidate)) {
                for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
                    if (BlackHoneyFeatureUtil.isSolid(world, candidate.relative(direction))) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private boolean placeRing(WorldGenLevel world, RandomSource random, BlockPos center, int radius, LightVeinConfiguration config) {
        boolean placed = false;
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            for (int step = 0; step <= radius; ++step) {
                BlockPos pos = center.relative(direction, step);
                if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                    continue;
                }
                BlockState state = random.nextFloat() < config.accentChance
                        ? config.accentProvider.getState(random, pos)
                        : config.veinProvider.getState(random, pos);
                world.setBlock(pos, state, 2);
                placed = true;
            }
        }
        if (BlackHoneyFeatureUtil.canReplace(world, center)) {
            world.setBlock(center, config.veinProvider.getState(random, center), 2);
            placed = true;
        }
        return placed;
    }
}
