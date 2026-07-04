package dev.denismasterherobrine.afterdark.features;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class GothicCopperRuinFeature extends Feature<DefaultFeatureConfig> {
    private static final Identifier COPPER_NECROPOLIS_RUIN_LOOT_TABLE = new Identifier(TheAfterdark.MOD_ID, "chests/copper_necropolis_ruin");

    public GothicCopperRuinFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos base = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 10, 24);
        if (base == null || !hasRoom(world, base, 4, 8)) {
            return false;
        }

        Direction forward = BlackHoneyFeatureUtil.randomHorizontal(random);
        int variant = random.nextInt(20);
        boolean placed = switch (variant % 5) {
            case 0 -> generateTower(world, random, base, forward, variant);
            case 1 -> generateChapel(world, random, base, forward, variant);
            case 2 -> generateArchHall(world, random, base, forward, variant);
            case 3 -> generateGate(world, random, base, forward, variant);
            default -> generateBrokenSpire(world, random, base, forward, variant);
        };

        if (placed) {
            placed |= tryPlaceLootChest(world, random, base, forward, variant);
        }
        return placed;
    }

    private boolean hasRoom(StructureWorldAccess world, BlockPos base, int radius, int height) {
        for (int y = 1; y <= height; y += 2) {
            if (!BlackHoneyFeatureUtil.canReplace(world, base.up(y))) {
                return false;
            }
        }
        return BlackHoneyFeatureUtil.isSolid(world, base.down())
                && BlackHoneyFeatureUtil.canReplace(world, base.add(radius, 1, 0))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(-radius, 1, 0))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(0, 1, radius))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(0, 1, -radius));
    }

    private boolean tryPlaceLootChest(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        if (random.nextFloat() >= 0.2F) {
            return false;
        }

        int[][] candidates = chestCandidates(variant);
        int start = random.nextInt(candidates.length);
        for (int i = 0; i < candidates.length; ++i) {
            int[] candidate = candidates[(start + i) % candidates.length];
            if (placeLootChest(world, random, local(base, forward, candidate[0], candidate[1], candidate[2]), forward)) {
                return true;
            }
        }
        return false;
    }

    private int[][] chestCandidates(int variant) {
        return switch (variant % 5) {
            case 0 -> new int[][]{{0, 0, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
            case 1 -> new int[][]{{0, 0, 6}, {1, 0, 5}, {-1, 0, 5}, {0, 0, 4}};
            case 2 -> new int[][]{{0, 0, 3}, {0, 0, 6}, {1, 0, 6}, {-1, 0, 6}};
            case 3 -> new int[][]{{0, 0, 1}, {0, 0, -1}, {1, 0, 1}, {-1, 0, -1}};
            default -> new int[][]{{0, 0, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
        };
    }

    private boolean placeLootChest(StructureWorldAccess world, Random random, BlockPos pos, Direction forward) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos) || !BlackHoneyFeatureUtil.isSolid(world, pos.down())) {
            return false;
        }

        world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, forward.getOpposite()), 2);
        if (world.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(COPPER_NECROPOLIS_RUIN_LOOT_TABLE, random.nextLong());
        }
        return true;
    }

    private boolean generateTower(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        int radius = 2 + variant / 10;
        int height = 8 + variant / 5 + random.nextInt(3);
        boolean placed = placeFloor(world, random, base, forward, radius, radius, -radius, radius);

        for (int y = 0; y <= height; ++y) {
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    boolean edge = Math.abs(x) == radius || Math.abs(z) == radius;
                    boolean corner = Math.abs(x) == radius && Math.abs(z) == radius;
                    boolean window = !corner && y >= 2 && y <= height - 2 && ((x == 0 && Math.abs(z) == radius) || (z == 0 && Math.abs(x) == radius));
                    if (edge && !window) {
                        placed |= placeAirBlock(world, local(base, forward, x, y, z), corner ? trim(random) : wall(random));
                    }
                }
            }
        }

        for (int x : new int[]{-radius - 1, radius + 1}) {
            for (int z : new int[]{-radius - 1, radius + 1}) {
                placed |= placeColumn(world, random, base, forward, x, z, height + 2, true);
            }
        }
        placed |= placeSpire(world, random, base, forward, 0, 0, height + 1, radius + 1);
        return placed;
    }

    private boolean generateChapel(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        int halfWidth = 3 + variant / 10;
        int length = 8 + variant / 5 + random.nextInt(3);
        int height = 5 + variant / 10 + random.nextInt(2);
        boolean placed = placeFloor(world, random, base, forward, halfWidth, halfWidth, -1, length);

        for (int z = 0; z <= length; ++z) {
            for (int y = 0; y <= height; ++y) {
                boolean window = z % 3 == 1 && y >= 2 && y <= height - 1;
                if (!window) {
                    placed |= placeAirBlock(world, local(base, forward, -halfWidth, y, z), wall(random));
                    placed |= placeAirBlock(world, local(base, forward, halfWidth, y, z), wall(random));
                }
            }
            if (z % 3 == 0) {
                placed |= placeColumn(world, random, base, forward, -halfWidth - 1, z, height + 1, false);
                placed |= placeColumn(world, random, base, forward, halfWidth + 1, z, height + 1, false);
            }
        }

        for (int x = -halfWidth; x <= halfWidth; ++x) {
            for (int y = 0; y <= height; ++y) {
                boolean archedDoor = Math.abs(x) <= 1 && y <= 3;
                if (!archedDoor) {
                    placed |= placeAirBlock(world, local(base, forward, x, y, 0), wall(random));
                    placed |= placeAirBlock(world, local(base, forward, x, y, length), wall(random));
                }
            }
        }

        for (int layer = 0; layer <= halfWidth; ++layer) {
            int y = height + layer + 1;
            int x = halfWidth - layer;
            for (int z = 0; z <= length; ++z) {
                placed |= placeAirBlock(world, local(base, forward, -x, y, z), layer == halfWidth ? trim(random) : roof(random));
                placed |= placeAirBlock(world, local(base, forward, x, y, z), layer == halfWidth ? trim(random) : roof(random));
            }
        }
        placed |= placeSpire(world, random, base, forward, 0, length / 2, height + halfWidth + 2, 2);
        return placed;
    }

    private boolean generateArchHall(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        int halfWidth = 4;
        int arches = 3 + variant / 5;
        int height = 6 + random.nextInt(3);
        boolean placed = placeFloor(world, random, base, forward, halfWidth, halfWidth, -1, arches * 3 + 1);

        for (int arch = 0; arch < arches; ++arch) {
            int z = arch * 3;
            placed |= placePointedArch(world, random, base, forward, z, halfWidth, height);
            placed |= placeColumn(world, random, base, forward, -halfWidth - 1, z, height + 1, false);
            placed |= placeColumn(world, random, base, forward, halfWidth + 1, z, height + 1, false);
        }
        return placed;
    }

    private boolean generateGate(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        int halfWidth = 4 + variant / 10;
        int height = 7 + variant / 5;
        boolean placed = placeFloor(world, random, base, forward, halfWidth + 2, halfWidth + 2, -2, 2);
        placed |= placePointedArch(world, random, base, forward, 0, halfWidth, height);
        placed |= placeColumn(world, random, base, forward, -halfWidth - 2, 0, height + 3, true);
        placed |= placeColumn(world, random, base, forward, halfWidth + 2, 0, height + 3, true);
        placed |= placeSpire(world, random, base, forward, -halfWidth - 2, 0, height + 4, 2);
        placed |= placeSpire(world, random, base, forward, halfWidth + 2, 0, height + 4, 2);
        return placed;
    }

    private boolean generateBrokenSpire(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int variant) {
        int radius = 2 + variant / 10;
        int height = 10 + variant / 5 + random.nextInt(5);
        boolean placed = placeFloor(world, random, base, forward, radius + 1, radius + 1, -radius - 1, radius + 1);

        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (Math.abs(x) == radius || Math.abs(z) == radius || random.nextInt(5) == 0) {
                    int localHeight = Math.max(2, height - Math.abs(x) - Math.abs(z) - random.nextInt(4));
                    for (int y = 0; y <= localHeight; ++y) {
                        if (random.nextInt(9) != 0) {
                            placed |= placeAirBlock(world, local(base, forward, x, y, z), y > localHeight - 2 ? trim(random) : wall(random));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 12; ++i) {
            int x = BlackHoneyFeatureUtil.signed(random, radius + 4);
            int z = BlackHoneyFeatureUtil.signed(random, radius + 4);
            placed |= placeAirBlock(world, local(base, forward, x, 0, z), random.nextInt(5) == 0 ? trim(random) : wall(random));
        }
        return placed;
    }

    private boolean placePointedArch(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int z, int halfWidth, int height) {
        boolean placed = false;
        for (int y = 0; y <= height; ++y) {
            int inset = Math.max(0, (y - 2) / 2);
            int x = Math.max(0, halfWidth - inset);
            if (x > 1 || y > height - 2) {
                placed |= placeAirBlock(world, local(base, forward, -x, y, z), y >= height - 1 ? trim(random) : wall(random));
                placed |= placeAirBlock(world, local(base, forward, x, y, z), y >= height - 1 ? trim(random) : wall(random));
            }
        }
        placed |= placeAirBlock(world, local(base, forward, 0, height + 1, z), trim(random));
        return placed;
    }

    private boolean placeSpire(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int centerX, int centerZ, int startY, int height) {
        boolean placed = false;
        for (int y = 0; y <= height; ++y) {
            int radius = Math.max(0, (height - y) / 2);
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    if (Math.abs(x) + Math.abs(z) <= radius + 1) {
                        placed |= placeAirBlock(world, local(base, forward, centerX + x, startY + y, centerZ + z), y > height - 2 ? trim(random) : roof(random));
                    }
                }
            }
        }
        return placed;
    }

    private boolean placeColumn(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int x, int z, int height, boolean copperAccent) {
        boolean placed = false;
        for (int y = 0; y <= height; ++y) {
            BlockState state = copperAccent && (y == height || y % 4 == 0) ? trim(random) : wall(random);
            placed |= placeAirBlock(world, local(base, forward, x, y, z), state);
        }
        return placed;
    }

    private boolean placeFloor(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int minX, int maxX, int minZ, int maxZ) {
        boolean placed = false;
        for (int x = -minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (random.nextInt(10) != 0) {
                    placed |= placeSolidBlock(world, local(base, forward, x, -1, z), floor(random));
                }
            }
        }
        return placed;
    }

    private BlockPos local(BlockPos base, Direction forward, int x, int y, int z) {
        Direction right = forward.rotateYClockwise();
        return base.offset(right, x).offset(forward, z).up(y);
    }

    private boolean placeAirBlock(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
            return false;
        }
        world.setBlockState(pos, state, 2);
        return true;
    }

    private boolean placeSolidBlock(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (!BlackHoneyFeatureUtil.inWorld(world, pos)) {
            return false;
        }
        world.setBlockState(pos, state, 2);
        return true;
    }

    private BlockState floor(Random random) {
        int pick = random.nextInt(18);
        if (pick < 5) {
            return Blocks.POLISHED_DEEPSLATE.getDefaultState();
        }
        if (pick < 10) {
            return Blocks.TUFF.getDefaultState();
        }
        if (pick < 14) {
            return Blocks.DEEPSLATE_TILES.getDefaultState();
        }
        if (pick < 17) {
            return Blocks.CALCITE.getDefaultState();
        }
        return Blocks.OXIDIZED_CUT_COPPER.getDefaultState();
    }

    private BlockState wall(Random random) {
        int pick = random.nextInt(24);
        if (pick < 7) {
            return Blocks.DEEPSLATE.getDefaultState();
        }
        if (pick < 13) {
            return Blocks.TUFF.getDefaultState();
        }
        if (pick < 17) {
            return Blocks.POLISHED_DEEPSLATE.getDefaultState();
        }
        if (pick < 20) {
            return Blocks.DEEPSLATE_BRICKS.getDefaultState();
        }
        if (pick < 22) {
            return Blocks.CALCITE.getDefaultState();
        }
        if (pick == 22) {
            return Blocks.OXIDIZED_COPPER.getDefaultState();
        }
        return Blocks.WEATHERED_COPPER.getDefaultState();
    }

    private BlockState trim(Random random) {
        int pick = random.nextInt(10);
        if (pick < 3) {
            return Blocks.OXIDIZED_CUT_COPPER.getDefaultState();
        }
        if (pick < 5) {
            return Blocks.WEATHERED_CUT_COPPER.getDefaultState();
        }
        if (pick < 8) {
            return Blocks.CHISELED_DEEPSLATE.getDefaultState();
        }
        return Blocks.OCHRE_FROGLIGHT.getDefaultState();
    }

    private BlockState roof(Random random) {
        int pick = random.nextInt(12);
        if (pick < 5) {
            return Blocks.DEEPSLATE_TILES.getDefaultState();
        }
        if (pick < 9) {
            return Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState();
        }
        if (pick < 11) {
            return Blocks.TUFF.getDefaultState();
        }
        return Blocks.WEATHERED_CUT_COPPER.getDefaultState();
    }
}
