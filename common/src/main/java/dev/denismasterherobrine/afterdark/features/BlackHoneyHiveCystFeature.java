package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlackHoneyHiveCystFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyHiveCystFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos center = context.origin();
        if (!BlackHoneyFeatureUtil.canReplace(world, center) || !hasAnchor(world, center)) {
            return false;
        }

        int radius = 2 + random.nextInt(2);
        boolean placed = false;
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    double dist = (x * x + y * y * 1.2D + z * z) / (double) (radius * radius);
                    if (dist > 1.0D + random.nextFloat() * 0.22D) {
                        continue;
                    }
                    BlockPos pos = center.offset(x, y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    world.setBlock(pos, cystState(random, dist), 2);
                    placed = true;
                }
            }
        }

        for (int i = 0; i < 7; ++i) {
            BlockPos decor = center.offset(BlackHoneyFeatureUtil.signed(random, radius + 1), BlackHoneyFeatureUtil.signed(random, radius), BlackHoneyFeatureUtil.signed(random, radius + 1));
            BlockState state = random.nextBoolean() ? Blocks.ORANGE_TERRACOTTA.defaultBlockState() : Blocks.MANGROVE_ROOTS.defaultBlockState();
            BlackHoneyFeatureUtil.placeIfReplaceable(world, decor, state);
        }
        return placed;
    }

    private boolean hasAnchor(WorldGenLevel world, BlockPos center) {
        if (BlackHoneyFeatureUtil.isSolid(world, center.above())) {
            return true;
        }
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            if (BlackHoneyFeatureUtil.isSolid(world, center.relative(direction))) {
                return true;
            }
        }
        return false;
    }

    private BlockState cystState(RandomSource random, double dist) {
        if (dist < 0.35D && random.nextInt(6) == 0) {
            return Blocks.OCHRE_FROGLIGHT.defaultBlockState();
        }
        if (dist < 0.55D) {
            return random.nextBoolean() ? Blocks.HONEY_BLOCK.defaultBlockState() : Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        }
        int pick = random.nextInt(10);
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (pick <= 6) {
            return Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        }
        if (pick <= 8) {
            return Blocks.BROWN_TERRACOTTA.defaultBlockState();
        }
        return Blocks.HONEY_BLOCK.defaultBlockState();
    }
}
