package dev.denismasterherobrine.afterdark.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.util.DripstoneHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DripstoneHelper.class)
public interface DripstoneHelperMixin {
    @Invoker("canGenerateOrLava")
    static boolean invokeCanGenerateOrLava(WorldAccess world, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("canGenerateBase")
    static boolean invokeCanGenerateBase(StructureWorldAccess world, BlockPos pos, int radius) {
        throw new AssertionError();
    }

    @Invoker("scaleHeightFromRadius")
    static double invokeScaleHeightFromRadius(double pRadius, double pBaseRadius, double pScale, double pBluntness) {
        throw new AssertionError();
    }
}