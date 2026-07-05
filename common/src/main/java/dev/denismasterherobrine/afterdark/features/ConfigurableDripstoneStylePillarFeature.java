package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.PillarFeatureConfiguration;
import dev.denismasterherobrine.afterdark.mixin.DripstoneHelperMixin;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ConfigurableDripstoneStylePillarFeature extends Feature<PillarFeatureConfiguration> {
    private final PillarPlacementRules defaultRules;

    public ConfigurableDripstoneStylePillarFeature(Codec<PillarFeatureConfiguration> codec, PillarPlacementRules defaultRules) {
        super(codec);
        this.defaultRules = defaultRules;
    }

    @Override
    public boolean place(FeaturePlaceContext<PillarFeatureConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = new BlockPos(
                (int) pContext.origin().getCenter().x(),
                pContext.origin().getY(),
                (int) pContext.origin().getCenter().z());
        RandomSource random = pContext.random();
        PillarFeatureConfiguration fullConfig = pContext.config();
        LargeDripstoneConfiguration config = fullConfig.base();
        Optional<BlockStateProvider> pillarOverride = fullConfig.pillarProvider();

        if (!DripstoneHelperMixin.invokeCanGenerateOrLava(worldgenlevel, blockpos)) {
            return false;
        }
        Optional<Column> optional = Column.scan(
                worldgenlevel, blockpos, config.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isDripstoneBaseOrLava);

        if (optional.isEmpty() || !(optional.get() instanceof Column.Range)) {
            return false;
        }
        Column.Range column$range = (Column.Range) optional.get();
        if (column$range.height() < 4) {
            return false;
        }
        int i = (int) ((float) column$range.height() * config.maxColumnRadiusToCaveHeightRatio);
        int j = Mth.clamp(i, config.columnRadius.getMinValue(), config.columnRadius.getMaxValue());
        int k = Mth.randomBetweenInclusive(random, config.columnRadius.getMinValue(), j);
        LargePillar upper = LargePillar.create(
                blockpos.atY(column$range.ceiling() - 1),
                false,
                random,
                k,
                config.stalactiteBluntness,
                config.heightScale,
                defaultRules,
                pillarOverride);
        LargePillar lower = LargePillar.create(
                blockpos.atY(column$range.floor() + 1),
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
                RandomSource pRandom,
                int pRadius,
                FloatProvider pBluntnessBase,
                FloatProvider pScaleBase,
                PillarPlacementRules rules,
                Optional<BlockStateProvider> pillarOverride) {
            return new LargePillar(
                    pRoot,
                    pPointingUp,
                    pRadius,
                    (double) pBluntnessBase.sample(pRandom),
                    (double) pScaleBase.sample(pRandom),
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

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel pLevel, WindOffsetter pWindOffsetter) {
            while (this.radius > 1) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.mutable();
                int i = Math.min(10, this.getHeight());

                for (int j = 0; j < i; ++j) {
                    if (pLevel.getBlockState(blockpos$mutableblockpos).is(Blocks.LAVA)) {
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

        void placeBlocks(WorldGenLevel pLevel, RandomSource pRandom, WindOffsetter pWindOffsetter, int columnRadiusParam) {
            for (int i = -this.radius; i <= this.radius; ++i) {
                for (int j = -this.radius; j <= this.radius; ++j) {
                    float f = Mth.sqrt((float) (i * i + j * j));
                    if (!(f > (float) this.radius)) {
                        int k = this.getHeightAtRadius(f);
                        if (k > 0) {
                            if ((double) pRandom.nextFloat() < 0.2D) {
                                k = (int) ((float) k * Mth.randomBetween(pRandom, 0.8F, 1.0F));
                            }

                            BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.offset(i, 0, j).mutable();
                            boolean flag = false;
                            int l = this.pointingUp
                                    ? pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ())
                                    : Integer.MAX_VALUE;

                            for (int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; ++i1) {
                                BlockPos blockpos = pWindOffsetter.offset(blockpos$mutableblockpos);
                                if (DripstoneHelperMixin.invokeCanGenerateOrLava(pLevel, blockpos)) {
                                    flag = true;
                                    BlockState state = pillarOverride
                                            .map(provider -> provider.getState(pRandom, blockpos))
                                            .orElseGet(() -> rules.pick(pLevel, pRandom, blockpos, columnRadiusParam));
                                    pLevel.setBlock(blockpos, state, 2);
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

        boolean isSuitableForWind(LargeDripstoneConfiguration pConfig) {
            return this.radius >= pConfig.minRadiusForWind && this.bluntness >= (double) pConfig.minBluntnessForWind;
        }
    }

    static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3 windSpeed;

        WindOffsetter(int pOriginY, RandomSource pRandom, FloatProvider pMagnitude) {
            this.originY = pOriginY;
            float f = pMagnitude.sample(pRandom);
            float f1 = Mth.randomBetween(pRandom, 0.0F, (float) Math.PI);
            this.windSpeed = new Vec3(Mth.cos(f1) * f, 0.0D, Mth.sin(f1) * f);
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
            Vec3 vec3 = this.windSpeed.scale(i);
            return pPos.offset((int) vec3.x, 0, (int) vec3.z);
        }
    }
}
