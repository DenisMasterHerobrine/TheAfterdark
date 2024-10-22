package dev.denismasterherobrine.afterdark.util;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FireCheck {
    public static void GrassAroundCheck(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().getServer() != null) {
            if (world == MinecraftClient.getInstance().getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL) && !Config.INSTANCE.shouldGrassBurn) {
                for (Direction direction : Direction.values()) {
                    if (Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos.offset(direction)).getBlock()).toString())) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
