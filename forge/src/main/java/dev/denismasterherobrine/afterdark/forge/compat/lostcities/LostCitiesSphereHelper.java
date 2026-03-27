package dev.denismasterherobrine.afterdark.forge.compat.lostcities;

import mcjty.lostcities.config.LostCityProfile;
import mcjty.lostcities.varia.ChunkCoord;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.lost.CitySphere;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import org.jetbrains.annotations.Nullable;


public final class LostCitiesSphereHelper {
    private LostCitiesSphereHelper() {}

    // Clears all non-air blocks in the chunk that are inside the city to prevent terrain spawning
    public static void clearChunkInsideSphere(StructureWorldAccess level, IDimensionInfo dimInfo, ChunkPos chunkPos) {
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
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();
        int baseX = chunkPos.x << 4;
        int baseZ = chunkPos.z << 4;
        int minY = Math.max(level.getBottomY(), cy - (int) radius - 2);
        int maxY = Math.min(level.getTopY() - 1, cy + (int) radius + 2);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
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
                        level.setBlockState(mutable, Blocks.AIR.getDefaultState(), 2);
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
    public static @Nullable ServerWorld toServerWorld(StructureWorldAccess level) {
        if (level instanceof ChunkRegion region) {
            return region.toServerWorld();
        }
        if (level instanceof ServerWorld sw) {
            return sw;
        }
        return null;
    }
}
