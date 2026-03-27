package dev.denismasterherobrine.afterdark.forge.mixin.compat.lostcities;

import dev.denismasterherobrine.afterdark.forge.compat.lostcities.LostCitiesSphereHelper;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.LostCityFeature;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LostCityFeature.class)
public abstract class LostCityFeatureCompatMixin {
    @Inject(method = "generate", at = @At("HEAD"))
    private void afterdark$clearTerrainInsideSpheresBeforeLostCities(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        LostCityFeature self = (LostCityFeature) (Object) this;

        var world = context.getWorld();

        if (!(world instanceof ChunkRegion region)) {
            return;
        }

        IDimensionInfo dimInfo = self.getDimensionInfo(world);

        if (dimInfo == null) {
            return;
        }

        ChunkPos chunkPos = region.getCenterPos();
        LostCitiesSphereHelper.clearChunkInsideSphere(world, dimInfo, chunkPos);
    }
}
