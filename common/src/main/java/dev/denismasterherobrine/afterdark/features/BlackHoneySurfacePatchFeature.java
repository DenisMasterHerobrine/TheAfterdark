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

public class BlackHoneySurfacePatchFeature extends Feature<NoneFeatureConfiguration> {
    public BlackHoneySurfacePatchFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockPos center = BlackHoneyFeatureUtil.findFloor(world, origin, 5, 16);
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

                BlockPos air = center.offset(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.below())) {
                    continue;
                }

                placed |= BlackHoneyFeatureUtil.placeSurfaceBlockInOriginChunk(world, origin, air, gradientState(air, distance));
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
                return Blocks.COARSE_DIRT.defaultBlockState();
            }
            if (dither < 74) {
                return Blocks.ROOTED_DIRT.defaultBlockState();
            }
            return Blocks.PACKED_MUD.defaultBlockState();
        }
        if (tone < 0.56D) {
            if (dither < 70) {
                return Blocks.YELLOW_TERRACOTTA.defaultBlockState();
            }
            if (dither < 88) {
                return Blocks.COARSE_DIRT.defaultBlockState();
            }
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        if (tone < 0.72D) {
            if (dither < 68) {
                return Blocks.ORANGE_TERRACOTTA.defaultBlockState();
            }
            if (dither < 88) {
                return Blocks.TERRACOTTA.defaultBlockState();
            }
            return Blocks.ROOTED_DIRT.defaultBlockState();
        }
        if (tone < 0.88D) {
            if (dither < 58) {
                return Blocks.TERRACOTTA.defaultBlockState();
            }
            if (dither < 82) {
                return Blocks.RED_TERRACOTTA.defaultBlockState();
            }
            return Blocks.BROWN_TERRACOTTA.defaultBlockState();
        }

        if (dither < 72) {
            return Blocks.BROWN_TERRACOTTA.defaultBlockState();
        }
        return Blocks.COARSE_DIRT.defaultBlockState();
    }

    private BlockState savannaGround(int dither) {
        if (dither < 62) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        if (dither < 82) {
            return Blocks.COARSE_DIRT.defaultBlockState();
        }
        if (dither < 94) {
            return Blocks.ROOTED_DIRT.defaultBlockState();
        }
        return Blocks.PACKED_MUD.defaultBlockState();
    }

    private int cell(BlockPos pos, int salt) {
        int hash = pos.getX() * 73428767 ^ pos.getZ() * 912931 ^ salt * 42349;
        hash ^= hash >>> 13;
        hash *= 1274126177;
        hash ^= hash >>> 16;
        return Math.floorMod(hash, 100);
    }
}
