package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlackHoneyRootArchFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyRootArchFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos start = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 5, 14);
        if (start == null) {
            return false;
        }

        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        Direction side = direction.rotateYClockwise();
        int span = 6 + random.nextInt(7);
        int height = 4 + random.nextInt(6);
        boolean placed = false;
        BlockPos previousSpine = null;

        for (int step = 0; step <= span; ++step) {
            double curve = Math.sin((Math.PI * step) / (double) span);
            int y = (int) Math.round(curve * height);
            BlockPos spine = start.offset(direction, step).up(y);
            if (previousSpine != null) {
                placed |= placeRootSegment(world, random, previousSpine, spine, side, y > 2);
            }
            placed |= placeRootKnot(world, random, spine, side, y > 2);
            previousSpine = spine;

            if (y > 2 && random.nextInt(3) == 0) {
                BlockPos hanging = spine.down();
                BlackHoneyFeatureUtil.placeIfReplaceable(world, hanging, random.nextBoolean()
                        ? Blocks.WEEPING_VINES_PLANT.getDefaultState()
                        : Blocks.HONEY_BLOCK.getDefaultState());
            }
        }

        return placed;
    }

    private boolean placeRootSegment(StructureWorldAccess world, Random random, BlockPos from, BlockPos to, Direction side, boolean thick) {
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

    private boolean placeRootBand(StructureWorldAccess world, Random random, BlockPos pos, Direction side, boolean thick) {
        boolean placed = placeRoot(world, random, pos);
        if (thick || random.nextInt(3) == 0) {
            placed |= placeRoot(world, random, pos.offset(side));
        }
        if (thick && random.nextInt(3) != 0) {
            placed |= placeRoot(world, random, pos.down());
        }
        return placed;
    }

    private boolean placeRootKnot(StructureWorldAccess world, Random random, BlockPos pos, Direction side, boolean thick) {
        boolean placed = placeRoot(world, random, pos);
        if (thick || random.nextBoolean()) {
            placed |= placeRoot(world, random, pos.offset(side));
        }
        if (thick && random.nextInt(3) != 0) {
            placed |= placeRoot(world, random, pos.offset(side.getOpposite()));
        }
        if (thick && random.nextBoolean()) {
            placed |= placeRoot(world, random, pos.down());
        }
        if (random.nextInt(5) == 0) {
            BlackHoneyFeatureUtil.placeIfReplaceable(world, pos.down(), Blocks.HONEYCOMB_BLOCK.getDefaultState());
        }
        return placed;
    }

    private boolean placeRoot(StructureWorldAccess world, Random random, BlockPos pos) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
            return false;
        }
        BlockState state = BlackHoneyFeatureUtil.darkRoot(random);
        world.setBlockState(pos, state, 2);
        return true;
    }
}
