package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlackHoneyCocoonClusterFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyCocoonClusterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.origin(), 20);
        if (ceiling == null) {
            return false;
        }

        int cocoons = 2 + random.nextInt(5);
        boolean placed = false;
        for (int i = 0; i < cocoons; ++i) {
            BlockPos near = ceiling.below().offset(BlackHoneyFeatureUtil.signed(random, 3), 0, BlackHoneyFeatureUtil.signed(random, 3));
            BlockPos localCeiling = BlackHoneyFeatureUtil.findCeiling(world, near, 5);
            if (localCeiling == null) {
                continue;
            }
            placed |= placeCocoon(world, random, localCeiling.below(), 2 + random.nextInt(4));
        }
        return placed;
    }

    private boolean placeCocoon(WorldGenLevel world, RandomSource random, BlockPos anchor, int height) {
        boolean placed = false;
        for (int y = 0; y < height; ++y) {
            int radius = (y == 0 || y == height - 1) ? 0 : 1;
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    if (radius == 1 && Math.abs(x) + Math.abs(z) > 1 + random.nextInt(2)) {
                        continue;
                    }
                    BlockPos pos = anchor.offset(x, -y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    world.setBlock(pos, cocoonState(random, y, height), 2);
                    placed = true;
                }
            }
        }

        BlockPos core = anchor.below(Math.max(1, height / 2));
        if (random.nextInt(3) == 0) {
            BlackHoneyFeatureUtil.placeIfReplaceable(world, core, Blocks.YELLOW_TERRACOTTA.defaultBlockState());
        }
        for (int i = 0; i < 5; ++i) {
            BlockPos web = core.offset(BlackHoneyFeatureUtil.signed(random, 2), BlackHoneyFeatureUtil.signed(random, 1), BlackHoneyFeatureUtil.signed(random, 2));
            if (random.nextInt(3) != 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, web, Blocks.WEEPING_VINES_PLANT.defaultBlockState());
            }
        }
        return placed;
    }

    private BlockState cocoonState(RandomSource random, int y, int height) {
        if (y == height - 1 && random.nextInt(4) == 0) {
            return Blocks.BEE_NEST.defaultBlockState();
        }
        int pick = random.nextInt(10);
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (pick <= 7) {
            return Blocks.HONEY_BLOCK.defaultBlockState();
        }
        if (pick == 8) {
            return Blocks.YELLOW_TERRACOTTA.defaultBlockState();
        }
        return Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
    }
}
