package dev.denismasterherobrine.afterdark.util;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FireCheck {
    public static void GrassAroundCheck(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClientSide) {
            MinecraftServer server = world.getServer();
            if (server != null) {
                if (world == server.getLevel(AfterdarkRegistry.AFTERDARK_LEVEL) && !Config.INSTANCE.shouldGrassBurn) {
                    for (Direction direction : Direction.values()) {
                        if (Config.INSTANCE.GrassBlocks.contains(BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos.relative(direction)).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos.above()).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos.below()).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos).getBlock()).toString())) {
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }
}
