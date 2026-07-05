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

public class BlackHoneyRootArchFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyRootArchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos start = BlackHoneyFeatureUtil.findFloor(world, context.origin(), 5, 14);
        if (start == null) {
            return false;
        }

        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        Direction side = direction.getClockWise();
        int span = 6 + random.nextInt(7);
        int height = 4 + random.nextInt(6);
        boolean placed = false;
        BlockPos previousSpine = null;

        for (int step = 0; step <= span; ++step) {
            double curve = Math.sin((Math.PI * step) / (double) span);
            int y = (int) Math.round(curve * height);
            BlockPos spine = start.relative(direction, step).above(y);
            if (previousSpine != null) {
                placed |= placeRootSegment(world, random, previousSpine, spine, side, y > 2);
            }
            placed |= placeRootKnot(world, random, spine, side, y > 2);
            previousSpine = spine;

            if (y > 2 && random.nextInt(3) == 0) {
                BlockPos hanging = spine.below();
                BlackHoneyFeatureUtil.placeIfReplaceable(world, hanging, random.nextBoolean()
                        ? Blocks.WEEPING_VINES_PLANT.defaultBlockState()
                        : Blocks.HONEY_BLOCK.defaultBlockState());
            }
        }

        return placed;
    }

    private boolean placeRootSegment(WorldGenLevel world, RandomSource random, BlockPos from, BlockPos to, Direction side, boolean thick) {
        boolean placed = false;
        BlockPos cursor = from;
        int stepX = BlackHoneyFeatureUtil.stepToward(from.getX(), to.getX());
        int stepY = BlackHoneyFeatureUtil.stepToward(from.getY(), to.getY());
        int stepZ = BlackHoneyFeatureUtil.stepToward(from.getZ(), to.getZ());

        while (cursor.getX() != to.getX() || cursor.getZ() != to.getZ()) {
            cursor = new BlockPos(cursor.getX() + stepX, cursor.getY(), cursor.getZ() + stepZ);
            placed |= placeRootBand(world, random, cursor, side, thick);
        }
        while (cursor.getY() != to.getY()) {
            cursor = new BlockPos(cursor.getX(), cursor.getY() + stepY, cursor.getZ());
            placed |= placeRootBand(world, random, cursor, side, thick);
        }

        return placed;
    }

    private boolean placeRootBand(WorldGenLevel world, RandomSource random, BlockPos pos, Direction side, boolean thick) {
        boolean placed = placeRoot(world, random, pos);
        if (thick || random.nextInt(3) == 0) {
            placed |= placeRoot(world, random, pos.relative(side));
        }
        if (thick && random.nextInt(3) != 0) {
            placed |= placeRoot(world, random, pos.below());
        }
        return placed;
    }

    private boolean placeRootKnot(WorldGenLevel world, RandomSource random, BlockPos pos, Direction side, boolean thick) {
        boolean placed = placeRoot(world, random, pos);
        if (thick || random.nextBoolean()) {
            placed |= placeRoot(world, random, pos.relative(side));
        }
        if (thick && random.nextInt(3) != 0) {
            placed |= placeRoot(world, random, pos.relative(side.getOpposite()));
        }
        if (thick && random.nextBoolean()) {
            placed |= placeRoot(world, random, pos.below());
        }
        if (random.nextInt(5) == 0) {
            BlackHoneyFeatureUtil.placeIfReplaceable(world, pos.below(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
        }
        return placed;
    }

    private boolean placeRoot(WorldGenLevel world, RandomSource random, BlockPos pos) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
            return false;
        }
        BlockState state = BlackHoneyFeatureUtil.darkRoot(random);
        world.setBlock(pos, state, 2);
        return true;
    }
}
