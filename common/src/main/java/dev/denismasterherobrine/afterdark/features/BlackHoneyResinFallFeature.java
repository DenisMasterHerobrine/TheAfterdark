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

public class BlackHoneyResinFallFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyResinFallFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.getOrigin(), 24);
        if (ceiling == null) {
            return false;
        }

        BlockPos cursor = ceiling.down();
        int length = 5 + random.nextInt(14);
        boolean placed = false;
        BlockPos lastAir = cursor;

        for (int i = 0; i < length && BlackHoneyFeatureUtil.canReplace(world, cursor); ++i) {
            BlockState state = random.nextInt(7) == 0
                    ? Blocks.HONEYCOMB_BLOCK.getDefaultState()
                    : Blocks.HONEY_BLOCK.getDefaultState();
            world.setBlockState(cursor, state, 2);
            placed = true;
            lastAir = cursor;

            if (i < 3 || random.nextInt(5) == 0) {
                placeCeilingNodule(world, random, cursor);
            }
            if (i > 2 && random.nextInt(9) == 0) {
                Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
                BlackHoneyFeatureUtil.placeIfReplaceable(world, cursor.offset(side), Blocks.HONEY_BLOCK.getDefaultState());
            }

            cursor = cursor.down();
        }

        if (placed) {
            spreadLandingPool(world, random, lastAir);
        }
        return placed;
    }

    private void placeCeilingNodule(StructureWorldAccess world, Random random, BlockPos pos) {
        Direction side = BlackHoneyFeatureUtil.randomHorizontal(random);
        BlockState state = random.nextBoolean() ? Blocks.HONEYCOMB_BLOCK.getDefaultState() : Blocks.MANGROVE_ROOTS.getDefaultState();
        BlackHoneyFeatureUtil.placeIfReplaceable(world, pos.offset(side), state);
    }

    private void spreadLandingPool(StructureWorldAccess world, Random random, BlockPos base) {
        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, base, 1, 8);
        if (floor == null) {
            return;
        }
        int radius = 1 + random.nextInt(2);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(2)) {
                    continue;
                }
                BlockPos air = floor.add(x, 0, z);
                if (BlackHoneyFeatureUtil.canReplace(world, air) && BlackHoneyFeatureUtil.isSolid(world, air.down())) {
                    BlockState state = random.nextInt(4) == 0 ? Blocks.MUD.getDefaultState() : Blocks.HONEY_BLOCK.getDefaultState();
                    world.setBlockState(air.down(), state, 2);
                }
            }
        }
    }
}
