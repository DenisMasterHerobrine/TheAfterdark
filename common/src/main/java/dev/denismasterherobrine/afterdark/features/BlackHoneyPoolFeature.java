package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlackHoneyPoolFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneyPoolFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos center = BlackHoneyFeatureUtil.findFloor(world, context.origin(), 4, 12);
        if (center == null) {
            return false;
        }

        int radiusX = 3 + random.nextInt(3);
        int radiusZ = 3 + random.nextInt(3);
        boolean placed = false;

        for (int x = -radiusX - 1; x <= radiusX + 1; ++x) {
            for (int z = -radiusZ - 1; z <= radiusZ + 1; ++z) {
                double nx = (double) x / (double) radiusX;
                double nz = (double) z / (double) radiusZ;
                double distance = nx * nx + nz * nz + Math.sin((center.getX() + x) * 0.7D) * 0.04D;
                if (distance > 1.18D) {
                    continue;
                }

                BlockPos air = center.offset(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.below())) {
                    continue;
                }

                world.setBlock(air.below(), floorState(air, distance), 2);
                placed = true;

                if (distance < 0.22D && random.nextInt(7) == 0) {
                    world.setBlock(air, Blocks.HONEY_BLOCK.defaultBlockState(), 2);
                }
            }
        }

        return placed;
    }

    private BlockState floorState(BlockPos pos, double distance) {
        int dither = Math.floorMod(pos.getX() * 341873128 + pos.getZ() * 132897987, 100);
        if (distance < 0.28D) {
            return dither < 72 ? Blocks.HONEY_BLOCK.defaultBlockState() : Blocks.HONEYCOMB_BLOCK.defaultBlockState();
        }
        if (distance < 0.52D) {
            return dither < 62 ? Blocks.HONEYCOMB_BLOCK.defaultBlockState() : Blocks.YELLOW_TERRACOTTA.defaultBlockState();
        }
        if (distance < 0.78D) {
            return dither < 55 ? Blocks.ORANGE_TERRACOTTA.defaultBlockState() : Blocks.TERRACOTTA.defaultBlockState();
        }
        if (dither < 45) {
            return Blocks.BROWN_TERRACOTTA.defaultBlockState();
        }
        if (dither < 72) {
            return Blocks.PACKED_MUD.defaultBlockState();
        }
        return Blocks.COARSE_DIRT.defaultBlockState();
    }
}
