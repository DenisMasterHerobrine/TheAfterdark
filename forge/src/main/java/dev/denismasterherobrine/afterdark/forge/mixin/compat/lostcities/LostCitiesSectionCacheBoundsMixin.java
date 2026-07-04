package dev.denismasterherobrine.afterdark.forge.mixin.compat.lostcities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "mcjty.lostcities.worldgen.ChunkDriver$SectionCache", remap = false)
public abstract class LostCitiesSectionCacheBoundsMixin {
    @Shadow @Final private int minY;
    @Shadow @Final private int maxY;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void afterdark$getBlockOutsideCachedHeightAsAir(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (isOutsideCachedHeight(pos.getY())) {
            cir.setReturnValue(Blocks.AIR.getDefaultState());
        }
    }

    @Inject(method = "put", at = @At("HEAD"), cancellable = true)
    private void afterdark$skipBlockOutsideCachedHeight(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (isOutsideCachedHeight(pos.getY())) {
            ci.cancel();
        }
    }

    private boolean isOutsideCachedHeight(int y) {
        return y < minY || y >= maxY;
    }
}
