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

public class BlackHoneyHiveCystFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyHiveCystFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos center = context.getOrigin();
        if (!BlackHoneyFeatureUtil.canReplace(world, center) || !hasAnchor(world, center)) {
            return false;
        }

        int radius = 2 + random.nextInt(2);
        boolean placed = false;
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    double dist = (x * x + y * y * 1.2D + z * z) / (double) (radius * radius);
                    if (dist > 1.0D + random.nextFloat() * 0.22D) {
                        continue;
                    }
                    BlockPos pos = center.add(x, y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    world.setBlockState(pos, cystState(random, dist), 2);
                    placed = true;
                }
            }
        }

        for (int i = 0; i < 7; ++i) {
            BlockPos decor = center.add(BlackHoneyFeatureUtil.signed(random, radius + 1), BlackHoneyFeatureUtil.signed(random, radius), BlackHoneyFeatureUtil.signed(random, radius + 1));
            BlockState state = random.nextBoolean() ? Blocks.ORANGE_TERRACOTTA.getDefaultState() : Blocks.MANGROVE_ROOTS.getDefaultState();
            BlackHoneyFeatureUtil.placeIfReplaceable(world, decor, state);
        }
        return placed;
    }

    private boolean hasAnchor(StructureWorldAccess world, BlockPos center) {
        if (BlackHoneyFeatureUtil.isSolid(world, center.up())) {
            return true;
        }
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            if (BlackHoneyFeatureUtil.isSolid(world, center.offset(direction))) {
                return true;
            }
        }
        return false;
    }

    private BlockState cystState(Random random, double dist) {
        if (dist < 0.35D && random.nextInt(6) == 0) {
            return Blocks.OCHRE_FROGLIGHT.getDefaultState();
        }
        if (dist < 0.55D) {
            return random.nextBoolean() ? Blocks.HONEY_BLOCK.getDefaultState() : Blocks.ORANGE_TERRACOTTA.getDefaultState();
        }
        int pick = random.nextInt(10);
        if (pick <= 4) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (pick <= 6) {
            return Blocks.ORANGE_TERRACOTTA.getDefaultState();
        }
        if (pick <= 8) {
            return Blocks.BROWN_TERRACOTTA.getDefaultState();
        }
        return Blocks.HONEY_BLOCK.getDefaultState();
    }
}
