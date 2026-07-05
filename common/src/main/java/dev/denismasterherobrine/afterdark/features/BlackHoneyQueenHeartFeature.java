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

public class BlackHoneyQueenHeartFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyQueenHeartFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.origin(), 28);
        if (ceiling == null) {
            return false;
        }

        int height = 7 + random.nextInt(4);
        int radius = 3 + random.nextInt(2);
        BlockPos anchor = ceiling.below();
        boolean placed = false;

        for (int y = 0; y < height; ++y) {
            double sliceRadius = sliceRadius(y, height, radius);
            int blockRadius = Math.max(1, (int) Math.ceil(sliceRadius));
            for (int x = -blockRadius; x <= blockRadius; ++x) {
                for (int z = -blockRadius; z <= blockRadius; ++z) {
                    if (!insideHeartSlice(x, z, y, height, sliceRadius, random)) {
                        continue;
                    }
                    BlockPos pos = anchor.offset(x, -y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    double dist = Math.sqrt(x * x + z * z);
                    world.setBlock(pos, heartState(random, dist, sliceRadius), 2);
                    placed = true;
                }
            }
        }

        if (placed) {
            placeVeins(world, random, anchor, radius, height);
            placeDrip(world, random, anchor.below(height));
        }
        return placed;
    }

    private double sliceRadius(int y, int height, int radius) {
        if (y == 0) {
            return 1.5D;
        }
        if (y <= 2) {
            return radius;
        }
        double t = (double) (y - 2) / (double) Math.max(1, height - 3);
        return Math.max(0.8D, radius * (1.0D - t * t));
    }

    private boolean insideHeartSlice(int x, int z, int y, int height, double radius, RandomSource random) {
        double dist = Math.sqrt(x * x + z * z);
        if (y <= 2) {
            double lobeA = Math.sqrt((x - 1.25D) * (x - 1.25D) + z * z);
            double lobeB = Math.sqrt((x + 1.25D) * (x + 1.25D) + z * z);
            return lobeA <= radius * 0.82D || lobeB <= radius * 0.82D || dist <= radius * 0.62D;
        }
        if (y >= height - 2) {
            return dist <= radius + random.nextFloat() * 0.15D;
        }
        return dist <= radius + random.nextFloat() * 0.2D;
    }

    private BlockState heartState(RandomSource random, double dist, double radius) {
        if (dist > radius - 0.75D) {
            int pick = random.nextInt(9);
            if (pick <= 4) {
                return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
            }
            if (pick <= 6) {
                return Blocks.BROWN_TERRACOTTA.defaultBlockState();
            }
            return Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        }
        int pick = random.nextInt(14);
        if (pick == 0) {
            return Blocks.OCHRE_FROGLIGHT.defaultBlockState();
        }
        if (pick <= 6) {
            return Blocks.HONEY_BLOCK.defaultBlockState();
        }
        if (pick <= 9) {
            return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (pick == 10) {
            return Blocks.YELLOW_TERRACOTTA.defaultBlockState();
        }
        return Blocks.ORANGE_TERRACOTTA.defaultBlockState();
    }

    private void placeVeins(WorldGenLevel world, RandomSource random, BlockPos anchor, int radius, int height) {
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            int veinLength = 2 + random.nextInt(4);
            BlockPos cursor = anchor.relative(direction, radius).below(1 + random.nextInt(Math.max(1, height / 2)));
            for (int i = 0; i < veinLength; ++i) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, cursor, BlackHoneyFeatureUtil.darkRoot(random));
                cursor = cursor.relative(direction).above(random.nextInt(2));
            }
        }
        for (int i = 0; i < 12; ++i) {
            BlockPos web = anchor.offset(BlackHoneyFeatureUtil.signed(random, radius + 1), -random.nextInt(height), BlackHoneyFeatureUtil.signed(random, radius + 1));
            if (random.nextInt(3) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, web, Blocks.WEEPING_VINES_PLANT.defaultBlockState());
            }
        }
    }

    private void placeDrip(WorldGenLevel world, RandomSource random, BlockPos start) {
        int length = 2 + random.nextInt(5);
        BlockPos cursor = start;
        for (int i = 0; i < length; ++i) {
            if (!BlackHoneyFeatureUtil.canReplace(world, cursor)) {
                return;
            }
            world.setBlock(cursor, i == length - 1 ? Blocks.HONEYCOMB_BLOCK.defaultBlockState() : Blocks.HONEY_BLOCK.defaultBlockState(), 2);
            cursor = cursor.below();
        }
    }
}
