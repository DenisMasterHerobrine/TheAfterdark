package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlackHoneyMourningFlowerFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyMourningFlowerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos base = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 5, 12);
        if (base == null) {
            return false;
        }

        int height = 3 + random.nextInt(4);
        for (int y = 0; y < height; ++y) {
            BlockPos stem = base.up(y);
            BlackHoneyFeatureUtil.placeIfReplaceable(world, stem, stemState(random));
            if (y > 0 && random.nextInt(4) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, stem.offset(BlackHoneyFeatureUtil.randomHorizontal(random)), Blocks.MANGROVE_ROOTS.getDefaultState());
            }
        }

        BlockPos head = base.up(height);
        BlackHoneyFeatureUtil.placeIfReplaceable(world, head, random.nextBoolean()
                ? Blocks.OCHRE_FROGLIGHT.getDefaultState()
                : Blocks.HONEY_BLOCK.getDefaultState());
        placePetals(world, random, head);
        decorateFloor(world, random, base);
        return true;
    }

    private BlockState stemState(Random random) {
        int pick = random.nextInt(5);
        if (pick <= 1) {
            return Blocks.DARK_OAK_FENCE.getDefaultState();
        }
        if (pick == 2) {
            return Blocks.MANGROVE_ROOTS.getDefaultState();
        }
        return Blocks.DARK_OAK_WOOD.getDefaultState();
    }

    private void placePetals(StructureWorldAccess world, Random random, BlockPos head) {
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            BlockPos petal = head.offset(direction);
            BlackHoneyFeatureUtil.placeIfReplaceable(world, petal, petalState(random));
            if (random.nextBoolean()) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, petal.down(), petalState(random));
            }
            if (random.nextInt(3) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, petal.offset(direction), Blocks.HONEYCOMB_BLOCK.getDefaultState());
            }
        }
        BlackHoneyFeatureUtil.placeIfReplaceable(world, head.up(), Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState());
    }

    private BlockState petalState(Random random) {
        int pick = random.nextInt(7);
        if (pick <= 2) {
            return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
        }
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (pick == 5) {
            return Blocks.BROWN_TERRACOTTA.getDefaultState();
        }
        return Blocks.HONEY_BLOCK.getDefaultState();
    }

    private void decorateFloor(StructureWorldAccess world, Random random, BlockPos base) {
        int radius = 2;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius || random.nextInt(3) == 0) {
                    continue;
                }
                BlockPos air = base.add(x, 0, z);
                if (!BlackHoneyFeatureUtil.canReplace(world, air) || !BlackHoneyFeatureUtil.isSolid(world, air.down())) {
                    continue;
                }
                if (random.nextInt(5) == 0) {
                    BlackHoneyFeatureUtil.placeIfReplaceable(world, air, random.nextBoolean()
                            ? Blocks.DANDELION.getDefaultState()
                            : Blocks.ORANGE_TULIP.getDefaultState());
                } else {
                    world.setBlockState(air.down(), random.nextBoolean() ? Blocks.BROWN_TERRACOTTA.getDefaultState() : Blocks.ROOTED_DIRT.getDefaultState(), 2);
                }
            }
        }
    }
}
