package dev.denismasterherobrine.afterdark.neoforge.mixin.compat.lostcities;

import dev.denismasterherobrine.afterdark.neoforge.compat.lostcities.LostCitiesSphereHelper;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.LostCityFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LostCityFeature.class)
public abstract class LostCityFeatureCompatMixin {
    @Inject(method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z", at = @At("HEAD"))
    private void afterdark$clearTerrainAboveSphereFloorBeforeLostCities(FeaturePlaceContext<NoneFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        LostCityFeature self = (LostCityFeature) (Object) this;

        var world = context.level();

        if (!(world instanceof WorldGenRegion region)) {
            return;
        }

        ServerLevel serverWorld = LostCitiesSphereHelper.toServerWorld(world);

        if (serverWorld == null || !serverWorld.dimension().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return;
        }

        IDimensionInfo dimInfo = self.getDimensionInfo(world);

        if (dimInfo == null) {
            return;
        }

        dimInfo.setWorld(world);

        ChunkPos chunkPos = region.getCenter();
        LostCitiesSphereHelper.clearChunkAboveLostCityFloorInsideSphere(world, dimInfo, chunkPos);
    }
}

