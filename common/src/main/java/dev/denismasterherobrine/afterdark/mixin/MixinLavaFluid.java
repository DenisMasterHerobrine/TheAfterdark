package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.FireCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public class MixinLavaFluid {
    @Inject(method = "hasFlammableNeighbours", at = @At("HEAD"), cancellable = true)
    private void preventFireLighting(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof Level) {
            FireCheck.GrassAroundCheck((Level) world, pos, cir);
        }
    }

    @Inject(method = "isFlammable", at = @At("HEAD"), cancellable = true)
    private void preventFireLighting2(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof Level) {
            FireCheck.GrassAroundCheck((Level) world, pos, cir);
        }
    }
}
