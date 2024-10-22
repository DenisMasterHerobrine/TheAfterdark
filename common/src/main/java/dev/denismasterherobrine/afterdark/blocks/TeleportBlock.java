package dev.denismasterherobrine.afterdark.blocks;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TeleportBlock extends BlockWithEntity implements BlockEntityProvider {
    public TeleportBlock() {
        super(Block.Settings.copy(Blocks.DEEPSLATE));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape makeShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0, 0, 1, 0.25, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.375, 0.375, 0.375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.25, 0.625, 0.375, 0.375, 0.75), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.625, 0.25, 0.625, 0.75, 0.375, 0.75), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.625, 0.25, 0.25, 0.75, 0.375, 0.375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.625, 0.625, 0.25, 0.75, 0.75, 0.375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.75, 0, 1, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.625, 0.25, 0.375, 0.75, 0.375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.625, 0.625, 0.375, 0.75, 0.75), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.625, 0.625, 0.625, 0.75, 0.75, 0.75), BooleanBiFunction.OR);

        return shape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && world.getServer() != null) {
            if (player.getWorld() == world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL) && Config.INSTANCE.canReturnWithoutCatalyst) {
                BlockPos safePos = getSafeTeleportPos(world.getServer().getWorld(World.OVERWORLD), player.getBlockPos(), player);
                player.teleport(world.getServer().getWorld(World.OVERWORLD), safePos.toCenterPos().getX(), safePos.getY(), safePos.toCenterPos().getZ(), null, player.getYaw(), player.getPitch());
            } else {
                if (player.getStackInHand(hand).getItem() == AfterdarkRegistry.TELEPORT_CATALYST_ITEM && player.getStackInHand(hand).getCount() > 0) {
                    player.getStackInHand(hand).decrement(1);
                    if (player.getWorld() == world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL)) {
                        BlockPos safePos = getSafeTeleportPos(world.getServer().getWorld(World.OVERWORLD), player.getBlockPos(), player);
                        player.teleport(world.getServer().getWorld(World.OVERWORLD), safePos.toCenterPos().getX(), safePos.getY(), safePos.toCenterPos().getZ(), null, player.getYaw(), player.getPitch());
                    } else {
                        BlockPos safePos = getSafeTeleportPos(world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL), player.getBlockPos(), player);
                        player.teleport(world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL), safePos.toCenterPos().getX(), safePos.getY(), safePos.toCenterPos().getZ(), null, player.getYaw(), player.getPitch());
                    }
                } else {
                    player.sendMessage(Text.translatable("chat.the_afterdark.teleport_missing_catalyst"), false);
                }
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public boolean isTeleportSafe(World world, BlockPos pos, PlayerEntity player) {
        if (world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
            for (int i = 0; i < player.getHeight(); i++) {
                if (world.getBlockState(pos.up(i)).isSolidBlock(world, pos.up(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public BlockPos getSafeTeleportPos(World world, BlockPos pos, PlayerEntity player) {
        int radius = Config.INSTANCE.SafeTeleportCheckRadius;

        if (isTeleportSafe(world, pos, player)) {
            return pos;
        } else {
            for (int r = 0; r <= radius; r++) {
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        for (int z = -r; z <= r; z++) {
                            BlockPos checkPos = pos.add(x, y, z);
                            if (isTeleportSafe(world, checkPos, player)) {
                                return checkPos;
                            }
                        }
                    }
                }
            }
            for (int y = world.getBottomY(); y <= world.getTopY() - pos.getY(); y++) {
                BlockPos checkPos = pos.add(0, y, 0);
                if (isTeleportSafe(world, checkPos, player)) {
                    return checkPos;
                }
            }
            for (int y = world.getBottomY(); y <= world.getTopY() - pos.getY(); y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && z == 0) {
                            continue;
                        }
                        BlockPos checkPos = pos.add(x, y, z);
                        if (isTeleportSafe(world, checkPos, player)) {
                            return checkPos;
                        }
                    }
                }
            }


            return pos;
        }
    }
}