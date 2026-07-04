package dev.denismasterherobrine.afterdark.features;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;

final class BlackHoneyFeatureUtil {
    static final Direction[] HORIZONTAL = new Direction[] {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST
    };

    private BlackHoneyFeatureUtil() {}

    static boolean inWorld(StructureWorldAccess world, BlockPos pos) {
        return pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY();
    }

    static boolean inOriginChunk(BlockPos origin, BlockPos pos) {
        return ChunkSectionPos.getSectionCoord(pos.getX()) == ChunkSectionPos.getSectionCoord(origin.getX())
                && ChunkSectionPos.getSectionCoord(pos.getZ()) == ChunkSectionPos.getSectionCoord(origin.getZ());
    }

    static boolean canReplace(StructureWorldAccess world, BlockPos pos) {
        if (!inWorld(world, pos)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.isReplaceable() || state.isIn(BlockTags.LEAVES);
    }

    static boolean isSolid(StructureWorldAccess world, BlockPos pos) {
        if (!inWorld(world, pos)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        return state.isSolid() && state.getFluidState().isEmpty();
    }

    static BlockPos findFloor(StructureWorldAccess world, BlockPos origin, int upSteps, int downSteps) {
        for (int y = upSteps; y >= -downSteps; --y) {
            BlockPos air = origin.up(y);
            if (canReplace(world, air) && isSolid(world, air.down())) {
                return air;
            }
        }
        return null;
    }

    static BlockPos findCeiling(StructureWorldAccess world, BlockPos origin, int maxSteps) {
        for (int y = 0; y <= maxSteps; ++y) {
            BlockPos air = origin.up(y);
            if (canReplace(world, air) && isSolid(world, air.up())) {
                return air.up();
            }
        }
        return null;
    }

    static void placeIfReplaceable(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (canReplace(world, pos)) {
            world.setBlockState(pos, state, 2);
        }
    }

    static boolean placeIfReplaceableInOriginChunk(StructureWorldAccess world, BlockPos origin, BlockPos pos, BlockState state) {
        if (!inOriginChunk(origin, pos) || !canReplace(world, pos)) {
            return false;
        }
        world.setBlockState(pos, state, 2);
        return true;
    }

    static boolean placeSurfaceBlockInOriginChunk(StructureWorldAccess world, BlockPos origin, BlockPos airPos, BlockState state) {
        BlockPos surface = airPos.down();
        if (!inOriginChunk(origin, surface) || !inWorld(world, surface) || !isSolid(world, surface)) {
            return false;
        }
        world.setBlockState(surface, state, 2);
        return true;
    }

    static void placeSurfaceBlock(StructureWorldAccess world, BlockPos airPos, BlockState state) {
        if (inWorld(world, airPos.down()) && isSolid(world, airPos.down())) {
            world.setBlockState(airPos.down(), state, 2);
        }
    }

    static Direction randomHorizontal(Random random) {
        return HORIZONTAL[random.nextInt(HORIZONTAL.length)];
    }

    static BlockState honeyGrowth(Random random) {
        int pick = random.nextInt(12);
        if (pick == 0) {
            return Blocks.OCHRE_FROGLIGHT.getDefaultState();
        }
        if (pick <= 4) {
            return Blocks.HONEY_BLOCK.getDefaultState();
        }
        if (pick <= 8) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (pick == 9) {
            return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
        }
        return Blocks.MANGROVE_ROOTS.getDefaultState();
    }

    static BlockState darkRoot(Random random) {
        int pick = random.nextInt(9);
        if (pick <= 2) {
            return Blocks.MANGROVE_ROOTS.getDefaultState();
        }
        if (pick <= 4) {
            return Blocks.MUDDY_MANGROVE_ROOTS.getDefaultState();
        }
        if (pick <= 6) {
            return Blocks.DARK_OAK_WOOD.getDefaultState();
        }
        return Blocks.ROOTED_DIRT.getDefaultState();
    }

    static int signed(Random random, int boundInclusive) {
        return random.nextInt(boundInclusive * 2 + 1) - boundInclusive;
    }

    static int stepToward(int from, int to) {
        if (from < to) {
            return 1;
        }
        return from > to ? -1 : 0;
    }
}
