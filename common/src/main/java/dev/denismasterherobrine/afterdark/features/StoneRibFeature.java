package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.StoneRibConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class StoneRibFeature extends Feature<StoneRibConfiguration> {
    public StoneRibFeature(Codec<StoneRibConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StoneRibConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        StoneRibConfiguration config = context.config();
        BlockPos start = BlackHoneyFeatureUtil.findFloor(world, context.origin(), 8, 18);
        if (start == null) {
            return false;
        }

        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        Direction side = direction.getClockWise();
        int span = config.span.sample(random);
        int height = config.height.sample(random);
        int thickness = config.thickness.sample(random);
        boolean placed = false;
        BlockPos previous = null;

        for (int step = 0; step <= span; ++step) {
            double curve = Math.sin((Math.PI * step) / (double) span);
            int y = (int) Math.round(curve * height);
            BlockPos spine = start.relative(direction, step).above(y);
            if (previous != null) {
                placed |= placeSegment(world, random, previous, spine, side, thickness, config);
            }
            placed |= placeCluster(world, random, spine, side, thickness, config);
            previous = spine;
        }

        return placed;
    }

    private boolean placeSegment(
            WorldGenLevel world,
            RandomSource random,
            BlockPos from,
            BlockPos to,
            Direction side,
            int thickness,
            StoneRibConfiguration config) {
        boolean placed = false;
        BlockPos cursor = from;
        int stepX = BlackHoneyFeatureUtil.stepToward(from.getX(), to.getX());
        int stepY = BlackHoneyFeatureUtil.stepToward(from.getY(), to.getY());
        int stepZ = BlackHoneyFeatureUtil.stepToward(from.getZ(), to.getZ());

        while (cursor.getX() != to.getX() || cursor.getZ() != to.getZ()) {
            cursor = new BlockPos(cursor.getX() + stepX, cursor.getY(), cursor.getZ() + stepZ);
            placed |= placeCluster(world, random, cursor, side, thickness, config);
        }
        while (cursor.getY() != to.getY()) {
            cursor = new BlockPos(cursor.getX(), cursor.getY() + stepY, cursor.getZ());
            placed |= placeCluster(world, random, cursor, side, thickness, config);
        }

        return placed;
    }

    private boolean placeCluster(
            WorldGenLevel world,
            RandomSource random,
            BlockPos pos,
            Direction side,
            int thickness,
            StoneRibConfiguration config) {
        boolean placed = placeSpine(world, random, pos, config);
        for (int i = 1; i < thickness; ++i) {
            if (random.nextFloat() <= config.sideThicknessChance) {
                placed |= placeSpine(world, random, pos.relative(side, i), config);
            }
            if (random.nextFloat() <= config.sideThicknessChance) {
                placed |= placeSpine(world, random, pos.relative(side.getOpposite(), i), config);
            }
            if (random.nextBoolean()) {
                placed |= placeSpine(world, random, pos.below(i), config);
            }
        }
        if (random.nextFloat() < config.accentChance) {
            placeAccent(world, random, pos, side, config);
        }
        return placed;
    }

    private boolean placeSpine(WorldGenLevel world, RandomSource random, BlockPos pos, StoneRibConfiguration config) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
            return false;
        }
        BlockState state = config.spineProvider.getState(random, pos);
        world.setBlock(pos, state, 2);
        return true;
    }

    private void placeAccent(WorldGenLevel world, RandomSource random, BlockPos pos, Direction side, StoneRibConfiguration config) {
        Direction targetSide = random.nextBoolean() ? side : side.getOpposite();
        BlockPos accentPos = pos.relative(targetSide);
        if (BlackHoneyFeatureUtil.canReplace(world, accentPos)) {
            world.setBlock(accentPos, config.accentProvider.getState(random, accentPos), 2);
        }
    }
}
