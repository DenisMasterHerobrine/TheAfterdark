package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CavePickleFeature extends Feature<DefaultFeatureConfig> {
    public CavePickleFeature(Codec<DefaultFeatureConfig> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> pContext) {
        net.minecraft.util.math.random.Random random = pContext.getRandom();
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        BlockPos blockpos1 = new BlockPos(blockpos.getX(), blockpos.up().getY(), blockpos.getZ());
        BlockState blockstate = Blocks.SEA_PICKLE.getDefaultState().with(SeaPickleBlock.PICKLES, Integer.valueOf(random.nextInt(4) + 1));

        if (worldgenlevel.getBlockState(blockpos1).isOf(Blocks.WATER) && blockstate.canPlaceAt(worldgenlevel, blockpos1)) {
            worldgenlevel.setBlockState(blockpos1, blockstate, 2);
            return true;
        } else {
            return false;
        }
    }
}
