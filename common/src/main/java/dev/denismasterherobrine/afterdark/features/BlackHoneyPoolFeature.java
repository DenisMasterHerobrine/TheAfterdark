package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlackHoneyPoolFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyPoolFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos center = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 4, 12);
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

                BlockPos air = center.add(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.down())) {
                    continue;
                }

                world.setBlockState(air.down(), floorState(air, distance), 2);
                placed = true;

                if (distance < 0.22D && random.nextInt(7) == 0) {
                    world.setBlockState(air, Blocks.HONEY_BLOCK.getDefaultState(), 2);
                }
            }
        }

        return placed;
    }

    private BlockState floorState(BlockPos pos, double distance) {
        int dither = Math.floorMod(pos.getX() * 341873128 + pos.getZ() * 132897987, 100);
        if (distance < 0.28D) {
            return dither < 72 ? Blocks.HONEY_BLOCK.getDefaultState() : Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (distance < 0.52D) {
            return dither < 62 ? Blocks.HONEYCOMB_BLOCK.getDefaultState() : Blocks.YELLOW_TERRACOTTA.getDefaultState();
        }
        if (distance < 0.78D) {
            return dither < 55 ? Blocks.ORANGE_TERRACOTTA.getDefaultState() : Blocks.TERRACOTTA.getDefaultState();
        }
        if (dither < 45) {
            return Blocks.BROWN_TERRACOTTA.getDefaultState();
        }
        if (dither < 72) {
            return Blocks.PACKED_MUD.getDefaultState();
        }
        return Blocks.COARSE_DIRT.getDefaultState();
    }
}
