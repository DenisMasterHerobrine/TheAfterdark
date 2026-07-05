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

public class AfterdarkMicrodecorFeature extends Feature<NoneFeatureConfiguration> {
    public AfterdarkMicrodecorFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        if (random.nextInt(5) == 0 && placeHangingChains(world, random, context.origin())) {
            return true;
        }

        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, context.origin(), 8, 24);
        if (floor == null) {
            return false;
        }

        return switch (random.nextInt(5)) {
            case 0 -> placeAshPatch(world, random, floor);
            case 1 -> placeBonePile(world, random, floor);
            case 2 -> placeSculkTrap(world, random, floor);
            case 3 -> placeShardCluster(world, random, floor);
            default -> placeCandleCircle(world, random, floor);
        };
    }

    private boolean placeAshPatch(WorldGenLevel world, RandomSource random, BlockPos base) {
        boolean placed = false;
        int radius = 2 + random.nextInt(3);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(3)) {
                    continue;
                }
                BlockPos floor = base.offset(x, -1, z);
                if (BlackHoneyFeatureUtil.inWorld(world, floor) && world.getBlockState(floor).isSolid()) {
                    world.setBlock(floor, random.nextInt(4) == 0 ? Blocks.SCULK.defaultBlockState() : darkSurface(random), 2);
                    placed = true;
                }
            }
        }
        return placed;
    }

    private boolean placeBonePile(WorldGenLevel world, RandomSource random, BlockPos base) {
        boolean placed = false;
        for (int i = 0; i < 8 + random.nextInt(8); ++i) {
            BlockPos pos = base.offset(BlackHoneyFeatureUtil.signed(random, 3), random.nextInt(2), BlackHoneyFeatureUtil.signed(random, 3));
            if (canPlaceOnFloor(world, pos)) {
                world.setBlock(pos, random.nextInt(5) == 0 ? Blocks.COBWEB.defaultBlockState() : Blocks.BONE_BLOCK.defaultBlockState(), 2);
                placed = true;
            }
        }
        return placed;
    }

    private boolean placeSculkTrap(WorldGenLevel world, RandomSource random, BlockPos base) {
        boolean placed = placeAshPatch(world, random, base);
        placed |= placeOnFloor(world, base, Blocks.SCULK_SENSOR.defaultBlockState());
        if (random.nextFloat() < 0.35F) {
            placed |= placeOnFloor(world, base.offset(BlackHoneyFeatureUtil.signed(random, 2), 0, BlackHoneyFeatureUtil.signed(random, 2)), Blocks.SCULK_SHRIEKER.defaultBlockState());
        }
        return placed;
    }

    private boolean placeShardCluster(WorldGenLevel world, RandomSource random, BlockPos base) {
        boolean placed = false;
        for (int i = 0; i < 4 + random.nextInt(5); ++i) {
            BlockPos start = base.offset(BlackHoneyFeatureUtil.signed(random, 3), 0, BlackHoneyFeatureUtil.signed(random, 3));
            int height = 1 + random.nextInt(4);
            for (int y = 0; y < height; ++y) {
                BlockPos pos = start.above(y);
                if (!BlackHoneyFeatureUtil.canReplace(world, pos) || (y == 0 && !BlackHoneyFeatureUtil.isSolid(world, pos.below()))) {
                    break;
                }
                world.setBlock(pos, random.nextInt(4) == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.COBBLED_DEEPSLATE.defaultBlockState(), 2);
                placed = true;
            }
        }
        return placed;
    }

    private boolean placeCandleCircle(WorldGenLevel world, RandomSource random, BlockPos base) {
        boolean placed = false;
        int radius = 2 + random.nextInt(2);
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            BlockPos pos = base.relative(direction, radius);
            placed |= placeOnFloor(world, pos, random.nextBoolean() ? Blocks.CYAN_CANDLE.defaultBlockState() : Blocks.BLACK_CANDLE.defaultBlockState());
        }
        if (random.nextBoolean()) {
            placed |= placeOnFloor(world, base, Blocks.SCULK.defaultBlockState());
        }
        return placed;
    }

    private boolean placeHangingChains(WorldGenLevel world, RandomSource random, BlockPos origin) {
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, origin, 28);
        if (ceiling == null) {
            return false;
        }

        int length = 2 + random.nextInt(6);
        boolean placed = false;
        for (int i = 1; i <= length; ++i) {
            BlockPos pos = ceiling.below(i);
            if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                break;
            }
            world.setBlock(pos, Blocks.CHAIN.defaultBlockState(), 2);
            placed = true;
        }
        BlockPos end = ceiling.below(length + 1);
        if (placed && BlackHoneyFeatureUtil.canReplace(world, end)) {
            world.setBlock(end, random.nextBoolean() ? Blocks.SCULK.defaultBlockState() : Blocks.CRYING_OBSIDIAN.defaultBlockState(), 2);
        }
        return placed;
    }

    private boolean placeOnFloor(WorldGenLevel world, BlockPos pos, BlockState state) {
        if (!canPlaceOnFloor(world, pos)) {
            return false;
        }
        world.setBlock(pos, state, 2);
        return true;
    }

    private boolean canPlaceOnFloor(WorldGenLevel world, BlockPos pos) {
        return BlackHoneyFeatureUtil.canReplace(world, pos) && BlackHoneyFeatureUtil.isSolid(world, pos.below());
    }

    private BlockState darkSurface(RandomSource random) {
        int pick = random.nextInt(8);
        if (pick < 3) {
            return Blocks.DEEPSLATE.defaultBlockState();
        }
        if (pick < 5) {
            return Blocks.BLACKSTONE.defaultBlockState();
        }
        if (pick < 7) {
            return Blocks.TUFF.defaultBlockState();
        }
        return Blocks.CRYING_OBSIDIAN.defaultBlockState();
    }
}
