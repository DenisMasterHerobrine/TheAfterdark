package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.DoubleBlockConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PondFeature extends Feature<DoubleBlockConfiguration> {
    public PondFeature(Codec<DoubleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<DoubleBlockConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();
        Random random = pContext.getRandom();
        BlockState material = pContext.getConfig().toPlace().getBlockState(random, blockpos);
        BlockState slab = pContext.getConfig().slabToPlace().getBlockState(random, blockpos);
        BlockState water = Blocks.WATER.getDefaultState();
        BlockState mud = Blocks.MUD.getDefaultState();

        if (!worldgenlevel.getBlockState(blockpos.add(3, 0, 0)).getMaterial().isReplaceable() && !worldgenlevel.getBlockState(blockpos.add(0, 0, 3)).getMaterial().isReplaceable() ||
                !worldgenlevel.getBlockState(blockpos.add(-3, 0, 0)).getMaterial().isReplaceable() && !worldgenlevel.getBlockState(blockpos.add(0, 0, -3)).getMaterial().isReplaceable() ||
                !worldgenlevel.getBlockState(blockpos.add(2, -1, 2)).getMaterial().isReplaceable() && !worldgenlevel.getBlockState(blockpos.add(-2, -1, 2)).getMaterial().isReplaceable() ||
                !worldgenlevel.getBlockState(blockpos.add(-2, -1, -2)).getMaterial().isReplaceable() && !worldgenlevel.getBlockState(blockpos.add(2, -1, -2)).getMaterial().isReplaceable()) {

            return false;
        }

        worldgenlevel.setBlockState(blockpos.add(0, -1, 0), water, 2);
        worldgenlevel.setBlockState(blockpos.add(0, 0, 0), Blocks.LILY_PAD.getDefaultState(), 2);
        worldgenlevel.setBlockState(blockpos.add(1, -1, 0), water, 2);
        worldgenlevel.setBlockState(blockpos.add(0, -1, 1), water, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -1, 0), water, 2);
        worldgenlevel.setBlockState(blockpos.add(0, -1, -1), water, 2);
        worldgenlevel.setBlockState(blockpos.add(1, -1, 1), water, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -1, -1), water, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -1, 1), water, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -1, 1), Blocks.SEAGRASS.getDefaultState(), 2);
        worldgenlevel.setBlockState(blockpos.add(1, -1, -1), water, 2);

        worldgenlevel.setBlockState(blockpos.add(0, -2, 0), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(1, -2, 0), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(0, -2, 1), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -2, 0), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(0, -2, -1), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(1, -2, 1), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -2, -1), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(-1, -2, 1), mud, 2);
        worldgenlevel.setBlockState(blockpos.add(1, -2, -1), mud, 2);

        replaceAir(worldgenlevel, blockpos.add(-2, -1, -1), material);
        replaceAir(worldgenlevel, blockpos.add(-2, -1, 0), material);
        replaceAir(worldgenlevel, blockpos.add(-2, -1, 1), material);
        replaceAir(worldgenlevel, blockpos.add(2, -1, -1), material);
        replaceAir(worldgenlevel, blockpos.add(2, -1, 0), material);
        replaceAir(worldgenlevel, blockpos.add(2, -1, 1), material);
        replaceAir(worldgenlevel, blockpos.add(-1, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.add(0, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.add(1, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.add(-1, -1, 2), material);
        replaceAir(worldgenlevel, blockpos.add(0, -1, 2), material);
        replaceAir(worldgenlevel, blockpos.add(1, -1, 2), material);

        int randomNumber = (int) (Math.random() * (6) + 1);

        if (randomNumber < 2) {
            worldgenlevel.setBlockState(blockpos.add(-3, -1, -1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-3, -1, 0), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-3, -1, 1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-2, -1, 0), water, 2);
            worldgenlevel.setBlockState(blockpos.add(2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, 2), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, -2), randomSapling(), 2);
        } else if (randomNumber < 3) {
            worldgenlevel.setBlockState(blockpos.add(3, -1, -1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(3, -1, 0), material, 2);
            worldgenlevel.setBlockState(blockpos.add(3, -1, 1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(2, -1, 0), water, 2);
            worldgenlevel.setBlockState(blockpos.add(-2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, 2), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, -2), randomSapling(), 2);
        } else if (randomNumber < 4) {
            worldgenlevel.setBlockState(blockpos.add(-1, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(1, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, -2), water, 2);
            worldgenlevel.setBlockState(blockpos.add(2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, 2), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(-2, 0, 0), randomSapling(), 2);
        } else if (randomNumber < 5) {
            worldgenlevel.setBlockState(blockpos.add(-1, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(1, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, 2), water, 2);
            worldgenlevel.setBlockState(blockpos.add(2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(-2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, -2), randomSapling(), 2);
        } else if (randomNumber < 6) {
            worldgenlevel.setBlockState(blockpos.add(-1, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(1, -1, -3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, -2), water, 2);
            worldgenlevel.setBlockState(blockpos.add(3, -1, -1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(3, -1, 0), material, 2);
            worldgenlevel.setBlockState(blockpos.add(3, -1, 1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(2, -1, 0), water, 2);
            worldgenlevel.setBlockState(blockpos.add(-2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, 2), randomSapling(), 2);
        } else if (randomNumber < 7) {
            worldgenlevel.setBlockState(blockpos.add(-1, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(1, -1, 3), material, 2);
            worldgenlevel.setBlockState(blockpos.add(0, -1, 2), water, 2);
            worldgenlevel.setBlockState(blockpos.add(-3, -1, -1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-3, -1, 0), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-3, -1, 1), material, 2);
            worldgenlevel.setBlockState(blockpos.add(-2, -1, 0), water, 2);
            worldgenlevel.setBlockState(blockpos.add(2, 0, 0), randomSapling(), 2);
            worldgenlevel.setBlockState(blockpos.add(0, 0, -2), randomSapling(), 2);
        }

        int randomNumber2 = (int) (Math.random() * (6) + 1);

        if (randomNumber2 < 3) {
            generateCorner(worldgenlevel, blockpos.add(-1, -1, -1), material, slab);
        } else if (randomNumber2 < 4) {
            generateCorner(worldgenlevel, blockpos.add(1, -1, -1), material, slab);
            generateCorner(worldgenlevel, blockpos.add(-1, -1, 1), material, slab);
        } else if (randomNumber2 < 5) {
            generateCorner(worldgenlevel, blockpos.add(1, -1, 1), material, slab);
        } else if (randomNumber2 < 6) {
            generateCorner(worldgenlevel, blockpos.add(-1, -1, -1), material, slab);
            generateCorner(worldgenlevel, blockpos.add(1, -1, 1), material, slab);
        } else if (randomNumber2 < 7) {
            generateCorner(worldgenlevel, blockpos.add(1, -1, -1), material, slab);
            generateCorner(worldgenlevel, blockpos.add(1, -1, 1), material, slab);
        }

        return true;
    }

    private void replaceAir(StructureWorldAccess worldGenLevel, BlockPos blockPos, BlockState material) {
        if (!worldGenLevel.getBlockState(blockPos).getMaterial().isSolid()) {
            worldGenLevel.setBlockState(blockPos, material, 2);
        }
    }

    private void generateCorner(StructureWorldAccess worldGenLevel, BlockPos blockPos, BlockState material, BlockState slab) {
        worldGenLevel.setBlockState(blockPos, material, 2);
        worldGenLevel.setBlockState(blockPos.up(), material, 2);

        int randomNumber = (int) (Math.random() * (3) + 1);
        if (randomNumber < 3) { //66% chance to put a moss carpet ontop.
            replaceAir(worldGenLevel, blockPos.up(2), Blocks.MOSS_CARPET.getDefaultState());
        }

        int randomNumber2 = (int) (Math.random() * (4) + 1);
        if (randomNumber2 < 2) { //25% chance to extend north.
            generateExtension(worldGenLevel, blockPos.north(), material, slab);
        }
    }

    private void generateExtension(StructureWorldAccess worldGenLevel, BlockPos blockPos, BlockState material, BlockState slab) {
        worldGenLevel.setBlockState(blockPos, material, 2);
        int randomNumber = (int) (Math.random() * (3) + 1);
        if (randomNumber < 2) { //33% chance to generate a slab ontop.
            replaceAir(worldGenLevel, blockPos.up(), slab);
        } else if (randomNumber < 3) { //33% chance to generate a solid block ontop.
            worldGenLevel.setBlockState(blockPos.up(), material, 2);
            generateExtensionDecor(worldGenLevel, blockPos.up(2));
        } else { //33% chance to attempt a 50% at generating decor ontop.
            generateExtensionDecor(worldGenLevel, blockPos.up());
        }
    }

    private void generateExtensionDecor(StructureWorldAccess worldGenLevel, BlockPos blockPos) {
        int randomNumber = (int) (Math.random() * (2) + 1);
        if (randomNumber < 2) { //50% chance to generate decoration.
            int randomNumber2 = (int) (Math.random() * (3) + 1);
            if (randomNumber2 < 2) { //66% chance to generate moss.
                replaceAir(worldGenLevel, blockPos, Blocks.MOSS_CARPET.getDefaultState());
            } else { //33% chance to generate a random sapling.
                replaceAir(worldGenLevel, blockPos, randomSapling());
            }
        }
    }

    private BlockState randomSapling() {
        int randomNumber = (int) (Math.random() * (15) + 1);

        if (randomNumber < 2) {
            return Blocks.OAK_SAPLING.getDefaultState();
        } else if (randomNumber < 3) {
            return Blocks.BIRCH_SAPLING.getDefaultState();
        } else if (randomNumber < 4) {
            return Blocks.SPRUCE_SAPLING.getDefaultState();
        } else if (randomNumber < 5) {
            return Blocks.ACACIA_SAPLING.getDefaultState();
        } else if (randomNumber < 6) {
            return Blocks.DARK_OAK_SAPLING.getDefaultState();
        } else if (randomNumber < 7) {
            return Blocks.JUNGLE_SAPLING.getDefaultState();
        } else if (randomNumber < 8) {
            return Blocks.RED_MUSHROOM.getDefaultState();
        } else if (randomNumber < 9) {
            return Blocks.BROWN_MUSHROOM.getDefaultState();
        }

        return Blocks.LARGE_FERN.getDefaultState().with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
    }
}
