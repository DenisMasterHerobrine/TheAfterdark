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

public class DeepAfterdarkStructureFeature extends Feature<DefaultFeatureConfig> {
    private static final Identifier DEEP_AFTERDARK_CACHE_LOOT_TABLE = new Identifier(TheAfterdark.MOD_ID, "chests/deep_afterdark_cache");
    private static final Identifier DEEP_AFTERDARK_EPIC_CACHE_LOOT_TABLE = new Identifier(TheAfterdark.MOD_ID, "chests/deep_afterdark_epic_cache");

    public DeepAfterdarkStructureFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos base = BlackHoneyFeatureUtil.findFloor(world, context.getOrigin(), 12, 30);
        if (base == null || !hasRoom(world, base, 5, 11)) {
            return false;
        }

        Direction forward = BlackHoneyFeatureUtil.randomHorizontal(random);
        boolean placed = switch (random.nextInt(6)) {
            case 0 -> generateObelisk(world, random, base, forward);
            case 1 -> generateShrine(world, random, base, forward);
            case 2 -> generateEchoWell(world, random, base, forward);
            case 3 -> generateBrokenBridge(world, random, base, forward);
            case 4 -> generateSculkHeart(world, random, base, forward);
            default -> generateBoneNest(world, random, base, forward);
        };
        if (placed) {
            placed |= tryPlaceEpicLootChest(world, random, base, forward);
        }
        return placed;
    }

    private boolean hasRoom(StructureWorldAccess world, BlockPos base, int radius, int height) {
        if (!BlackHoneyFeatureUtil.canReplace(world, base) || !BlackHoneyFeatureUtil.isSolid(world, base.down())) {
            return false;
        }
        for (int y = 1; y <= height; y += 3) {
            if (!BlackHoneyFeatureUtil.canReplace(world, base.up(y))) {
                return false;
            }
        }
        return BlackHoneyFeatureUtil.canReplace(world, base.add(radius, 1, 0))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(-radius, 1, 0))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(0, 1, radius))
                && BlackHoneyFeatureUtil.canReplace(world, base.add(0, 1, -radius));
    }

    private boolean generateObelisk(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = placeGroundPatch(world, random, base, forward, 5);
        int height = 11 + random.nextInt(7);

        for (int y = 0; y <= height; ++y) {
            int radius = y < 3 ? 1 : y == height ? 0 : random.nextInt(6) == 0 ? 1 : 0;
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    if (Math.abs(x) + Math.abs(z) <= radius + 1) {
                        placed |= placeAirBlock(world, local(base, forward, x, y, z), y == height ? dangerousAccent(random) : obeliskBlock(random));
                    }
                }
            }
            if (y > 2 && y < height - 1 && y % 4 == 0) {
                placed |= placeAirBlock(world, local(base, forward, 2, y, 0), Blocks.SCULK.getDefaultState());
                placed |= placeAirBlock(world, local(base, forward, -2, y, 0), Blocks.SCULK.getDefaultState());
                placed |= placeAirBlock(world, local(base, forward, 0, y, 2), Blocks.SCULK.getDefaultState());
                placed |= placeAirBlock(world, local(base, forward, 0, y, -2), Blocks.SCULK.getDefaultState());
            }
        }

        for (int[] marker : new int[][]{{3, 0}, {-3, 0}, {0, 3}, {0, -3}}) {
            placed |= placeFloorDanger(world, local(base, forward, marker[0], 0, marker[1]), random.nextBoolean()
                    ? Blocks.SCULK_SENSOR.getDefaultState()
                    : Blocks.CYAN_CANDLE.getDefaultState());
        }
        placed |= placeFloorDanger(world, local(base, forward, 0, 0, 0), Blocks.SCULK_SHRIEKER.getDefaultState());
        return placed;
    }

    private boolean generateShrine(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = false;
        for (int x = -4; x <= 4; ++x) {
            for (int z = -3; z <= 5; ++z) {
                if (random.nextInt(9) != 0) {
                    placed |= placeSolidBlock(world, local(base, forward, x, -1, z), floor(random));
                }
            }
        }

        for (int[] column : new int[][]{{-4, -3}, {4, -3}, {-4, 5}, {4, 5}}) {
            int height = 3 + random.nextInt(3);
            for (int y = 0; y <= height; ++y) {
                placed |= placeAirBlock(world, local(base, forward, column[0], y, column[1]), y == height ? accent(random) : ruinBlock(random));
            }
        }

        for (int x = -2; x <= 2; ++x) {
            placed |= placeAirBlock(world, local(base, forward, x, 0, 4), ruinBlock(random));
        }
        placed |= placeAirBlock(world, local(base, forward, 0, 1, 4), Blocks.SCULK_CATALYST.getDefaultState());
        placed |= placeAirBlock(world, local(base, forward, 0, 0, 2), Blocks.CHISELED_DEEPSLATE.getDefaultState());
        placed |= placeAirBlock(world, local(base, forward, 0, 1, 2), Blocks.SCULK.getDefaultState());
        placed |= placeFloorDanger(world, local(base, forward, -2, 0, 1), Blocks.SCULK_SENSOR.getDefaultState());
        placed |= placeFloorDanger(world, local(base, forward, 2, 0, 1), Blocks.SCULK_SENSOR.getDefaultState());

        if (random.nextFloat() < 0.45F) {
            placed |= placeLootChest(world, random, local(base, forward, 0, 0, 5), forward, DEEP_AFTERDARK_CACHE_LOOT_TABLE);
        }
        return placed;
    }

    private boolean generateEchoWell(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = placeGroundPatch(world, random, base, forward, 5);
        int depth = 8 + random.nextInt(10);

        for (int y = 0; y >= -depth; --y) {
            for (int x = -2; x <= 2; ++x) {
                for (int z = -2; z <= 2; ++z) {
                    int distance = x * x + z * z;
                    BlockPos pos = local(base, forward, x, y, z);
                    if (distance <= 1) {
                        placed |= placeSolidBlock(world, pos, y >= -3 ? Blocks.WATER.getDefaultState() : Blocks.CAVE_AIR.getDefaultState());
                    } else if (distance <= 5 && random.nextInt(4) != 0) {
                        placed |= placeSolidBlock(world, pos, random.nextInt(5) == 0 ? Blocks.SCULK.getDefaultState() : ruinBlock(random));
                    }
                }
            }
        }

        for (int[] rim : new int[][]{{-3, 0}, {3, 0}, {0, -3}, {0, 3}, {-2, -2}, {-2, 2}, {2, -2}, {2, 2}}) {
            placed |= placeAirBlock(world, local(base, forward, rim[0], 0, rim[1]), ruinBlock(random));
        }
        placed |= placeFloorDanger(world, local(base, forward, 3, 0, 1), Blocks.SCULK_SENSOR.getDefaultState());
        if (random.nextFloat() < 0.25F) {
            placed |= placeLootChest(world, random, local(base, forward, -3, 0, -1), forward, DEEP_AFTERDARK_CACHE_LOOT_TABLE);
        }
        return placed;
    }

    private boolean generateBrokenBridge(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = false;
        int gapStart = -1 + random.nextInt(4);
        int gapEnd = gapStart + 1 + random.nextInt(2);
        for (int z = -7; z <= 9; ++z) {
            boolean gap = z >= gapStart && z <= gapEnd;
            for (int x = -2; x <= 2; ++x) {
                if (!gap || Math.abs(x) == 2 && random.nextBoolean()) {
                    placed |= placeAirBlock(world, local(base, forward, x, 0, z), random.nextInt(5) == 0 ? Blocks.SCULK.getDefaultState() : floor(random));
                }
                if (Math.abs(x) == 2 && random.nextInt(3) == 0) {
                    placed |= placeAirBlock(world, local(base, forward, x, 1, z), ruinBlock(random));
                }
            }
            if (z % 4 == 0) {
                placed |= placeSupport(world, random, base, forward, -2, z, 4);
                placed |= placeSupport(world, random, base, forward, 2, z, 4);
            }
        }
        placed |= placeFloorDanger(world, local(base, forward, -1, 1, gapStart - 1), Blocks.SCULK_SENSOR.getDefaultState());
        placed |= placeFloorDanger(world, local(base, forward, 1, 1, gapEnd + 1), Blocks.CYAN_CANDLE.getDefaultState());
        return placed;
    }

    private boolean generateSculkHeart(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = placeGroundPatch(world, random, base, forward, 6);
        for (int x = -3; x <= 3; ++x) {
            for (int y = 0; y <= 4; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    double distance = Math.sqrt(x * x + z * z + (y - 2) * (y - 2) * 1.35D);
                    if (distance <= 2.45D + random.nextFloat() * 0.35D) {
                        placed |= placeAirBlock(world, local(base, forward, x, y, z), heartBlock(random));
                    }
                }
            }
        }

        for (int i = 0; i < 8; ++i) {
            Direction direction = BlackHoneyFeatureUtil.HORIZONTAL[i % BlackHoneyFeatureUtil.HORIZONTAL.length];
            int length = 3 + random.nextInt(5);
            BlockPos cursor = base.up(random.nextInt(2));
            for (int step = 0; step < length; ++step) {
                cursor = cursor.offset(direction).add(0, random.nextInt(3) == 0 ? 1 : 0, 0);
                placed |= placeAirBlock(world, cursor, random.nextInt(4) == 0 ? Blocks.CRYING_OBSIDIAN.getDefaultState() : Blocks.SCULK.getDefaultState());
            }
        }

        placed |= placeFloorDanger(world, local(base, forward, 0, 0, -4), Blocks.SCULK_SHRIEKER.getDefaultState());
        placed |= placeFloorDanger(world, local(base, forward, 4, 0, 0), Blocks.SCULK_SENSOR.getDefaultState());
        placed |= placeFloorDanger(world, local(base, forward, -4, 0, 0), Blocks.SCULK_SENSOR.getDefaultState());
        return placed;
    }

    private boolean generateBoneNest(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        boolean placed = placeGroundPatch(world, random, base, forward, 4);
        for (int rib = -3; rib <= 3; ++rib) {
            int height = 2 + random.nextInt(4);
            for (int y = 0; y <= height; ++y) {
                int spread = Math.max(0, 3 - y);
                placed |= placeAirBlock(world, local(base, forward, rib, y, -spread), boneOrSculk(random));
                placed |= placeAirBlock(world, local(base, forward, rib, y, spread), boneOrSculk(random));
            }
        }
        for (int i = 0; i < 16; ++i) {
            int x = BlackHoneyFeatureUtil.signed(random, 4);
            int z = BlackHoneyFeatureUtil.signed(random, 4);
            placed |= placeFloorDanger(world, local(base, forward, x, 0, z), random.nextInt(5) == 0 ? Blocks.COBWEB.getDefaultState() : boneOrSculk(random));
        }
        placed |= placeFloorDanger(world, local(base, forward, 0, 0, 0), Blocks.SCULK_SHRIEKER.getDefaultState());
        if (random.nextFloat() < 0.3F) {
            placed |= placeLootChest(world, random, local(base, forward, 0, 0, 3), forward, DEEP_AFTERDARK_CACHE_LOOT_TABLE);
        }
        return placed;
    }

    private boolean tryPlaceEpicLootChest(StructureWorldAccess world, Random random, BlockPos base, Direction forward) {
        if (random.nextFloat() >= 0.15F) {
            return false;
        }

        int[][] candidates = new int[][]{
                {0, 0, -5}, {0, 0, 6}, {-5, 0, 0}, {5, 0, 0},
                {-4, 0, -4}, {4, 0, -4}, {-4, 0, 4}, {4, 0, 4},
                {-2, 1, 3}, {2, 1, 3}, {-3, 1, -2}, {3, 1, -2}
        };
        int start = random.nextInt(candidates.length);
        for (int i = 0; i < candidates.length; ++i) {
            int[] candidate = candidates[(start + i) % candidates.length];
            if (placeLootChest(world, random, local(base, forward, candidate[0], candidate[1], candidate[2]), forward, DEEP_AFTERDARK_EPIC_CACHE_LOOT_TABLE)) {
                return true;
            }
        }
        return false;
    }

    private boolean placeGroundPatch(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int radius) {
        boolean placed = false;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z <= radius * radius + random.nextInt(4)) {
                    placed |= placeSolidBlock(world, local(base, forward, x, -1, z), random.nextInt(5) == 0 ? Blocks.SCULK.getDefaultState() : floor(random));
                }
            }
        }
        return placed;
    }

    private boolean placeSupport(StructureWorldAccess world, Random random, BlockPos base, Direction forward, int x, int z, int depth) {
        boolean placed = false;
        for (int y = -1; y >= -depth; --y) {
            BlockPos pos = local(base, forward, x, y, z);
            if (!BlackHoneyFeatureUtil.canReplace(world, pos)) {
                break;
            }
            placed |= placeSolidBlock(world, pos, random.nextInt(4) == 0 ? Blocks.CHAIN.getDefaultState() : ruinBlock(random));
        }
        return placed;
    }

    private boolean placeLootChest(StructureWorldAccess world, Random random, BlockPos pos, Direction forward, Identifier lootTable) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos) || !BlackHoneyFeatureUtil.isSolid(world, pos.down())) {
            return false;
        }

        world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, forward.getOpposite()), 2);
        if (world.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(lootTable, random.nextLong());
        }
        return true;
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

    private boolean placeFloorDanger(StructureWorldAccess world, BlockPos pos, BlockState state) {
        if (!BlackHoneyFeatureUtil.canReplace(world, pos) || !BlackHoneyFeatureUtil.isSolid(world, pos.down())) {
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
            return Blocks.DEEPSLATE_TILES.getDefaultState();
        }
        if (pick < 9) {
            return Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState();
        }
        if (pick < 13) {
            return Blocks.POLISHED_DEEPSLATE.getDefaultState();
        }
        if (pick < 16) {
            return Blocks.TUFF.getDefaultState();
        }
        return Blocks.CRYING_OBSIDIAN.getDefaultState();
    }

    private BlockState ruinBlock(Random random) {
        int pick = random.nextInt(20);
        if (pick < 5) {
            return Blocks.DEEPSLATE.getDefaultState();
        }
        if (pick < 9) {
            return Blocks.COBBLED_DEEPSLATE.getDefaultState();
        }
        if (pick < 13) {
            return Blocks.POLISHED_DEEPSLATE.getDefaultState();
        }
        if (pick < 16) {
            return Blocks.CHISELED_DEEPSLATE.getDefaultState();
        }
        if (pick < 18) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        }
        return Blocks.SCULK.getDefaultState();
    }

    private BlockState obeliskBlock(Random random) {
        int pick = random.nextInt(12);
        if (pick < 5) {
            return Blocks.REINFORCED_DEEPSLATE.getDefaultState();
        }
        if (pick < 8) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        }
        if (pick < 10) {
            return Blocks.CHISELED_DEEPSLATE.getDefaultState();
        }
        return Blocks.SCULK.getDefaultState();
    }

    private BlockState accent(Random random) {
        return random.nextBoolean() ? Blocks.CRYING_OBSIDIAN.getDefaultState() : Blocks.SCULK.getDefaultState();
    }

    private BlockState dangerousAccent(Random random) {
        int pick = random.nextInt(4);
        if (pick == 0) {
            return Blocks.SCULK_CATALYST.getDefaultState();
        }
        if (pick == 1) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        }
        return Blocks.SCULK.getDefaultState();
    }

    private BlockState heartBlock(Random random) {
        int pick = random.nextInt(10);
        if (pick < 5) {
            return Blocks.SCULK.getDefaultState();
        }
        if (pick < 7) {
            return Blocks.SCULK_CATALYST.getDefaultState();
        }
        if (pick < 9) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        }
        return Blocks.SOUL_LANTERN.getDefaultState();
    }

    private BlockState boneOrSculk(Random random) {
        int pick = random.nextInt(8);
        if (pick < 4) {
            return Blocks.BONE_BLOCK.getDefaultState();
        }
        if (pick < 6) {
            return Blocks.SCULK.getDefaultState();
        }
        return ruinBlock(random);
    }
}
