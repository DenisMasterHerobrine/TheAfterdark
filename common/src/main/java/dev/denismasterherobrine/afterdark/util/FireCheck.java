package dev.denismasterherobrine.afterdark.util;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FireCheck {
    public static void GrassAroundCheck(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClient) {
            MinecraftServer server = world.getServer();
            if (server != null) {
                if (world == server.getWorld(AfterdarkRegistry.AFTERDARK_LEVEL) && !Config.INSTANCE.shouldGrassBurn) {
                    for (Direction direction : Direction.values()) {
                        if (Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos.offset(direction)).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos.up()).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos.down()).getBlock()).toString())
                                || Config.INSTANCE.GrassBlocks.contains(Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString())) {
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }
}
