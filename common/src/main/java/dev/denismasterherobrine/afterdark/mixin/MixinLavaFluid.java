package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.FireCheck;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public class MixinLavaFluid {

    @Inject(method = "canLightFire", at = @At("HEAD"), cancellable = true)
    private void preventFireLighting(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        FireCheck.GrassAroundCheck(world, pos, cir);
    }
}
