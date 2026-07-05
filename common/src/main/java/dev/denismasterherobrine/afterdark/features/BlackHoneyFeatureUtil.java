package dev.denismasterherobrine.afterdark.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

final class BlackHoneyFeatureUtil {
    static final Direction[] HORIZONTAL = new Direction[] {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };

    private BlackHoneyFeatureUtil() {}

    static boolean inWorld(WorldGenLevel world, BlockPos pos) {
        return pos.getY() >= world.getMinBuildHeight() && pos.getY() < world.getMaxBuildHeight();
    }

    static boolean inOriginChunk(BlockPos origin, BlockPos pos) {
        return SectionPos.blockToSectionCoord(pos.getX()) == SectionPos.blockToSectionCoord(origin.getX())
                && SectionPos.blockToSectionCoord(pos.getZ()) == SectionPos.blockToSectionCoord(origin.getZ());
    }

    static boolean canReplace(WorldGenLevel world, BlockPos pos) {
        if (!inWorld(world, pos)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.canBeReplaced() || state.is(BlockTags.LEAVES);
    }

    static boolean isSolid(WorldGenLevel world, BlockPos pos) {
        if (!inWorld(world, pos)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        return state.isSolid() && state.getFluidState().isEmpty();
    }

    static BlockPos findFloor(WorldGenLevel world, BlockPos origin, int upSteps, int downSteps) {
        for (int y = upSteps; y >= -downSteps; --y) {
            BlockPos air = origin.above(y);
            if (canReplace(world, air) && isSolid(world, air.below())) {
                return air;
            }
        }
        return null;
    }

    static BlockPos findCeiling(WorldGenLevel world, BlockPos origin, int maxSteps) {
        for (int y = 0; y <= maxSteps; ++y) {
            BlockPos air = origin.above(y);
            if (canReplace(world, air) && isSolid(world, air.above())) {
                return air.above();
            }
        }
        return null;
    }

    static void placeIfReplaceable(WorldGenLevel world, BlockPos pos, BlockState state) {
        if (canReplace(world, pos)) {
            world.setBlock(pos, state, 2);
        }
    }

    static boolean placeIfReplaceableInOriginChunk(WorldGenLevel world, BlockPos origin, BlockPos pos, BlockState state) {
        if (!inOriginChunk(origin, pos) || !canReplace(world, pos)) {
            return false;
        }
        world.setBlock(pos, state, 2);
        return true;
    }

    static boolean placeSurfaceBlockInOriginChunk(WorldGenLevel world, BlockPos origin, BlockPos airPos, BlockState state) {
        BlockPos surface = airPos.below();
        if (!inOriginChunk(origin, surface) || !inWorld(world, surface) || !isSolid(world, surface)) {
            return false;
        }
        world.setBlock(surface, state, 2);
        return true;
    }

    static void placeSurfaceBlock(WorldGenLevel world, BlockPos airPos, BlockState state) {
        if (inWorld(world, airPos.below()) && isSolid(world, airPos.below())) {
            world.setBlock(airPos.below(), state, 2);
        }
    }

    static Direction randomHorizontal(RandomSource random) {
        return HORIZONTAL[random.nextInt(HORIZONTAL.length)];
    }

    static BlockState honeyGrowth(RandomSource random) {
        int pick = random.nextInt(12);
        if (pick == 0) {
            return Blocks.OCHRE_FROGLIGHT.defaultBlockState();
        }
        if (pick <= 4) {
            return Blocks.HONEY_BLOCK.defaultBlockState();
        }
        if (pick <= 8) {
            return Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (pick == 9) {
            return Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        }
        return Blocks.MANGROVE_ROOTS.defaultBlockState();
    }

    static BlockState darkRoot(RandomSource random) {
        int pick = random.nextInt(9);
        if (pick <= 2) {
            return Blocks.MANGROVE_ROOTS.defaultBlockState();
        }
        if (pick <= 4) {
            return Blocks.MUDDY_MANGROVE_ROOTS.defaultBlockState();
        }
        if (pick <= 6) {
            return Blocks.DARK_OAK_WOOD.defaultBlockState();
        }
        return Blocks.ROOTED_DIRT.defaultBlockState();
    }

    static int signed(RandomSource random, int boundInclusive) {
        return random.nextInt(boundInclusive * 2 + 1) - boundInclusive;
    }

    static int stepToward(int from, int to) {
        if (from < to) {
            return 1;
        }
        return from > to ? -1 : 0;
    }
}
