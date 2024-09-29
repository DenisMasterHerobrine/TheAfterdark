package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeDripstoneFeatureConfig;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.DripstoneHelper;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LargeCoalOrePillarFeature extends Feature<LargeDripstoneFeatureConfig> {
    public LargeCoalOrePillarFeature(Codec<LargeDripstoneFeatureConfig> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<LargeDripstoneFeatureConfig> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = new BlockPos((int) pContext.getOrigin().toCenterPos().getX(), pContext.getOrigin().getY(), (int) pContext.getOrigin().toCenterPos().getZ());
        net.minecraft.util.math.random.Random random = pContext.getRandom();
        LargeDripstoneFeatureConfig config = pContext.getConfig();

        if (!DripstoneHelper.canGenerateOrLava(worldgenlevel, blockpos)) {
            return false;
        } else {
            Optional<CaveSurface> optional = CaveSurface.create(worldgenlevel, blockpos, config.floorToCeilingSearchRange, DripstoneHelper::canGenerate, DripstoneHelper::canReplaceOrLava);

            if (optional.isPresent() && optional.get() instanceof CaveSurface.Bounded) {
                CaveSurface.Bounded column$range = (CaveSurface.Bounded) optional.get();
                if (column$range.getHeight() < 4) {
                    return false;
                } else {
                    int i = (int) ((float) column$range.getHeight() * config.maxColumnRadiusToCaveHeightRatio);
                    int j = MathHelper.clamp(i, config.columnRadius.getMin(), config.columnRadius.getMax());
                    int k = MathHelper.nextBetween(random, config.columnRadius.getMin(), j);
                    LargePillar largepillarfeature$largepillar = makeDarkPrismarine(blockpos.withY(column$range.getCeiling() - 1), false, random, k, config.stalactiteBluntness, config.heightScale);
                    LargePillar largepillarfeature$largepillar1 = makeDarkPrismarine(blockpos.withY(column$range.getFloor() + 1), true, random, k, config.stalagmiteBluntness, config.heightScale);
                    WindOffsetter largepillarfeature$windoffsetter;
                    if (largepillarfeature$largepillar.isSuitableForWind(config) && largepillarfeature$largepillar1.isSuitableForWind(config)) {
                        largepillarfeature$windoffsetter = new WindOffsetter(blockpos.getY(), random, config.windSpeed);
                    } else {
                        largepillarfeature$windoffsetter = WindOffsetter.noWind();
                    }

                    boolean flag = largepillarfeature$largepillar.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largepillarfeature$windoffsetter);
                    boolean flag1 = largepillarfeature$largepillar1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largepillarfeature$windoffsetter);
                    if (flag) {
                        largepillarfeature$largepillar.placeBlocks(worldgenlevel, random, largepillarfeature$windoffsetter);
                    }

                    if (flag1) {
                        largepillarfeature$largepillar1.placeBlocks(worldgenlevel, random, largepillarfeature$windoffsetter);
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private static LargePillar makeDarkPrismarine(BlockPos pRoot, boolean pPointingUp, net.minecraft.util.math.random.Random pRandom, int pRadius, FloatProvider pBluntnessBase, FloatProvider pScaleBase) {
        return new LargePillar(pRoot, pPointingUp, pRadius, (double)pBluntnessBase.get(pRandom), (double)pScaleBase.get(pRandom));
    }

    static final class LargePillar {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargePillar(BlockPos pRoot, boolean pPointingUp, int pRadius, double pBluntness, double pScale) {
            this.root = pRoot;
            this.pointingUp = pPointingUp;
            this.radius = pRadius;
            this.bluntness = pBluntness;
            this.scale = pScale;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        private int getMinY() {
            return this.pointingUp ? this.root.getY() : this.root.getY() - this.getHeight();
        }

        private int getMaxY() {
            return !this.pointingUp ? this.root.getY() : this.root.getY() + this.getHeight();
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(StructureWorldAccess pLevel, WindOffsetter pWindOffsetter) {
            while(this.radius > 1) {
                BlockPos.Mutable blockpos$mutableblockpos = this.root.mutableCopy();
                int i = Math.min(10, this.getHeight());

                for(int j = 0; j < i; ++j) {
                    if (pLevel.getBlockState(blockpos$mutableblockpos).isOf(Blocks.LAVA)) {
                        return false;
                    }

                    if (DripstoneHelper.canGenerateBase(pLevel, pWindOffsetter.offset(blockpos$mutableblockpos), this.radius)) {
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
            return (int)DripstoneHelper.scaleHeightFromRadius((double)pRadius, (double)this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(StructureWorldAccess pLevel, net.minecraft.util.math.random.Random pRandom, WindOffsetter pWindOffsetter) {
            for(int i = -this.radius; i <= this.radius; ++i) {
                for(int j = -this.radius; j <= this.radius; ++j) {
                    float f = MathHelper.sqrt((float)(i * i + j * j));
                    if (!(f > (float)this.radius)) {
                        int k = this.getHeightAtRadius(f);
                        if (k > 0) {
                            if ((double)pRandom.nextFloat() < 0.2D) {
                                k = (int)((float)k * MathHelper.nextBetween(pRandom, 0.8F, 1.0F));
                            }

                            BlockPos.Mutable blockpos$mutableblockpos = this.root.add(i, 0, j).mutableCopy();
                            boolean flag = false;
                            int l = this.pointingUp ? pLevel.getTopY(Heightmap.Type.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) : Integer.MAX_VALUE;

                            for(int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; ++i1) {
                                BlockPos blockpos = pWindOffsetter.offset(blockpos$mutableblockpos);
                                if (DripstoneHelper.canGenerateOrLava(pLevel, blockpos)) {
                                    flag = true;
                                    Block block = Blocks.COAL_ORE;
                                    pLevel.setBlockState(blockpos, block.getDefaultState(), 2);
                                } else if (flag && pLevel.getBlockState(blockpos).isIn(BlockTags.BASE_STONE_OVERWORLD)) {
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
            return this.radius >= pConfig.minRadiusForWind && this.bluntness >= (double)pConfig.minBluntnessForWind;
        }
    }

    static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3d windSpeed;

        WindOffsetter(int pOriginY, net.minecraft.util.math.random.Random pRandom, FloatProvider pMagnitude) {
            this.originY = pOriginY;
            float f = pMagnitude.get(pRandom);
            float f1 = MathHelper.nextBetween(pRandom, 0.0F, (float)Math.PI);
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
            } else {
                int i = this.originY - pPos.getY();
                Vec3d vec3 = this.windSpeed.multiply(i);
                return pPos.add((int) vec3.x, 0, (int) vec3.z);
            }
        }
    }
}
