package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.DarkTarConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class DarkTarFeature extends Feature<DarkTarConfiguration> {
    public DarkTarFeature(Codec<DarkTarConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DarkTarConfiguration> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        DarkTarConfiguration config = context.getConfig();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.getOrigin(), 28);
        if (ceiling == null) {
            return false;
        }

        int length = config.length.get(random);
        BlockPos cursor = ceiling.down();
        BlockPos last = cursor;
        boolean placed = false;

        for (int i = 0; i < length && BlackHoneyFeatureUtil.canReplace(world, cursor); ++i) {
            world.setBlockState(cursor, config.columnProvider.get(random, cursor), 2);
            placed = true;
            last = cursor;

            if (random.nextFloat() < config.sideChance) {
                Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
                BlockPos sidePos = cursor.offset(side);
                if (BlackHoneyFeatureUtil.canReplace(world, sidePos)) {
                    world.setBlockState(sidePos, config.sideProvider.get(random, sidePos), 2);
                }
            }

            cursor = cursor.down();
        }

        if (placed && random.nextFloat() < config.poolChance) {
            spreadPool(world, random, last, config);
        }
        return placed;
    }

    private void spreadPool(StructureWorldAccess world, Random random, BlockPos base, DarkTarConfiguration config) {
        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, base, 1, 10);
        if (floor == null) {
            return;
        }
        int radius = config.poolRadius.get(random);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                BlockPos target = floor.add(x, -1, z);
                if (!BlackHoneyFeatureUtil.inWorld(world, target)) {
                    continue;
                }
                if (world.getBlockState(target).isSolid()) {
                    BlockState state = config.poolProvider.get(random, target);
                    world.setBlockState(target, state, 2);
                }
            }
        }
    }
}
