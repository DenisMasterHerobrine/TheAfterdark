package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class SupportedDiskFeature extends Feature<DiskConfiguration> {
    public SupportedDiskFeature(Codec<DiskConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<DiskConfiguration> pContext) {
        DiskConfiguration diskconfiguration = pContext.config();
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldGenLevel = pContext.level();
        RandomSource randomsource = pContext.random();
        boolean flag = false;
        int i = blockpos.getY();
        int j = i + diskconfiguration.halfHeight();
        int k = i - diskconfiguration.halfHeight() - 1;
        int l = diskconfiguration.radius().sample(randomsource);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-l, 0, -l), blockpos.offset(l, 0, l))) {
            int i1 = blockpos1.getX() - blockpos.getX();
            int j1 = blockpos1.getZ() - blockpos.getZ();
            if (i1 * i1 + j1 * j1 <= l * l) {
                flag |= this.placeColumn(diskconfiguration, worldGenLevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1));
            }
        }

        return flag;
    }

    protected boolean placeColumn(DiskConfiguration pContext, WorldGenLevel worldGenLevel, RandomSource random, int j, int k, BlockPos.MutableBlockPos blockPos) {
        boolean flag = false;

        for(int i = j; i > k; --i) {
            blockPos.setY(i);
            if (pContext.target().test(worldGenLevel, blockPos) && (worldGenLevel.getBlockState(blockPos.below()).isSolid() || worldGenLevel.getBlockState(blockPos.below()).is(Blocks.WATER) || worldGenLevel.getBlockState(blockPos.below()).is(Blocks.LAVA) || worldGenLevel.getBlockState(blockPos.below()).is(Blocks.POWDER_SNOW))) {
                BlockState blockstate = pContext.stateProvider().getState(worldGenLevel, random, blockPos);
                worldGenLevel.setBlock(blockPos, blockstate, 2);
                this.markAboveForPostProcessing(worldGenLevel, blockPos);
                flag = true;
            }
        }

        return flag;
    }
}