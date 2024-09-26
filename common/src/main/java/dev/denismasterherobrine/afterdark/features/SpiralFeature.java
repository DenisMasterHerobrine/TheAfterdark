package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import java.util.Set;

import dev.denismasterherobrine.afterdark.features.configuration.SpiralConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpiralFeature extends Feature<SpiralConfiguration> {
    public SpiralFeature(Codec<SpiralConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<SpiralConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        Random random = pContext.getRandom();
        SpiralConfiguration config = pContext.getConfig();
        Set<Block> validBlocks = config.validBlocks;
        Block stemMaterial = config.stemMaterial.getBlock();
        Block leafMaterial = config.leafMaterial.getBlock();
        Integer blobMass = config.getBlobMass().get(random);
        Integer blobWidth = config.getBlobWidth().get(random);
        Integer blobHeight = config.getBlobHeight().get(random);

        if (!worldgenlevel.isAir(blockpos) && validBlocks.contains(worldgenlevel.getBlockState(blockpos.up()).getBlock().getDefaultState())) {
            worldgenlevel.setBlockState(blockpos, stemMaterial.getDefaultState(), 2);
            BlockPos blockpos1 = blockpos;
            boolean northNegative = false; //x
            boolean eastNegative = false; //z
            int randomNumber = (int)(Math.random()*(4-1+1)+1);

            if (randomNumber >= 4) {
                northNegative = true;
                eastNegative = true;
            } else if (randomNumber >= 3) {
                northNegative = true;
            } else if (randomNumber >= 2) {
                eastNegative = true;
            }

            int xFactor = 1;
            int zFactor = 1;
            int spiralStep = 1;

            if (northNegative) {xFactor = -1;}
            if (eastNegative) {zFactor = -1;}

            for (int i = 0; i < blobMass*4;) {
                int randomNumber2 = (int)(Math.random()*(4)+1);

                if (randomNumber2 >= 4/blobHeight) { //25% chance per number up to 4.
                    blockpos1 = new BlockPos(blockpos1.getX() + xFactor, blockpos1.getY() - 1, blockpos1.getZ() + zFactor);
                } else {
                    blockpos1 = new BlockPos(blockpos1.getX(), blockpos1.getY() - 1, blockpos1.getZ());
                }
                worldgenlevel.setBlockState(blockpos1, stemMaterial.getDefaultState(), 2);
                worldgenlevel.setBlockState(blockpos1.down(), stemMaterial.getDefaultState(), 2);
                worldgenlevel.setBlockState(blockpos1.up(), stemMaterial.getDefaultState(), 2);

                if (spiralStep == 1) {
                    worldgenlevel.setBlockState(blockpos1.north(), stemMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.east(), stemMaterial.getDefaultState(), 2);
                } else if (spiralStep == 2) {
                    worldgenlevel.setBlockState(blockpos1.east(), stemMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.south(), stemMaterial.getDefaultState(), 2);
                } else if (spiralStep == 3) {
                    worldgenlevel.setBlockState(blockpos1.south(), stemMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.west(), stemMaterial.getDefaultState(), 2);
                } else if (spiralStep == 4) {
                    worldgenlevel.setBlockState(blockpos1.west(), stemMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.north(), stemMaterial.getDefaultState(), 2);
                    spiralStep = 0;
                }

                int xDistance = blockpos1.getX() - blockpos.getX();
                int zDistance = blockpos1.getZ() - blockpos.getZ();

                if (xDistance >= blobWidth || zDistance >= blobWidth) {
                    break;
                } else if (randomNumber2 >= 4/blobHeight && !(xDistance >= blobWidth - 4) && !(zDistance >= blobWidth - 4) && !leafMaterial.getDefaultState().isAir()) {
                    for (int b = 1; b <= 4;) {
                        int randomNumber3 = (int)(Math.random()*(2));
                        if (randomNumber3 >= 1) {
                            placeBranch(worldgenlevel, blockpos1.down(b).north(randomNumber3).east(randomNumber3 - 1), leafMaterial.getDefaultState());
                            b++;
                        } else {
                            b = 5;
                            int randomNumber4 = (int)(Math.random()*(8));
                            if (randomNumber4 >= 7 && Blocks.WARPED_WART_BLOCK.equals(leafMaterial)) {
                                worldgenlevel.setBlockState(blockpos1.down(b), Blocks.SHROOMLIGHT.getDefaultState(), 2);
                            }
                        }
                    }
                }

                spiralStep++;
                i += 3;
            }

            return true;
        }

        return false;
    }

    private void placeBranch(StructureWorldAccess level, BlockPos blockPos, BlockState material) {
        placeLeafBlock(level, blockPos.north(), material);
        placeLeafBlock(level, blockPos.east(), material);
        placeLeafBlock(level, blockPos.south(), material);
        placeLeafBlock(level, blockPos.west(), material);
        placeLeafBlock(level, blockPos.north().east(), material);
        placeLeafBlock(level, blockPos.south().east(), material);
        placeLeafBlock(level, blockPos.north().west(), material);
        placeLeafBlock(level, blockPos.south().west(), material);
        blockPos = blockPos.down();
        placeLeafBlock(level, blockPos.north().east(), material);
        placeLeafBlock(level, blockPos.south().east(), material);
        placeLeafBlock(level, blockPos.north().west(), material);
        placeLeafBlock(level, blockPos.south().west(), material);
    }

    private void placeLeafBlock(StructureWorldAccess level, BlockPos blockPos, BlockState material) {
        if (level.getBlockState(blockPos).isAir()) {
            level.setBlockState(blockPos, material, 2);
        }
    }
}
