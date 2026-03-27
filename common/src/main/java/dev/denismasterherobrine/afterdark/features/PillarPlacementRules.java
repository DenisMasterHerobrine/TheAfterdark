package dev.denismasterherobrine.afterdark.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;

public interface PillarPlacementRules {
    BlockState pick(StructureWorldAccess world, Random random, BlockPos pos, int columnRadius);

    boolean shouldStop(BlockState state, boolean flag);

    static PillarPlacementRules simpleStoneOnlyStop(Block block) {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(StructureWorldAccess world, Random random, BlockPos pos, int columnRadius) {
                return block.getDefaultState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && state.isIn(BlockTags.BASE_STONE_OVERWORLD);
            }
        };
    }

    static PillarPlacementRules simpleIceFamilyStop(Block block) {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(StructureWorldAccess world, Random random, BlockPos pos, int columnRadius) {
                return block.getDefaultState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.isIn(BlockTags.BASE_STONE_OVERWORLD)
                        || state.isOf(Blocks.ICE)
                        || state.isOf(Blocks.PACKED_ICE));
            }
        };
    }

    static PillarPlacementRules basaltMix() {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(StructureWorldAccess world, Random random, BlockPos pos, int columnRadius) {
                return random.nextInt(6) == 5 ? Blocks.BLACKSTONE.getDefaultState() : Blocks.SMOOTH_BASALT.getDefaultState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.isIn(BlockTags.BASE_STONE_OVERWORLD)
                        || state.isOf(Blocks.ICE)
                        || state.isOf(Blocks.PACKED_ICE));
            }
        };
    }

    static PillarPlacementRules packedIceMix() {
        return new PillarPlacementRules() {
            @Override
            public BlockState pick(StructureWorldAccess world, Random random, BlockPos pos, int columnRadius) {
                Block block = Blocks.PACKED_ICE;
                if (columnRadius <= 3) {
                    block = Blocks.ICE;
                } else {
                    int chance = random.nextInt(20) + 1;
                    if (chance >= 17) {
                        block = Blocks.BLUE_ICE;
                    }
                }
                return block.getDefaultState();
            }

            @Override
            public boolean shouldStop(BlockState state, boolean flag) {
                return flag && (state.isIn(BlockTags.BASE_STONE_OVERWORLD)
                        || state.isOf(Blocks.ICE)
                        || state.isOf(Blocks.PACKED_ICE));
            }
        };
    }
}
