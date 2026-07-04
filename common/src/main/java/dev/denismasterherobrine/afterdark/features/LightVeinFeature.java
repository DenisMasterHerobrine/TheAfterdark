package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.features.configuration.LightVeinConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LightVeinFeature extends Feature<LightVeinConfiguration> {
    public LightVeinFeature(Codec<LightVeinConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<LightVeinConfiguration> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        LightVeinConfiguration config = context.getConfig();
        BlockPos anchor = findAnchor(world, context.getOrigin(), config.verticalRange.get(random));
        if (anchor == null) {
            return false;
        }

        int length = config.length.get(random);
        int radius = config.radius.get(random);
        Direction direction = BlackHoneyFeatureUtil.randomHorizontal(random);
        BlockPos cursor = anchor;
        boolean placed = false;

        for (int i = 0; i < length; ++i) {
            cursor = cursor.offset(direction);
            if (random.nextInt(4) == 0) {
                cursor = cursor.add(0, random.nextBoolean() ? 1 : -1, 0);
            }
            placed |= placeRing(world, random, cursor, radius, config);
            if (random.nextInt(3) == 0) {
                direction = direction.rotateYClockwise();
            } else if (random.nextInt(5) == 0) {
                direction = direction.rotateYCounterclockwise();
            }
        }

        return placed;
    }

    private BlockPos findAnchor(StructureWorldAccess world, BlockPos origin, int verticalRange) {
        for (int y = 0; y <= verticalRange; ++y) {
            BlockPos candidate = origin.up(y);
            if (BlackHoneyFeatureUtil.canReplace(world, candidate)) {
                for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
                    if (BlackHoneyFeatureUtil.isSolid(world, candidate.offset(direction))) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private boolean placeRing(StructureWorldAccess world, Random random, BlockPos center, int radius, LightVeinConfiguration config) {
        boolean placed = false;
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            for (int step = 0; step <= radius; ++step) {
                BlockPos pos = center.offset(direction, step);
                if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                    continue;
                }
                BlockState state = random.nextFloat() < config.accentChance
                        ? config.accentProvider.get(random, pos)
                        : config.veinProvider.get(random, pos);
                world.setBlockState(pos, state, 2);
                placed = true;
            }
        }
        if (BlackHoneyFeatureUtil.canReplace(world, center)) {
            world.setBlockState(center, config.veinProvider.get(random, center), 2);
            placed = true;
        }
        return placed;
    }
}
