package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.WaterloggableRandomPatchConfiguration;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WaterloggableRandomPatchFeature extends Feature<WaterloggableRandomPatchConfiguration> {
    public WaterloggableRandomPatchFeature(Codec<WaterloggableRandomPatchConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<WaterloggableRandomPatchConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        Random random = pContext.getRandom();
        WaterloggableRandomPatchConfiguration randomPatchConfiguration = pContext.getConfig();
        int placed = 0;
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        int j = randomPatchConfiguration.xz_spread + 1;
        int k = randomPatchConfiguration.y_spread + 1;
        for (int l = 0; l < randomPatchConfiguration.tries; ++l) {
            mutableBlockPos.set(blockpos, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            BlockState currentState = worldgenlevel.getBlockState(mutableBlockPos);
            if ((currentState.isAir() || currentState.isOf(Blocks.WATER))
                    && placeBlock(worldgenlevel, mutableBlockPos, randomPatchConfiguration.to_place.get(random, mutableBlockPos), currentState)) {
                ++placed;
            }
        }
        return placed > 0;
    }

    private boolean placeBlock(StructureWorldAccess worldGenLevel, BlockPos blockPos, BlockState blockState, BlockState currentState) {
        if (!blockState.canPlaceAt(worldGenLevel, blockPos)) return false;
        if (blockState.getBlock() instanceof TallPlantBlock) {
            if (!worldGenLevel.isAir(blockPos.up())) return false;
            TallPlantBlock.placeAt(worldGenLevel, blockState, blockPos, 2);
            return true;
        } else {
            if (blockState.getProperties().contains(Properties.WATERLOGGED) && currentState.isOf(Blocks.WATER)) {
                blockState = blockState.with(Properties.WATERLOGGED, true);
            }
            worldGenLevel.setBlockState(blockPos, blockState, 2);
        }
        return true;
    }
}
