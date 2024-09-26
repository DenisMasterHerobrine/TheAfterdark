package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CaveSeagrassFeature extends Feature<ProbabilityConfig> {
    public CaveSeagrassFeature(Codec<ProbabilityConfig> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<ProbabilityConfig> pContext) {
        boolean flag = false;

        net.minecraft.util.math.random.Random random = pContext.getRandom();
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        ProbabilityConfig probabilityfeatureconfiguration = pContext.getConfig();
        BlockPos blockpos1 = new BlockPos(blockpos.getX(), blockpos.up().getY(), blockpos.getZ());

        if (worldgenlevel.getBlockState(blockpos1).isOf(Blocks.WATER)) {
            boolean flag1 = random.nextDouble() < (double)probabilityfeatureconfiguration.probability;
            BlockState blockstate = flag1 ? Blocks.TALL_SEAGRASS.getDefaultState() : Blocks.SEAGRASS.getDefaultState();

            if (blockstate.canPlaceAt(worldgenlevel, blockpos1)) {
                if (flag1) {
                    BlockState blockstate1 = blockstate.with(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos blockpos2 = blockpos1.up();

                    if (worldgenlevel.getBlockState(blockpos2).isOf(Blocks.WATER)) {
                        worldgenlevel.setBlockState(blockpos1, blockstate, 2);
                        worldgenlevel.setBlockState(blockpos2, blockstate1, 2);
                    }
                } else {
                    worldgenlevel.setBlockState(blockpos1, blockstate, 2);
                }

                flag = true;
            }
        }

        return flag;
    }
}
