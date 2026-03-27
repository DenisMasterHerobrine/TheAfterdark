package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.PillarFeatureConfiguration;
import dev.denismasterherobrine.afterdark.mixin.DripstoneHelperMixin;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeDripstoneFeatureConfig;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.DripstoneHelper;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.jetbrains.annotations.Nullable;

public class ConfigurableDripstoneStylePillarFeature extends Feature<PillarFeatureConfiguration> {
    private final PillarPlacementRules defaultRules;

    public ConfigurableDripstoneStylePillarFeature(Codec<PillarFeatureConfiguration> codec, PillarPlacementRules defaultRules) {
        super(codec);
        this.defaultRules = defaultRules;
    }

    @Override
    public boolean generate(FeatureContext<PillarFeatureConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = new BlockPos(
                (int) pContext.getOrigin().toCenterPos().getX(),
                pContext.getOrigin().getY(),
                (int) pContext.getOrigin().toCenterPos().getZ());
        Random random = pContext.getRandom();
        PillarFeatureConfiguration fullConfig = pContext.getConfig();
        LargeDripstoneFeatureConfig config = fullConfig.base();
        Optional<BlockStateProvider> pillarOverride = fullConfig.pillarProvider();

        if (!DripstoneHelperMixin.invokeCanGenerateOrLava(worldgenlevel, blockpos)) {
            return false;
        }
        Optional<CaveSurface> optional = CaveSurface.create(
                worldgenlevel, blockpos, config.floorToCeilingSearchRange, DripstoneHelper::canGenerate, DripstoneHelper::canReplaceOrLava);

        if (optional.isEmpty() || !(optional.get() instanceof CaveSurface.Bounded)) {
            return false;
        }
        CaveSurface.Bounded column$range = (CaveSurface.Bounded) optional.get();
        if (column$range.getHeight() < 4) {
            return false;
        }
        int i = (int) ((float) column$range.getHeight() * config.maxColumnRadiusToCaveHeightRatio);
        int j = MathHelper.clamp(i, config.columnRadius.getMin(), config.columnRadius.getMax());
        int k = MathHelper.nextBetween(random, config.columnRadius.getMin(), j);
        LargePillar upper = LargePillar.create(
                blockpos.withY(column$range.getCeiling() - 1),
                false,
                random,
                k,
                config.stalactiteBluntness,
                config.heightScale,
                defaultRules,
                pillarOverride);
        LargePillar lower = LargePillar.create(
                blockpos.withY(column$range.getFloor() + 1),
                true,
                random,
                k,
                config.stalagmiteBluntness,
                config.heightScale,
                defaultRules,
                pillarOverride);
        WindOffsetter windOffsetter;
        if (upper.isSuitableForWind(config) && lower.isSuitableForWind(config)) {
            windOffsetter = new WindOffsetter(blockpos.getY(), random, config.windSpeed);
        } else {
            windOffsetter = WindOffsetter.noWind();
        }

        boolean flag = upper.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, windOffsetter);
        boolean flag1 = lower.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, windOffsetter);
        if (flag) {
            upper.placeBlocks(worldgenlevel, random, windOffsetter, k);
        }
        if (flag1) {
            lower.placeBlocks(worldgenlevel, random, windOffsetter, k);
        }
        return true;
    }

    static final class LargePillar {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;
        private final PillarPlacementRules rules;
        private final Optional<BlockStateProvider> pillarOverride;

        static LargePillar create(
                BlockPos pRoot,
                boolean pPointingUp,
                Random pRandom,
                int pRadius,
                FloatProvider pBluntnessBase,
                FloatProvider pScaleBase,
                PillarPlacementRules rules,
                Optional<BlockStateProvider> pillarOverride) {
            return new LargePillar(
                    pRoot,
                    pPointingUp,
                    pRadius,
                    (double) pBluntnessBase.get(pRandom),
                    (double) pScaleBase.get(pRandom),
                    rules,
                    pillarOverride);
        }

        LargePillar(
                BlockPos pRoot,
                boolean pPointingUp,
                int pRadius,
                double pBluntness,
                double pScale,
                PillarPlacementRules rules,
                Optional<BlockStateProvider> pillarOverride) {
            this.root = pRoot;
            this.pointingUp = pPointingUp;
            this.radius = pRadius;
            this.bluntness = pBluntness;
            this.scale = pScale;
            this.rules = rules;
            this.pillarOverride = pillarOverride;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(StructureWorldAccess pLevel, WindOffsetter pWindOffsetter) {
            while (this.radius > 1) {
                BlockPos.Mutable blockpos$mutableblockpos = this.root.mutableCopy();
                int i = Math.min(10, this.getHeight());

                for (int j = 0; j < i; ++j) {
                    if (pLevel.getBlockState(blockpos$mutableblockpos).isOf(Blocks.LAVA)) {
                        return false;
                    }

                    if (DripstoneHelperMixin.invokeCanGenerateBase(pLevel, pWindOffsetter.offset(blockpos$mutableblockpos), this.radius)) {
                        this.root = blockpos$mutableblockpos;
                        return true;
                    }

                    blockpos$mutableblockpos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeightAtRadius(float pRadius) {
            return (int) DripstoneHelperMixin.invokeScaleHeightFromRadius(
                    (double) pRadius, (double) this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(StructureWorldAccess pLevel, Random pRandom, WindOffsetter pWindOffsetter, int columnRadiusParam) {
            for (int i = -this.radius; i <= this.radius; ++i) {
                for (int j = -this.radius; j <= this.radius; ++j) {
                    float f = MathHelper.sqrt((float) (i * i + j * j));
                    if (!(f > (float) this.radius)) {
                        int k = this.getHeightAtRadius(f);
                        if (k > 0) {
                            if ((double) pRandom.nextFloat() < 0.2D) {
                                k = (int) ((float) k * MathHelper.nextBetween(pRandom, 0.8F, 1.0F));
                            }

                            BlockPos.Mutable blockpos$mutableblockpos = this.root.add(i, 0, j).mutableCopy();
                            boolean flag = false;
                            int l = this.pointingUp
                                    ? pLevel.getTopY(Heightmap.Type.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ())
                                    : Integer.MAX_VALUE;

                            for (int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; ++i1) {
                                BlockPos blockpos = pWindOffsetter.offset(blockpos$mutableblockpos);
                                if (DripstoneHelperMixin.invokeCanGenerateOrLava(pLevel, blockpos)) {
                                    flag = true;
                                    BlockState state = pillarOverride
                                            .map(provider -> provider.get(pRandom, blockpos))
                                            .orElseGet(() -> rules.pick(pLevel, pRandom, blockpos, columnRadiusParam));
                                    pLevel.setBlockState(blockpos, state, 2);
                                } else if (rules.shouldStop(pLevel.getBlockState(blockpos), flag)) {
                                    break;
                                }

                                blockpos$mutableblockpos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }
        }

        boolean isSuitableForWind(LargeDripstoneFeatureConfig pConfig) {
            return this.radius >= pConfig.minRadiusForWind && this.bluntness >= (double) pConfig.minBluntnessForWind;
        }
    }

    static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3d windSpeed;

        WindOffsetter(int pOriginY, Random pRandom, FloatProvider pMagnitude) {
            this.originY = pOriginY;
            float f = pMagnitude.get(pRandom);
            float f1 = MathHelper.nextBetween(pRandom, 0.0F, (float) Math.PI);
            this.windSpeed = new Vec3d(MathHelper.cos(f1) * f, 0.0D, MathHelper.sin(f1) * f);
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        static WindOffsetter noWind() {
            return new WindOffsetter();
        }

        BlockPos offset(BlockPos pPos) {
            if (this.windSpeed == null) {
                return pPos;
            }
            int i = this.originY - pPos.getY();
            Vec3d vec3 = this.windSpeed.multiply(i);
            return pPos.add((int) vec3.x, 0, (int) vec3.z);
        }
    }
}
