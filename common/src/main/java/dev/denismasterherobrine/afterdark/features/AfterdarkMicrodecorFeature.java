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

public class AfterdarkMicrodecorFeature extends Feature<DefaultFeatureConfig> {
    public AfterdarkMicrodecorFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        if (random.nextInt(5) == 0 && placeHangingChains(world, random, context.getOrigin())) {
            return true;
        }

        BlockPos floor = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 8, 24);
        if (floor == null) {
            return false;
        }

        return switch (random.nextInt(5)) {
            case 0 -> placeAshPatch(world, random, floor);
            case 1 -> placeBonePile(world, random, floor);
            case 2 -> placeSculkTrap(world, random, floor);
            case 3 -> placeShardCluster(world, random, floor);
            default -> placeCandleCircle(world, random, floor);
        };
    }

    private boolean placeAshPatch(StructureWorldAccess world, Random random, BlockPos base) {
        boolean placed = false;
        int radius = 2 + random.nextInt(3);
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z > radius * radius + random.nextInt(3)) {
                    continue;
                }
                BlockPos floor = base.add(x, -1, z);
                if (BlackHoneyFeatureUtil.inWorld(world, floor) && world.getBlockState(floor).isSolid()) {
                    world.setBlockState(floor, random.nextInt(4) == 0 ? Blocks.SCULK.getDefaultState() : darkSurface(random), 2);
                    placed = true;
                }
            }
        }
        return placed;
    }

    private boolean placeBonePile(StructureWorldAccess world, Random random, BlockPos base) {
        boolean placed = false;
        for (int i = 0; i < 8 + random.nextInt(8); ++i) {
            BlockPos pos = base.add(BlackHoneyFeatureUtil.signed(random, 3), random.nextInt(2), BlackHoneyFeatureUtil.signed(random, 3));
            if (canPlaceOnFloor(world, pos)) {
                world.setBlockState(pos, random.nextInt(5) == 0 ? Blocks.COBWEB.getDefaultState() : Blocks.BONE_BLOCK.getDefaultState(), 2);
                placed = true;
            }
        }
        return placed;
    }

    private boolean placeSculkTrap(StructureWorldAccess world, Random random, BlockPos base) {
        boolean placed = placeAshPatch(world, random, base);
        placed |= placeOnFloor(world, base, Blocks.SCULK_SENSOR.getDefaultState());
        if (random.nextFloat() < 0.35F) {
            placed |= placeOnFloor(world, base.add(BlackHoneyFeatureUtil.signed(random, 2), 0, BlackHoneyFeatureUtil.signed(random, 2)), Blocks.SCULK_SHRIEKER.getDefaultState());
        }
        return placed;
    }

    private boolean placeShardCluster(StructureWorldAccess world, Random random, BlockPos base) {
        boolean placed = false;
        for (int i = 0; i < 4 + random.nextInt(5); ++i) {
            BlockPos start = base.add(BlackHoneyFeatureUtil.signed(random, 3), 0, BlackHoneyFeatureUtil.signed(random, 3));
            int height = 1 + random.nextInt(4);
            for (int y = 0; y < height; ++y) {
                BlockPos pos = start.up(y);
                if (!BlackHoneyFeatureUtil.canReplace(world, pos) || (y == 0 && !BlackHoneyFeatureUtil.isSolid(world, pos.down()))) {
                    break;
                }
                world.setBlockState(pos, random.nextInt(4) == 0 ? Blocks.CRYING_OBSIDIAN.getDefaultState() : Blocks.COBBLED_DEEPSLATE.getDefaultState(), 2);
                placed = true;
            }
        }
        return placed;
    }

    private boolean placeCandleCircle(StructureWorldAccess world, Random random, BlockPos base) {
        boolean placed = false;
        int radius = 2 + random.nextInt(2);
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            BlockPos pos = base.offset(direction, radius);
            placed |= placeOnFloor(world, pos, random.nextBoolean() ? Blocks.CYAN_CANDLE.getDefaultState() : Blocks.BLACK_CANDLE.getDefaultState());
        }
        if (random.nextBoolean()) {
            placed |= placeOnFloor(world, base, Blocks.SCULK.getDefaultState());
        }
        return placed;
    }

    private boolean placeHangingChains(StructureWorldAccess world, Random random, BlockPos origin) {
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, origin, 28);
        if (ceiling == null) {
            return false;
        }

        int length = 2 + random.nextInt(6);
        boolean placed = false;
        for (int i = 1; i <= length; ++i) {
            BlockPos pos = ceiling.down(i);
            if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                break;
            }
            world.setBlockState(pos, Blocks.CHAIN.getDefaultState(), 2);
            placed = true;
        }
        BlockPos end = ceiling.down(length + 1);
        if (placed && BlackHoneyFeatureUtil.canReplace(world, end)) {
            world.setBlockState(end, random.nextBoolean() ? Blocks.SCULK.getDefaultState() : Blocks.CRYING_OBSIDIAN.getDefaultState(), 2);
        }
        return placed;
    }

    private boolean placeOnFloor(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (!canPlaceOnFloor(world, pos)) {
            return false;
        }
        world.setBlockState(pos, state, 2);
        return true;
    }

    private boolean canPlaceOnFloor(StructureWorldAccess world, BlockPos pos) {
        return BlackHoneyFeatureUtil.canReplace(world, pos) && BlackHoneyFeatureUtil.isSolid(world, pos.down());
    }

    private BlockState darkSurface(Random random) {
        int pick = random.nextInt(8);
        if (pick < 3) {
            return Blocks.DEEPSLATE.getDefaultState();
        }
        if (pick < 5) {
            return Blocks.BLACKSTONE.getDefaultState();
        }
        if (pick < 7) {
            return Blocks.TUFF.getDefaultState();
        }
        return Blocks.CRYING_OBSIDIAN.getDefaultState();
    }
}
