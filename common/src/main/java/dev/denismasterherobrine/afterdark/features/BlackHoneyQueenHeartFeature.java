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

public class BlackHoneyQueenHeartFeature extends Feature<DefaultFeatureConfig> {
    public BlackHoneyQueenHeartFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos ceiling = BlackHoneyFeatureUtil.findCeiling(world, context.getOrigin(), 28);
        if (ceiling == null) {
            return false;
        }

        int height = 7 + random.nextInt(4);
        int radius = 3 + random.nextInt(2);
        BlockPos anchor = ceiling.down();
        boolean placed = false;

        for (int y = 0; y < height; ++y) {
            double sliceRadius = sliceRadius(y, height, radius);
            int blockRadius = Math.max(1, (int) Math.ceil(sliceRadius));
            for (int x = -blockRadius; x <= blockRadius; ++x) {
                for (int z = -blockRadius; z <= blockRadius; ++z) {
                    if (!insideHeartSlice(x, z, y, height, sliceRadius, random)) {
                        continue;
                    }
                    BlockPos pos = anchor.add(x, -y, z);
                    if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                        continue;
                    }
                    double dist = Math.sqrt(x * x + z * z);
                    world.setBlockState(pos, heartState(random, dist, sliceRadius), 2);
                    placed = true;
                }
            }
        }

        if (placed) {
            placeVeins(world, random, anchor, radius, height);
            placeDrip(world, random, anchor.down(height));
        }
        return placed;
    }

    private double sliceRadius(int y, int height, int radius) {
        if (y == 0) {
            return 1.5D;
        }
        if (y <= 2) {
            return radius;
        }
        double t = (double) (y - 2) / (double) Math.max(1, height - 3);
        return Math.max(0.8D, radius * (1.0D - t * t));
    }

    private boolean insideHeartSlice(int x, int z, int y, int height, double radius, Random random) {
        double dist = Math.sqrt(x * x + z * z);
        if (y <= 2) {
            double lobeA = Math.sqrt((x - 1.25D) * (x - 1.25D) + z * z);
            double lobeB = Math.sqrt((x + 1.25D) * (x + 1.25D) + z * z);
            return lobeA <= radius * 0.82D || lobeB <= radius * 0.82D || dist <= radius * 0.62D;
        }
        if (y >= height - 2) {
            return dist <= radius + random.nextFloat() * 0.15D;
        }
        return dist <= radius + random.nextFloat() * 0.2D;
    }

    private BlockState heartState(Random random, double dist, double radius) {
        if (dist > radius - 0.75D) {
            int pick = random.nextInt(9);
            if (pick <= 4) {
                return Blocks.HONEYCOMB_BLOCK.getDefaultState();
            }
            if (pick <= 6) {
                return Blocks.BROWN_TERRACOTTA.getDefaultState();
            }
            return Blocks.ORANGE_TERRACOTTA.getDefaultState();
        }
        int pick = random.nextInt(14);
        if (pick == 0) {
            return Blocks.OCHRE_FROGLIGHT.getDefaultState();
        }
        if (pick <= 6) {
            return Blocks.HONEY_BLOCK.getDefaultState();
        }
        if (pick <= 9) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        }
        if (pick == 10) {
            return Blocks.YELLOW_TERRACOTTA.getDefaultState();
        }
        return Blocks.ORANGE_TERRACOTTA.getDefaultState();
    }

    private void placeVeins(StructureWorldAccess world, Random random, BlockPos anchor, int radius, int height) {
        for (Direction direction : BlackHoneyFeatureUtil.HORIZONTAL) {
            int veinLength = 2 + random.nextInt(4);
            BlockPos cursor = anchor.offset(direction, radius).down(1 + random.nextInt(Math.max(1, height / 2)));
            for (int i = 0; i < veinLength; ++i) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, cursor, BlackHoneyFeatureUtil.darkRoot(random));
                cursor = cursor.offset(direction).up(random.nextInt(2));
            }
        }
        for (int i = 0; i < 12; ++i) {
            BlockPos web = anchor.add(BlackHoneyFeatureUtil.signed(random, radius + 1), -random.nextInt(height), BlackHoneyFeatureUtil.signed(random, radius + 1));
            if (random.nextInt(3) == 0) {
                BlackHoneyFeatureUtil.placeIfReplaceable(world, web, Blocks.WEEPING_VINES_PLANT.getDefaultState());
            }
        }
    }

    private void placeDrip(StructureWorldAccess world, Random random, BlockPos start) {
        int length = 2 + random.nextInt(5);
        BlockPos cursor = start;
        for (int i = 0; i < length; ++i) {
            if (!BlackHoneyFeatureUtil.canReplace(world, cursor)) {
                return;
            }
            world.setBlockState(cursor, i == length - 1 ? Blocks.HONEYCOMB_BLOCK.getDefaultState() : Blocks.HONEY_BLOCK.getDefaultState(), 2);
            cursor = cursor.down();
        }
    }
}
