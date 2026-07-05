package dev.denismasterherobrine.afterdark.neoforge.compat.lostcities;

import mcjty.lostcities.config.LostCityProfile;
import mcjty.lostcities.varia.ChunkCoord;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.lost.BuildingInfo;
import mcjty.lostcities.worldgen.lost.CitySphere;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;


public final class LostCitiesSphereHelper {
    private LostCitiesSphereHelper() {}

    // Clears terrain/features from the city's airspace while preserving the floor Lost Cities/Lost Worlds builds on.
    public static void clearChunkAboveLostCityFloorInsideSphere(WorldGenLevel level, IDimensionInfo dimInfo, ChunkPos chunkPos) {
        LostCityProfile profile = dimInfo.getProfile();
        if (!profile.isSpace() && !profile.isSpheres()) {
            return;
        }
        ChunkCoord coord = new ChunkCoord(dimInfo.getType(), chunkPos.x, chunkPos.z);
        CitySphere sphere = CitySphere.getCitySphere(coord, dimInfo);
        if (!sphere.isEnabled()) {
            return;
        }
        float radius = sphere.getRadius();
        BlockPos center = sphere.getCenterPos();
        double r2 = radius * radius;
        BuildingInfo buildingInfo = BuildingInfo.getBuildingInfo(coord, dimInfo);
        int floorY = buildingInfo.getCityGroundLevel();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        int baseX = chunkPos.x << 4;
        int baseZ = chunkPos.z << 4;
        int minY = Math.max(level.getMinBuildHeight(), floorY + 1);
        int maxY = Math.min(level.getMaxBuildHeight() - 1, cy + (int) Math.ceil(radius));
        if (minY > maxY) {
            return;
        }
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                int wx = baseX + lx;
                int wz = baseZ + lz;
                double dx = wx - cx;
                double dz = wz - cz;
                if (dx * dx + dz * dz > r2) {
                    continue;
                }
                for (int y = minY; y <= maxY; y++) {
                    double dy = y - cy;
                    if (dx * dx + dy * dy + dz * dz > r2) {
                        continue;
                    }
                    mutable.set(wx, y, wz);
                    if (!level.getBlockState(mutable).isAir()) {
                        level.setBlock(mutable, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    // check for the presence of an enabled sphere in the chunk to skip feature generation early
    public static boolean chunkIntersectsEnabledLostSphere(IDimensionInfo dimInfo, ChunkPos chunkPos) {
        LostCityProfile profile = dimInfo.getProfile();

        if (!profile.isSpace() && !profile.isSpheres()) {
            return false;
        }

        ChunkCoord coord = new ChunkCoord(dimInfo.getType(), chunkPos.x, chunkPos.z);
        return CitySphere.intersectsWithCitySphere(coord, dimInfo);
    }

    // Converts a StructureWorldAccess to ServerWorld if possible, since the LC API requires a ServerWorld for dimension info lookups and we want to avoid doing instanceof checks in mixins as much as possible.
    public static @Nullable ServerLevel toServerWorld(WorldGenLevel level) {
        if (level instanceof WorldGenRegion region) {
            return region.getLevel();
        }
        if (level instanceof ServerLevel sw) {
            return sw;
        }
        return null;
    }
}

