package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlackHoneyCocoonClusterFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyCocoonClusterFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.getOrigin(), 20);
        if (ceiling == null) {
            return false;
        }

        int cocoons = 2 + random.nextInt(5);
        boolean placed = false;
        for (int i = 0; i < cocoons; ++i) {
            BlockPos near = ceiling.down().add(BlackHoneyFeatureUtil.signed(random, 3), 0, BlackHoneyFeatureUtil.signed(random, 3));
            BlockPos localCeiling = BlackHoneyFeatureUtil.findCeiling(world, near, 5);
            if (localCeiling == null) {
                continue;
            }
            placed |= placeCocoon(world, random, localCeiling.down(), 2 + random.nextInt(4));
        }
        return placed;
    }

    private boolean placeCocoon(StructureWorldAccess world, Random random, BlockPos anchor, int height) {
        boolean placed = false;
        for (int y = 0; y < height; ++y) {
            int radius = (y == 0 || y == height - 1) ? 0 : 1;
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    if (radius == 1 && Math.abs(x) + Math.abs(z) > 1 + random.nextInt(2)) {
                        continue;
                    }
                    BlockPos pos = anchor.add(x, -y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    world.setBlockState(pos, cocoonState(random, y, height), 2);
                    placed = true;
                }
            }
        }

        BlockPos core = anchor.down(Math.max(1, height / 2));
        if (random.nextInt(3) == 0) {
            BlackHoneyFeatureUtil.placeIfReplaceable(world, core, Blocks.YELLOW_TERRACOTTA.getDefaultState());
        }
        for (int i = 0; i < 5; ++i) {
            BlockPos web = core.add(BlackHoneyFeatureUtil.signed(random, 2), BlackHoneyFeatureUtil.signed(random, 1), BlackHoneyFeatureUtil.signed(random, 2));
            if (random.nextInt(3) != 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, web, Blocks.WEEPING_VINES_PLANT.getDefaultState());
            }
        }
        return placed;
    }

    private BlockState cocoonState(Random random, int y, int height) {
        if (y == height - 1 && random.nextInt(4) == 0) {
            return Blocks.BEE_NEST.getDefaultState();
        }
        int pick = random.nextInt(10);
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (pick <= 7) {
            return Blocks.HONEY_BLOCK.getDefaultState();
        }
        if (pick == 8) {
            return Blocks.YELLOW_TERRACOTTA.getDefaultState();
        }
        return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
    }
}
