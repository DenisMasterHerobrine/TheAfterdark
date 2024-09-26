package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.AnvilRockConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AnvilRockFeature extends Feature<AnvilRockConfiguration> {
    public AnvilRockFeature(Codec<AnvilRockConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<AnvilRockConfiguration> pContext) {
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = new BlockPos((int) pContext.getOrigin().toCenterPos().getX(), pContext.getOrigin().getY(), (int) pContext.getOrigin().toCenterPos().getZ());
        Random random = pContext.getRandom();
        AnvilRockConfiguration config = pContext.getConfig();
        Integer radius = config.getRadius().get(random);
        Integer height = config.getHeight().get(random);
        Integer stretch = config.getStretch().get(random);
        Integer maxHeight = height-1;

        if (worldgenlevel.isAir(blockpos.down())) {
            return false;
        } else {
            int randomNumber = (int)(Math.random()*(4));

            for (int s = 0; s <= stretch; ++s) {
                for (int h = 0; h < height; ++h) {
                    BlockPos pos;

                    if (randomNumber == 3) {
                        pos = blockpos.add(-((h/4)*s), 0, -((h/6)));
                    } else if (randomNumber == 2) {
                        pos = blockpos.add(-((h/6)), 0, -((h/4)*s));
                    } else if (randomNumber == 1) {
                        pos = blockpos.add(((h/4)*s), 0, ((h/6)));
                    } else {
                        pos = blockpos.add(((h/6)), 0, ((h/4)*s));
                    }

                    boolean obese = maxHeight >= 5 && h == 2 || maxHeight >= 5 && h == height - 2;
                    boolean narrow = maxHeight >= 5 && h > 3 && h < height-3;
                    boolean anorexic = maxHeight >= 9 && h > 5 && h < height-5;

                    //Radius 1
                    if (narrow) {
                        genNarrowRadius1(pContext, worldgenlevel, pos, h);
                    } else if (anorexic) {
                        genAnorexicRadius1(pContext, worldgenlevel, pos, h);
                    } else if (obese) {
                        genObeseRadius1(pContext, worldgenlevel, pos, h);
                    } else {
                        genRadius1(pContext, worldgenlevel, pos, h);
                    }

                    //Radius 2
                    if (radius >= 2 || h == 1 || h == maxHeight) {
                        if (narrow) {
                            genNarrowRadius2(pContext, worldgenlevel, pos, h);
                        } else if (!anorexic) {
                            if (obese) {
                                genObeseRadius2(pContext, worldgenlevel, pos, h);
                            } else {
                                genRadius2(pContext, worldgenlevel, pos, h);
                            }
                        }
                    }

                    //Radius 3
                    if (radius >= 3) {
                        if (anorexic) {
                            genNarrowRadius2(pContext, worldgenlevel, pos, h);
                        } else if (narrow) {
                            genRadius2(pContext, worldgenlevel, pos, h);
                        } else if (obese) {
                            genObeseRadius3(pContext, worldgenlevel, pos, h);
                        } else {
                            genRadius3(pContext, worldgenlevel, pos, h);
                        }
                        if (h == 1 || h == maxHeight) {
                            genRadius4(pContext, worldgenlevel, pos, h);
                        }
                    } else if (radius == 2 && h == 1) {
                        genRadius3(pContext, worldgenlevel, pos, h);
                    } else if (radius == 2 && h == maxHeight) {
                        genRadius3(pContext, worldgenlevel, pos, h);
                    }
                }
            }

            return true;
        }
    }

    private void genAnorexicRadius1(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
    }

    private void genNarrowRadius1(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
    }

    private void genRadius1(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
    }

    private void genObeseRadius1(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);

        worldgenlevel.setBlockState(blockpos.add(2, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, 2), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 0), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -2), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 2), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -2), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 2), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -1), getState(pContext, blockpos.add(0, h, 0)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -2), getState(pContext, blockpos.add(0, h, 0)), 2);
    }

    private void genNarrowRadius2(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
    }

    private void genRadius2(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);
    }

    private void genObeseRadius2(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(0, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -2), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);

        worldgenlevel.setBlockState(blockpos.add(0, h, 3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 0), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -3), getState(pContext, blockpos.add(1, h, 1)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -1), getState(pContext, blockpos.add(1, h, 1)), 2);
    }

    private void genRadius3(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(2, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);

        worldgenlevel.setBlockState(blockpos.add(0, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
    }

    private void genObeseRadius3(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(2, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);

        worldgenlevel.setBlockState(blockpos.add(0, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);

        worldgenlevel.setBlockState(blockpos.add(3, h+1, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h+1, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h+1, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h+1, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h-1, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h-1, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h-1, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h-1, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -3), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, 4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 0), getState(pContext, blockpos.add(2, h, 2)), 2);

        worldgenlevel.setBlockState(blockpos.add(1, h, 4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, -1), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -4), getState(pContext, blockpos.add(2, h, 2)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, -2), getState(pContext, blockpos.add(2, h, 2)), 2);
    }

    private void genRadius4(FeatureContext<AnvilRockConfiguration> pContext, StructureWorldAccess worldgenlevel, BlockPos blockpos, int h) {
        h--;
        worldgenlevel.setBlockState(blockpos.add(3, h, 3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -3), getState(pContext, blockpos.add(3, h, 3)), 2);

        worldgenlevel.setBlockState(blockpos.add(0, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 0), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(0, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 0), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 1), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(1, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 1), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, -1), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-1, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, -1), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 2), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(2, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 2), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, -2), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-2, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, -2), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, 3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(3, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, 3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, 4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(4, h, -3), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-3, h, -4), getState(pContext, blockpos.add(3, h, 3)), 2);
        worldgenlevel.setBlockState(blockpos.add(-4, h, -3), getState(pContext, blockpos.add(3, h, 3)), 2);
    }

    private BlockState getState(FeatureContext<AnvilRockConfiguration> pContext, BlockPos pos) {
        return pContext.getConfig().material.getBlockState(pContext.getWorld(), pContext.getRandom(), pos);
    }
}
