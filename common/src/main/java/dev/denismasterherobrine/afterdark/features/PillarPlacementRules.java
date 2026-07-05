package dev.denismasterherobrine.afterdark.features;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface PillarPlacementRules {
    BlockState pick(WorldGenLevel world, RandomSource random, BlockPos pos, int columnRadius);

    boolean shouldStop(BlockState state, boolean flag);

    static PillarPlacementRules simpleStoneOnlyStop(Block block) {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(WorldGenLevel world, RandomSource random, BlockPos pos, int columnRadius) {
                return block.defaultBlockState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && state.is(BlockTags.BASE_STONE_OVERWORLD);
            }
        };
    }

    static PillarPlacementRules simpleIceFamilyStop(Block block) {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(WorldGenLevel world, RandomSource random, BlockPos pos, int columnRadius) {
                return block.defaultBlockState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.is(BlockTags.BASE_STONE_OVERWORLD)
                        || state.is(Blocks.ICE)
                        || state.is(Blocks.PACKED_ICE));
            }
        };
    }

    static PillarPlacementRules basaltMix() {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(WorldGenLevel world, RandomSource random, BlockPos pos, int columnRadius) {
                return random.nextInt(6) == 5 ? Blocks.BLACKSTONE.defaultBlockState() : Blocks.SMOOTH_BASALT.defaultBlockState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.is(BlockTags.BASE_STONE_OVERWORLD)
                        || state.is(Blocks.ICE)
                        || state.is(Blocks.PACKED_ICE));
            }
        };
    }

    static PillarPlacementRules packedIceMix() {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(WorldGenLevel world, RandomSource random, BlockPos pos, int columnRadius) {
                Block block = Blocks.PACKED_ICE;
                if (columnRadius <= 3) {
                    block = Blocks.ICE;
                } else {
                    int chance = random.nextInt(20) + 1;
                    if (chance >= 17) {
                        block = Blocks.BLUE_ICE;
                    }
                }
                return block.defaultBlockState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.is(BlockTags.BASE_STONE_OVERWORLD)
                        || state.is(Blocks.ICE)
                        || state.is(Blocks.PACKED_ICE));
            }
        };
    }
}
