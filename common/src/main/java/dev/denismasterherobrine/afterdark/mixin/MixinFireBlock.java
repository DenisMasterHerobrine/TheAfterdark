package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import dev.denismasterherobrine.afterdark.util.FireCheck;
import net.minecraft.block.FireBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public class MixinFireBlock {
//    @Inject(method = "trySpreadingFire", at = @At("HEAD"), cancellable = true)
//    private void preventGrassBurning(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci) {
//        if (world.getServer() != null) {
//            if (world == world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL)
//                    && Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString()) && !Config.INSTANCE.shouldGrassBurn) {
//                ci.cancel();
//            }
//        }
//    }

    @Inject(method = "areBlocksAroundFlammable", at = @At("HEAD"), cancellable = true)
    private void preventGrassBurning(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        FireCheck.GrassAroundCheck(world, pos, cir);
    }

}