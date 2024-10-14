package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.CatchingFallConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CatchingFallFeature extends Feature<CatchingFallConfiguration> {
    public CatchingFallFeature(Codec<CatchingFallConfiguration> pContext) {
        super(pContext);
    }

    public boolean generate(FeatureContext<CatchingFallConfiguration> pContext) {
        CatchingFallConfiguration springconfiguration = pContext.getConfig();
        StructureWorldAccess worldgenlevel = pContext.getWorld();
        BlockPos blockpos = pContext.getOrigin();

        if (!springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.up()).getBlock().getDefaultState())) {
            return false;
        } else if (springconfiguration.requiresBlockBelow && !springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.down()).getBlock())) {
            return false;
        } else {
            BlockState blockstate = worldgenlevel.getBlockState(blockpos);

            if (!blockstate.isAir() && !blockstate.isOf(springconfiguration.state.getBlockState().getBlock()) && !springconfiguration.validBlocks.contains(blockstate.getBlock())) {
                return false;
            } else {
                int i = 0;
                int j = 0;

                if (springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.west()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.east()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.north()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.south()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(worldgenlevel.getBlockState(blockpos.down()).getBlock())) {
                    ++j;
                }

                int k = 0;

                if (worldgenlevel.isAir(blockpos.west())) {
                    ++k;
                }

                if (worldgenlevel.isAir(blockpos.east())) {
                    ++k;
                }

                if (worldgenlevel.isAir(blockpos.north())) {
                    ++k;
                }

                if (worldgenlevel.isAir(blockpos.south())) {
                    ++k;
                }

                if (worldgenlevel.isAir(blockpos.down())) {
                    ++k;
                }

                if (j >= springconfiguration.rockCount && k >= springconfiguration.holeCount) {
                    BlockPos blockpos1 = blockpos.down();
                    Boolean shouldGenerate = true;

                    if (!worldgenlevel.isAir(blockpos1.down())) {
                        blockpos1 = blockpos1.up();
                        if (worldgenlevel.isAir(blockpos1.north())) {
                            blockpos1 = blockpos1.north();
                        } else if (worldgenlevel.isAir(blockpos1.east())) {
                            blockpos1 = blockpos1.east();
                        } else if (worldgenlevel.isAir(blockpos1.south())) {
                            blockpos1 = blockpos1.south();
                        } else if (worldgenlevel.isAir(blockpos1.west())) {
                            blockpos1 = blockpos1.west();
                        }
                    }

                    if (worldgenlevel.isAir(blockpos1.down(5))) {
                        for(int l = 0; l < 200; ++l) {
                            BlockState blockBelow = worldgenlevel.getBlockState(blockpos1.down());

                            if (springconfiguration.validBlocks.contains(blockBelow.getBlock())) {
                                blockpos1 = blockpos1.down();
                                worldgenlevel.setBlockState(blockpos1, springconfiguration.state.getBlockState(), 2);
//                                worldgenlevel.scheduleFluidTick(blockpos1, springconfiguration.state.getFluid(), 0);
                                worldgenlevel.setBlockState(blockpos1.north(), springconfiguration.state.getBlockState(), 2);
//                                worldgenlevel.scheduleFluidTick(blockpos1.north(), springconfiguration.state.getFluid(), 0);
                                worldgenlevel.setBlockState(blockpos1.east(), springconfiguration.state.getBlockState(), 2);
//                                worldgenlevel.scheduleFluidTick(blockpos1.east(), springconfiguration.state.getFluid(), 0);
                                worldgenlevel.setBlockState(blockpos1.south(), springconfiguration.state.getBlockState(), 2);
//                                worldgenlevel.scheduleFluidTick(blockpos1.south(), springconfiguration.state.getFluid(), 0);
                                worldgenlevel.setBlockState(blockpos1.west(), springconfiguration.state.getBlockState(), 2);
//                                worldgenlevel.scheduleFluidTick(blockpos1.west(), springconfiguration.state.getFluid(), 0);

                                worldgenlevel.setBlockState(blockpos1.south().south(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.south().south().south(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.south().south().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.south().south().south().up(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.south().south().west(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.south().west(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.south().west().west(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.west().west(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.west().north(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.west().north().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.west().west().north(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.west().north().north(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.west().north().north().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.west().north().north().north(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.north().north(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.north().north().east(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.north().north().east().east(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.north().east(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.north().east().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.north().east().east().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.north().east().east(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.east().east(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                worldgenlevel.setBlockState(blockpos1.east().south(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.east().south().up(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.east().east().south(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.east().south().south(), springconfiguration.basinMaterial.getDefaultState(), 2);
                                worldgenlevel.setBlockState(blockpos1.east().east().south().south(), springconfiguration.basinMaterial.getDefaultState(), 2);

                                l = 200;
                            } else if (springconfiguration.invalidBlocks.contains(blockBelow.getBlock())) { //cant generate over these to prevent large lava cast-like formations
                                l = 200;
                                shouldGenerate = false;
                            } else {
                                blockpos1 = blockpos1.down();
                            }
                        }

                        if (shouldGenerate) {
                            worldgenlevel.setBlockState(blockpos, springconfiguration.state.getBlockState(), 2);
//                            worldgenlevel.scheduleFluidTick(blockpos, springconfiguration.state.getFluid(), 0);
                        }
                    }

                    ++i;
                }

                return i > 0;
            }
        }
    }
}
