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

public class BlackHoneyMourningFlowerFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyMourningFlowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos base = BlackHoneyFeatureUtil.findFloor(world, context.origin(), 5, 12);
        if (base == null) {
            return false;
        }

        int height = 3 + random.nextInt(4);
        for (int y = 0; y < height; ++y) {
            BlockPos stem = base.above(y);
            BlackHoneyFeatureUtil.placeIfReplaceable(world, stem, stemState(random));
            if (y > 0 && random.nextInt(4) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, stem.relative(BlackHoneyFeatureUtil.randomHorizontal(random)), Blocks.MANGROVE_ROOTS.defaultBlockState());
            }
        }

        BlockPos head = base.above(height);
        BlackHoneyFeatureUtil.placeIfReplaceable(world, head, random.nextBoolean()
                ? Blocks.OCHRE_FROGLIGHT.defaultBlockState()
                : Blocks.HONEY_BLOCK.defaultBlockState());
        placePetals(world, random, head);
        decorateFloor(world, random, base);
        return true;
    }

    private BlockState stemState(RandomSource random) {
        int pick = random.nextInt(5);
        if (pick <= 1) {
            return Blocks.DARK_OAK_FENCE.defaultBlockState();
        }
        if (pick == 2) {
            return Blocks.MANGROVE_ROOTS.defaultBlockState();
        }
        return Blocks.DARK_OAK_WOOD.defaultBlockState();
    }

    private void placePetals(WorldGenLevel world, RandomSource random, BlockPos head) {
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            BlockPos petal = head.relative(direction);
            BlackHoneyFeatureUtil.placeIfReplaceable(world, petal, petalState(random));
            if (random.nextBoolean()) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, petal.below(), petalState(random));
            }
            if (random.nextInt(3) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, petal.relative(direction), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
            }
        }
        BlackHoneyFeatureUtil.placeIfReplaceable(world, head.above(), Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState());
    }

    private BlockState petalState(RandomSource random) {
        int pick = random.nextInt(7);
        if (pick <= 2) {
            return Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        }
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (pick == 5) {
            return Blocks.BROWN_TERRACOTTA.defaultBlockState();
        }
        return Blocks.HONEY_BLOCK.defaultBlockState();
    }

    private void decorateFloor(WorldGenLevel world, RandomSource random, BlockPos base) {
        int radius = 2;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius || random.nextInt(3) == 0) {
                    continue;
                }
                BlockPos air = base.offset(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.below())) {
                    continue;
                }
                if (random.nextInt(5) == 0) {
                    BlackHoneyFeatureUtil.placeIfReplaceable(world, air, random.nextBoolean()
                            ? Blocks.DANDELION.defaultBlockState()
                            : Blocks.ORANGE_TULIP.defaultBlockState());
                } else {
                    world.setBlock(air.below(), random.nextBoolean() ? Blocks.BROWN_TERRACOTTA.defaultBlockState() : Blocks.ROOTED_DIRT.defaultBlockState(), 2);
                }
            }
        }
    }
}
