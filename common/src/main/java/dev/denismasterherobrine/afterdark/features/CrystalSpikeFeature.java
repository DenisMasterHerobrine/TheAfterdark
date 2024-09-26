package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.VerticalBlobConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CrystalSpikeFeature extends Feature<VerticalBlobConfiguration> {
    public CrystalSpikeFeature(Codec<VerticalBlobConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<VerticalBlobConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        Random random = pContext.getRandom();
        VerticalBlobConfiguration config = pContext.getConfig();
        Block hangFrom = config.blockOn.getBlock();
        Block hangFrom2 = config.blockOn2.getBlock();
        Block blobMaterial = config.blobMaterial.getBlock();
        Integer blobMass = config.getBlobMass().get(random);
        Integer blobWidth = config.getBlobWidth().get(random);
        Integer blobHeight = config.getBlobHeight().get(random);

        if (worldgenlevel.isAir(blockpos)) {
            return false;
        } else {
            BlockState blockstate = worldgenlevel.getBlockState(blockpos.up());

            if (!blockstate.isOf(hangFrom) && !blockstate.isOf(hangFrom2) && !blockstate.isOf(blobMaterial)) {
                return false;
            } else {
                worldgenlevel.setBlockState(blockpos, blobMaterial.getDefaultState(), 2);

                BlockPos blockpos1 = blockpos;
                boolean northNegative = false;//x
                boolean eastNegative = false;//z
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

                if (northNegative) {xFactor = -1;}
                if (eastNegative) {zFactor = -1;}

                for (int i = 0; i < blobMass*4; ++i) {
                    int randomNumber2 = (int)(Math.random()*(4)+1);

                    if (randomNumber2 >= 4/blobHeight) { //25% chance per number up to 4.
                        blockpos1 = new BlockPos(blockpos1.getX() + xFactor, blockpos1.getY() - 1, blockpos1.getZ() + zFactor);
                    } else {
                        blockpos1 = new BlockPos(blockpos1.getX(), blockpos1.getY() - 1, blockpos1.getZ());
                    }

                    int xDistance = blockpos1.getX() - blockpos.getX();
                    int zDistance = blockpos1.getZ() - blockpos.getZ();

                    if (xDistance >= blobWidth || zDistance >= blobWidth) {
                        i = blobMass*5;
                    }

                    worldgenlevel.setBlockState(blockpos1, blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.down(), blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.up(), blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.east(), blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.south(), blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.west(), blobMaterial.getDefaultState(), 2);
                    worldgenlevel.setBlockState(blockpos1.north(), blobMaterial.getDefaultState(), 2);

                    i += 3;
                }

                return true;
            }
        }
    }
}
