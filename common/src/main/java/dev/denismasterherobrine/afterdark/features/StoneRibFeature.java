package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.StoneRibConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class StoneRibFeature extends Feature<StoneRibConfiguration> {
    public StoneRibFeature(Codec<StoneRibConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<StoneRibConfiguration> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        StoneRibConfiguration config = context.getConfig();
        BlockPos start = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 8, 18);
        if (start == null) {
            return false;
        }

        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        Direction side = direction.rotateYClockwise();
        int span = config.span.get(random);
        int height = config.height.get(random);
        int thickness = config.thickness.get(random);
        boolean placed = false;
        BlockPos previous = null;

        for (int step = 0; step <= span; ++step) {
            double curve = Math.sin((Math.PI * step) / (double) span);
            int y = (int) Math.round(curve * height);
            BlockPos spine = start.offset(direction, step).up(y);
            if (previous != null) {
                placed |= placeSegment(world, random, previous, spine, side, thickness, config);
            }
            placed |= placeCluster(world, random, spine, side, thickness, config);
            previous = spine;
        }

        return placed;
    }

    private boolean placeSegment(
            StructureWorldAccess world,
            Random random,
            BlockPos from,
            BlockPos to,
            Direction side,
            int thickness,
            StoneRibConfiguration config) {
        boolean placed = false;
        BlockPos cursor = from;
        int stepX = Integer.compare(to.getX(), from.getX());
        int stepY = Integer.compare(to.getY(), from.getY());
        int stepZ = Integer.compare(to.getZ(), from.getZ());

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
            StructureWorldAccess world,
            Random random,
            BlockPos pos,
            Direction side,
            int thickness,
            StoneRibConfiguration config) {
        boolean placed = placeSpine(world, random, pos, config);
        for (int i = 1; i < thickness; ++i) {
            if (random.nextFloat() <= config.sideThicknessChance) {
                placed |= placeSpine(world, random, pos.offset(side, i), config);
            }
            if (random.nextFloat() <= config.sideThicknessChance) {
                placed |= placeSpine(world, random, pos.offset(side.getOpposite(), i), config);
            }
            if (random.nextBoolean()) {
                placed |= placeSpine(world, random, pos.down(i), config);
            }
        }
        if (random.nextFloat() < config.accentChance) {
            placeAccent(world, random, pos, side, config);
        }
        return placed;
    }

    private boolean placeSpine(StructureWorldAccess world, Random random, BlockPos pos, StoneRibConfiguration config) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
            return false;
        }
        BlockState state = config.spineProvider.get(random, pos);
        world.setBlockState(pos, state, 2);
        return true;
    }

    private void placeAccent(StructureWorldAccess world, Random random, BlockPos pos, Direction side, StoneRibConfiguration config) {
        Direction targetSide = random.nextBoolean() ? side : side.getOpposite();
        BlockPos accentPos = pos.offset(targetSide);
        if (BlackHoneyFeatureUtil.canReplace(world, accentPos)) {
            world.setBlockState(accentPos, config.accentProvider.get(random, accentPos), 2);
        }
    }
}
