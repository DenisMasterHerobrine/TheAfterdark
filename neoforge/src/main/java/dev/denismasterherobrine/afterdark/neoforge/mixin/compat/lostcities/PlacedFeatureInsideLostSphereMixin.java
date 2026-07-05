package dev.denismasterherobrine.afterdark.neoforge.mixin.compat.lostcities;

import dev.denismasterherobrine.afterdark.neoforge.compat.lostcities.LostCitiesSphereHelper;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import mcjty.lostcities.setup.Registration;
import mcjty.lostcities.worldgen.IDimensionInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureInsideLostSphereMixin {
    @Inject(method = "placeWithBiomeCheck", at = @At("HEAD"), cancellable = true)
    private void afterdark$skipPlacedFeaturesInLostSpheres(WorldGenLevel world, ChunkGenerator generator, RandomSource random, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        PlacedFeature self = (PlacedFeature) (Object) this;

        ConfiguredFeature<?, ?> configured = self.feature().value();
        Feature<?> feature = configured.feature();

        if (feature.getClass().getName().startsWith("mcjty.lostcities.")) {
            return;
        }

        ServerLevel serverWorld = LostCitiesSphereHelper.toServerWorld(world);

        if (serverWorld == null) {
            return;
        }

        if (!serverWorld.dimension().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return;
        }

        IDimensionInfo dimInfo = Registration.LOSTCITY_FEATURE.get().getDimensionInfo(world);

        if (dimInfo == null) {
            return;
        }

        ChunkPos chunkPos = new ChunkPos(pos);

        if (!LostCitiesSphereHelper.chunkIntersectsEnabledLostSphere(dimInfo, chunkPos)) {
            return;
        }

        cir.setReturnValue(false);
    }
}

