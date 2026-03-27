package dev.denismasterherobrine.afterdark.forge.mixin.compat.lostcities;

import dev.denismasterherobrine.afterdark.forge.compat.lostcities.LostCitiesSphereHelper;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import mcjty.lostcities.setup.Registration;
import mcjty.lostcities.worldgen.IDimensionInfo;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureInsideLostSphereMixin {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void afterdark$skipPlacedFeaturesInLostSpheres(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        PlacedFeature self = (PlacedFeature) (Object) this;

        ConfiguredFeature<?, ?> configured = self.feature().value();
        Feature<?> feature = configured.feature();

        if (feature.getClass().getName().startsWith("mcjty.lostcities.")) {
            return;
        }

        ServerWorld serverWorld = LostCitiesSphereHelper.toServerWorld(world);

        if (serverWorld == null) {
            return;
        }

        if (!serverWorld.getRegistryKey().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
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
