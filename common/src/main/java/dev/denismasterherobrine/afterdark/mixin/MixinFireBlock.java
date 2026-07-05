package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.FireCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public class MixinFireBlock {
    @Inject(method = "isValidFireLocation", at = @At("HEAD"), cancellable = true)
    private void preventGrassBurning(BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof Level) {
            FireCheck.GrassAroundCheck((Level) world, pos, cir);
        }
    }
}