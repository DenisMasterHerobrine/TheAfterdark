package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.WaterloggableRandomPatchConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class WaterloggableRandomPatchFeature extends Feature<WaterloggableRandomPatchConfiguration> {
    public WaterloggableRandomPatchFeature(Codec<WaterloggableRandomPatchConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<WaterloggableRandomPatchConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        RandomSource random = pContext.random();
        WaterloggableRandomPatchConfiguration randomPatchConfiguration = pContext.config();
        int placed = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int j = randomPatchConfiguration.xz_spread + 1;
        int k = randomPatchConfiguration.y_spread + 1;
        for (int l = 0; l < randomPatchConfiguration.tries; ++l) {
            mutableBlockPos.setWithOffset(blockpos, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            BlockState currentState = worldgenlevel.getBlockState(mutableBlockPos);
            if ((currentState.isAir() || currentState.is(Blocks.WATER))
                    && placeBlock(worldgenlevel, mutableBlockPos, randomPatchConfiguration.to_place.getState(random, mutableBlockPos), currentState)) {
                ++placed;
            }
        }
        return placed > 0;
    }

    private boolean placeBlock(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState blockState, BlockState currentState) {
        if (!blockState.canSurvive(worldGenLevel, blockPos)) return false;
        if (blockState.getBlock() instanceof DoublePlantBlock) {
            if (!worldGenLevel.isEmptyBlock(blockPos.above())) return false;
            DoublePlantBlock.placeAt(worldGenLevel, blockState, blockPos, 2);
            return true;
        } else {
            if (blockState.getProperties().contains(BlockStateProperties.WATERLOGGED) && currentState.is(Blocks.WATER)) {
                blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            worldGenLevel.setBlock(blockPos, blockState, 2);
        }
        return true;
    }
}
