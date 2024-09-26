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

public class AdditiveBlobFeature extends Feature<VerticalBlobConfiguration> {
    public AdditiveBlobFeature(Codec<VerticalBlobConfiguration> pContext) {
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

                for(int i = 0; i < blobMass; ++i) {
                    BlockPos blockpos1 = blockpos.add(random.nextInt(blobWidth) - random.nextInt(blobWidth), -random.nextInt(blobHeight), random.nextInt(blobWidth) - random.nextInt(blobWidth));
                    BlockState blockAbove = worldgenlevel.getBlockState(blockpos1.up());
                    if (blockAbove.isOf(hangFrom) || blockAbove.isOf(hangFrom2) || blockAbove.isOf(blobMaterial)) {
                        worldgenlevel.setBlockState(blockpos1, blobMaterial.getDefaultState(), 2);
                    }
                }

                return true;
            }
        }
    }
}
