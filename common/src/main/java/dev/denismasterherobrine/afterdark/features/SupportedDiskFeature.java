package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SupportedDiskFeature extends Feature<DiskFeatureConfig> {
    public SupportedDiskFeature(Codec<DiskFeatureConfig> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<DiskFeatureConfig> pContext) {
        DiskFeatureConfig diskconfiguration = pContext.getConfig();
        BlockPos blockpos = pContext.getOrigin();
        StructureWorldAccess worldGenLevel = pContext.getWorld();
        Random randomsource = pContext.getRandom();
        boolean flag = false;
        int i = blockpos.getY();
        int j = i + diskconfiguration.halfHeight();
        int k = i - diskconfiguration.halfHeight() - 1;
        int l = diskconfiguration.radius().get(randomsource);
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

        for(BlockPos blockpos1 : BlockPos.iterate(blockpos.add(-l, 0, -l), blockpos.add(l, 0, l))) {
            int i1 = blockpos1.getX() - blockpos.getX();
            int j1 = blockpos1.getZ() - blockpos.getZ();
            if (i1 * i1 + j1 * j1 <= l * l) {
                flag |= this.placeColumn(diskconfiguration, worldGenLevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1));
            }
        }

        return flag;
    }

    protected boolean placeColumn(DiskFeatureConfig pContext, StructureWorldAccess worldGenLevel, Random random, int j, int k, BlockPos.Mutable blockPos) {
        boolean flag = false;

        for(int i = j; i > k; --i) {
            blockPos.setY(i);
            if (pContext.target().test(worldGenLevel, blockPos) && (worldGenLevel.getBlockState(blockPos.down()).getMaterial().isSolid() || worldGenLevel.getBlockState(blockPos.down()).isOf(Blocks.WATER) || worldGenLevel.getBlockState(blockPos.down()).isOf(Blocks.LAVA) || worldGenLevel.getBlockState(blockPos.down()).isOf(Blocks.POWDER_SNOW))) {
                BlockState blockstate = pContext.stateProvider().getBlockState(worldGenLevel, random, blockPos);
                worldGenLevel.setBlockState(blockPos, blockstate, 2);
                this.markBlocksAboveForPostProcessing(worldGenLevel, blockPos);
                flag = true;
            }
        }

        return flag;
    }
}