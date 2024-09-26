package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CaveKelpFeature extends Feature<DefaultFeatureConfig> {
    public CaveKelpFeature(Codec<DefaultFeatureConfig> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> pContext) {
        int i = 0;
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        net.minecraft.util.math.random.Random random = pContext.getRandom();
        BlockPos blockpos1 = new BlockPos(blockpos.getX(), blockpos.up().getY(), blockpos.getZ());

        if (worldgenlevel.getBlockState(blockpos1).isOf(Blocks.WATER)) {
            BlockState blockstate = Blocks.KELP.getDefaultState();
            BlockState blockstate1 = Blocks.KELP_PLANT.getDefaultState();
            int k = 1 + random.nextInt(10);

            for(int l = 0; l <= k; ++l) {
                if (worldgenlevel.getBlockState(blockpos1).isOf(Blocks.WATER) && worldgenlevel.getBlockState(blockpos1.up()).isOf(Blocks.WATER) && blockstate1.canPlaceAt(worldgenlevel, blockpos1)) {
                    if (l == k) {
                        worldgenlevel.setBlockState(blockpos1, blockstate.with(KelpBlock.AGE, Integer.valueOf(random.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        worldgenlevel.setBlockState(blockpos1, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos2 = blockpos1.down();
                    if (blockstate.canPlaceAt(worldgenlevel, blockpos2) && !worldgenlevel.getBlockState(blockpos2.down()).isOf(Blocks.KELP)) {
                        worldgenlevel.setBlockState(blockpos2, blockstate.with(KelpBlock.AGE, Integer.valueOf(random.nextInt(4) + 20)), 2);
                        ++i;
                    }
                    break;
                }

                blockpos1 = blockpos1.up();
            }
        }

        return i > 0;
    }
}
