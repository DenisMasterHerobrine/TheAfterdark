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

public class BlackHoneySurfacePatchFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneySurfacePatchFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos center = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 5, 16);
        if (center == null) {
            return false;
        }

        int radiusX = 10 + random.nextInt(9);
        int radiusZ = 9 + random.nextInt(8);
        boolean placed = false;

        for (int x = -radiusX; x <= radiusX; ++x) {
            for (int z = -radiusZ; z <= radiusZ; ++z) {
                double nx = (double) x / (double) radiusX;
                double nz = (double) z / (double) radiusZ;
                double ripple = Math.sin((center.getX() + x) * 0.29D) * 0.035D
                        + Math.cos((center.getZ() + z) * 0.23D) * 0.03D;
                double distance = nx * nx + nz * nz + ripple;
                if (distance > 1.08D) {
                    continue;
                }

                BlockPos air = center.add(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.down())) {
                    continue;
                }

                world.setBlockState(air.down(), gradientState(air, distance), 2);
                placed = true;
            }
        }

        return placed;
    }

    private BlockState gradientState(BlockPos pos, double distance) {
        int dither = cell(pos, 17);
        if (distance > 0.88D) {
            return savannaGround(dither);
        }

        double tone = 0.5D
                + Math.sin(pos.getX() * 0.105D + pos.getZ() * 0.065D) * 0.22D
                + Math.cos(pos.getX() * 0.045D - pos.getZ() * 0.12D) * 0.17D
                + Math.sin((pos.getX() + pos.getZ()) * 0.18D) * 0.06D
                + (dither - 50) * 0.0018D;

        if (tone < 0.24D) {
            return savannaGround(dither);
        }
        if (tone < 0.4D) {
            if (dither < 46) {
                return Blocks.COARSE_DIRT.getDefaultState();
            }
            if (dither < 74) {
                return Blocks.ROOTED_DIRT.getDefaultState();
            }
            return Blocks.PACKED_MUD.getDefaultState();
        }
        if (tone < 0.56D) {
            if (dither < 70) {
                return Blocks.YELLOW_TERRACOTTA.getDefaultState();
            }
            if (dither < 88) {
                return Blocks.COARSE_DIRT.getDefaultState();
            }
            return Blocks.GRASS_BLOCK.getDefaultState();
        }
        if (tone < 0.72D) {
            if (dither < 68) {
                return Blocks.ORANGE_TERRACOTTA.getDefaultState();
            }
            if (dither < 88) {
                return Blocks.TERRACOTTA.getDefaultState();
            }
            return Blocks.ROOTED_DIRT.getDefaultState();
        }
        if (tone < 0.88D) {
            if (dither < 58) {
                return Blocks.TERRACOTTA.getDefaultState();
            }
            if (dither < 82) {
                return Blocks.RED_TERRACOTTA.getDefaultState();
            }
            return Blocks.BROWN_TERRACOTTA.getDefaultState();
        }

        if (dither < 72) {
            return Blocks.BROWN_TERRACOTTA.getDefaultState();
        }
        return Blocks.COARSE_DIRT.getDefaultState();
    }

    private BlockState savannaGround(int dither) {
        if (dither < 62) {
            return Blocks.GRASS_BLOCK.getDefaultState();
        }
        if (dither < 82) {
            return Blocks.COARSE_DIRT.getDefaultState();
        }
        if (dither < 94) {
            return Blocks.ROOTED_DIRT.getDefaultState();
        }
        return Blocks.PACKED_MUD.getDefaultState();
    }

    private int cell(BlockPos pos, int salt) {
        int hash = pos.getX() * 73428767 ^ pos.getZ() * 912931 ^ salt * 42349;
        hash ^= hash >>> 13;
        hash *= 1274126177;
        hash ^= hash >>> 16;
        return Math.floorMod(hash, 100);
    }
}
