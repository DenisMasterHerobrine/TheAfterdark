package dev.denismasterherobrine.afterdark.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DripstoneUtils.class)
public interface DripstoneHelperMixin {
    @Invoker("isEmptyOrWaterOrLava")
    static boolean invokeCanGenerateOrLava(LevelAccessor world, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("isCircleMostlyEmbeddedInStone")
    static boolean invokeCanGenerateBase(WorldGenLevel world, BlockPos pos, int radius) {
        throw new AssertionError();
    }

    @Invoker("getDripstoneHeight")
    static double invokeScaleHeightFromRadius(double pRadius, double pBaseRadius, double pScale, double pBluntness) {
        throw new AssertionError();
    }
}