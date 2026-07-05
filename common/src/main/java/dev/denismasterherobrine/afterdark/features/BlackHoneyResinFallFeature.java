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

public class BlackHoneyResinFallFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyResinFallFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.origin(), 24);
        if (ceiling == null) {
            return false;
        }

        BlockPos cursor = ceiling.below();
        int length = 5 + random.nextInt(14);
        boolean placed = false;
        BlockPos lastAir = cursor;

        for (int i = 0; i < length && BlackHoneyFeatureUtil.canReplace(world, cursor); ++i) {
            BlockState state = random.nextInt(7) == 0
                    ? Blocks.HONEYCOMB_BLOCK.defaultBlockState()
                    : Blocks.HONEY_BLOCK.defaultBlockState();
            world.setBlock(cursor, state, 2);
            placed = true;
            lastAir = cursor;

            if (i < 3 || random.nextInt(5) == 0) {
                placeCeilingNodule(world, random, cursor);
            }
            if (i > 2 && random.nextInt(9) == 0) {
                Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
                BlackHoneyFeatureUtil.placeIfReplaceable(world, cursor.relative(side), Blocks.HONEY_BLOCK.defaultBlockState());
            }

            cursor = cursor.below();
        }

        if (placed) {
            spreadLandingPool(world, random, lastAir);
        }
        return placed;
    }

    private void placeCeilingNodule(WorldGenLevel world, RandomSource random, BlockPos pos) {
        Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
        BlockState state = random.nextBoolean() ? Blocks.HONEYCOMB_BLOCK.defaultBlockState() : Blocks.MANGROVE_ROOTS.defaultBlockState();
        BlackHoneyFeatureUtil.placeIfReplaceable(world, pos.relative(side), state);
    }

    private void spreadLandingPool(WorldGenLevel world, RandomSource random, BlockPos base) {
        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, base, 1, 8);
        if (floor == null) {
            return;
        }
        int radius = 1 + random.nextInt(2);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                BlockPos air = floor.offset(x, 0, z);
                if (BlackHoneyFeatureUtil.canReplace(world, air) && BlackHoneyFeatureUtil.isSolid(world, air.below())) {
                    BlockState state = random.nextInt(4) == 0 ? Blocks.MUD.defaultBlockState() : Blocks.HONEY_BLOCK.defaultBlockState();
                    world.setBlock(air.below(), state, 2);
                }
            }
        }
    }
}
