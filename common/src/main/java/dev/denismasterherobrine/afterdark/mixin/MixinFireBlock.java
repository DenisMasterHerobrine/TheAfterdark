package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.FireCheck;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public class MixinFireBlock {

    @Inject(method = "areBlocksAroundFlammable", at = @At("HEAD"), cancellable = true)
    private void preventGrassBurning(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        FireCheck.GrassAroundCheck((World) world, pos, cir);
    }

}