package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.DoubleBlockConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class PondFeature extends Feature<DoubleBlockConfiguration> {
    public PondFeature(Codec<DoubleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<DoubleBlockConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        RandomSource random = pContext.random();
        BlockState material = pContext.config().toPlace().getState(random, blockpos);
        BlockState slab = pContext.config().slabToPlace().getState(random, blockpos);
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState mud = Blocks.MUD.defaultBlockState();

        if (!worldgenlevel.getBlockState(blockpos.offset(3, 0, 0)).canBeReplaced() && !worldgenlevel.getBlockState(blockpos.offset(0, 0, 3)).canBeReplaced() ||
                !worldgenlevel.getBlockState(blockpos.offset(-3, 0, 0)).canBeReplaced() && !worldgenlevel.getBlockState(blockpos.offset(0, 0, -3)).canBeReplaced() ||
                !worldgenlevel.getBlockState(blockpos.offset(2, -1, 2)).canBeReplaced() && !worldgenlevel.getBlockState(blockpos.offset(-2, -1, 2)).canBeReplaced() ||
                !worldgenlevel.getBlockState(blockpos.offset(-2, -1, -2)).canBeReplaced() && !worldgenlevel.getBlockState(blockpos.offset(2, -1, -2)).canBeReplaced()) {

            return false;
        }

        worldgenlevel.setBlock(blockpos.offset(0, -1, 0), water, 2);
        worldgenlevel.setBlock(blockpos.offset(0, 0, 0), Blocks.LILY_PAD.defaultBlockState(), 2);
        worldgenlevel.setBlock(blockpos.offset(1, -1, 0), water, 2);
        worldgenlevel.setBlock(blockpos.offset(0, -1, 1), water, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -1, 0), water, 2);
        worldgenlevel.setBlock(blockpos.offset(0, -1, -1), water, 2);
        worldgenlevel.setBlock(blockpos.offset(1, -1, 1), water, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -1, -1), water, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -1, 1), water, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -1, 1), Blocks.SEAGRASS.defaultBlockState(), 2);
        worldgenlevel.setBlock(blockpos.offset(1, -1, -1), water, 2);

        worldgenlevel.setBlock(blockpos.offset(0, -2, 0), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(1, -2, 0), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(0, -2, 1), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -2, 0), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(0, -2, -1), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(1, -2, 1), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -2, -1), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(-1, -2, 1), mud, 2);
        worldgenlevel.setBlock(blockpos.offset(1, -2, -1), mud, 2);

        replaceAir(worldgenlevel, blockpos.offset(-2, -1, -1), material);
        replaceAir(worldgenlevel, blockpos.offset(-2, -1, 0), material);
        replaceAir(worldgenlevel, blockpos.offset(-2, -1, 1), material);
        replaceAir(worldgenlevel, blockpos.offset(2, -1, -1), material);
        replaceAir(worldgenlevel, blockpos.offset(2, -1, 0), material);
        replaceAir(worldgenlevel, blockpos.offset(2, -1, 1), material);
        replaceAir(worldgenlevel, blockpos.offset(-1, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.offset(0, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.offset(1, -1, -2), material);
        replaceAir(worldgenlevel, blockpos.offset(-1, -1, 2), material);
        replaceAir(worldgenlevel, blockpos.offset(0, -1, 2), material);
        replaceAir(worldgenlevel, blockpos.offset(1, -1, 2), material);

        int randomNumber = random.nextInt(6) + 1;

        if (randomNumber < 2) {
            worldgenlevel.setBlock(blockpos.offset(-3, -1, -1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-3, -1, 0), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-3, -1, 1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-2, -1, 0), water, 2);
            worldgenlevel.setBlock(blockpos.offset(2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, 2), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, -2), randomSapling(random), 2);
        } else if (randomNumber < 3) {
            worldgenlevel.setBlock(blockpos.offset(3, -1, -1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(3, -1, 0), material, 2);
            worldgenlevel.setBlock(blockpos.offset(3, -1, 1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(2, -1, 0), water, 2);
            worldgenlevel.setBlock(blockpos.offset(-2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, 2), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, -2), randomSapling(random), 2);
        } else if (randomNumber < 4) {
            worldgenlevel.setBlock(blockpos.offset(-1, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(1, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, -2), water, 2);
            worldgenlevel.setBlock(blockpos.offset(2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, 2), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(-2, 0, 0), randomSapling(random), 2);
        } else if (randomNumber < 5) {
            worldgenlevel.setBlock(blockpos.offset(-1, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(1, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, 2), water, 2);
            worldgenlevel.setBlock(blockpos.offset(2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(-2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, -2), randomSapling(random), 2);
        } else if (randomNumber < 6) {
            worldgenlevel.setBlock(blockpos.offset(-1, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(1, -1, -3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, -2), water, 2);
            worldgenlevel.setBlock(blockpos.offset(3, -1, -1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(3, -1, 0), material, 2);
            worldgenlevel.setBlock(blockpos.offset(3, -1, 1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(2, -1, 0), water, 2);
            worldgenlevel.setBlock(blockpos.offset(-2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, 2), randomSapling(random), 2);
        } else if (randomNumber < 7) {
            worldgenlevel.setBlock(blockpos.offset(-1, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(1, -1, 3), material, 2);
            worldgenlevel.setBlock(blockpos.offset(0, -1, 2), water, 2);
            worldgenlevel.setBlock(blockpos.offset(-3, -1, -1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-3, -1, 0), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-3, -1, 1), material, 2);
            worldgenlevel.setBlock(blockpos.offset(-2, -1, 0), water, 2);
            worldgenlevel.setBlock(blockpos.offset(2, 0, 0), randomSapling(random), 2);
            worldgenlevel.setBlock(blockpos.offset(0, 0, -2), randomSapling(random), 2);
        }

        int randomNumber2 = random.nextInt(6) + 1;

        if (randomNumber2 < 3) {
            generateCorner(worldgenlevel, blockpos.offset(-1, -1, -1), material, slab, random);
        } else if (randomNumber2 < 4) {
            generateCorner(worldgenlevel, blockpos.offset(1, -1, -1), material, slab, random);
            generateCorner(worldgenlevel, blockpos.offset(-1, -1, 1), material, slab, random);
        } else if (randomNumber2 < 5) {
            generateCorner(worldgenlevel, blockpos.offset(1, -1, 1), material, slab, random);
        } else if (randomNumber2 < 6) {
            generateCorner(worldgenlevel, blockpos.offset(-1, -1, -1), material, slab, random);
            generateCorner(worldgenlevel, blockpos.offset(1, -1, 1), material, slab, random);
        } else if (randomNumber2 < 7) {
            generateCorner(worldgenlevel, blockpos.offset(1, -1, -1), material, slab, random);
            generateCorner(worldgenlevel, blockpos.offset(1, -1, 1), material, slab, random);
        }

        return true;
    }

    private void replaceAir(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material) {
        if (!worldGenLevel.getBlockState(blockPos).isSolid()) {
            worldGenLevel.setBlock(blockPos, material, 2);
        }
    }

    private void generateCorner(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material, BlockState slab, RandomSource random) {
        worldGenLevel.setBlock(blockPos, material, 2);
        worldGenLevel.setBlock(blockPos.above(), material, 2);

        int randomNumber = random.nextInt(3) + 1;
        if (randomNumber < 3) { //66% chance to put a moss carpet ontop.
            replaceAir(worldGenLevel, blockPos.above(2), Blocks.MOSS_CARPET.defaultBlockState());
        }

        int randomNumber2 = random.nextInt(4) + 1;
        if (randomNumber2 < 2) { //25% chance to extend north.
            generateExtension(worldGenLevel, blockPos.north(), material, slab, random);
        }
    }

    private void generateExtension(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material, BlockState slab, RandomSource random) {
        worldGenLevel.setBlock(blockPos, material, 2);
        int randomNumber = random.nextInt(3) + 1;
        if (randomNumber < 2) { //33% chance to generate a slab ontop.
            replaceAir(worldGenLevel, blockPos.above(), slab);
        } else if (randomNumber < 3) { //33% chance to generate a solid block ontop.
            worldGenLevel.setBlock(blockPos.above(), material, 2);
            generateExtensionDecor(worldGenLevel, blockPos.above(2), random);
        } else { //33% chance to attempt a 50% at generating decor ontop.
            generateExtensionDecor(worldGenLevel, blockPos.above(), random);
        }
    }

    private void generateExtensionDecor(WorldGenLevel worldGenLevel, BlockPos blockPos, RandomSource random) {
        int randomNumber = random.nextInt(2) + 1;
        if (randomNumber < 2) { //50% chance to generate decoration.
            int randomNumber2 = random.nextInt(3) + 1;
            if (randomNumber2 < 2) { //66% chance to generate moss.
                replaceAir(worldGenLevel, blockPos, Blocks.MOSS_CARPET.defaultBlockState());
            } else { //33% chance to generate a random sapling.
                replaceAir(worldGenLevel, blockPos, randomSapling(random));
            }
        }
    }

    private static BlockState randomSapling(RandomSource random) {
        int randomNumber = random.nextInt(15) + 1;

        if (randomNumber < 2) {
            return Blocks.OAK_SAPLING.defaultBlockState();
        } else if (randomNumber < 3) {
            return Blocks.BIRCH_SAPLING.defaultBlockState();
        } else if (randomNumber < 4) {
            return Blocks.SPRUCE_SAPLING.defaultBlockState();
        } else if (randomNumber < 5) {
            return Blocks.ACACIA_SAPLING.defaultBlockState();
        } else if (randomNumber < 6) {
            return Blocks.DARK_OAK_SAPLING.defaultBlockState();
        } else if (randomNumber < 7) {
            return Blocks.JUNGLE_SAPLING.defaultBlockState();
        } else if (randomNumber < 8) {
            return Blocks.CHERRY_SAPLING.defaultBlockState();
        } else if (randomNumber < 9) {
            return Blocks.RED_MUSHROOM.defaultBlockState();
        } else if (randomNumber < 10) {
            return Blocks.BROWN_MUSHROOM.defaultBlockState();
        }

        return Blocks.LARGE_FERN.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
    }
}
