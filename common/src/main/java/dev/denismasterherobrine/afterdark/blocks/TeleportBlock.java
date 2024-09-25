package dev.denismasterherobrine.afterdark.blocks;

import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TeleportBlock extends BlockWithEntity implements BlockEntityProvider {
    public TeleportBlock() {
        super(Block.Settings.copy(net.minecraft.block.Blocks.STONE).solid());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.getStackInHand(hand).getItem() == AfterdarkRegistry.TELEPORT_CATALYST_ITEM && player.getStackInHand(hand).getCount() > 0) {
                player.getStackInHand(hand).decrement(1);
                if (world.getServer() != null) {
                    if (player.getWorld() == world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL)) {
                        BlockPos safePos = getSafeTeleportPos(world.getServer().getWorld(World.OVERWORLD), player.getBlockPos(), player);
                        player.teleport(world.getServer().getWorld(World.OVERWORLD), safePos.toCenterPos().getX(), safePos.getY(), safePos.toCenterPos().getZ(), null, player.getYaw(), player.getPitch());
                    } else {
                        BlockPos safePos = getSafeTeleportPos(world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL), player.getBlockPos(), player);
                        player.teleport(world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL), safePos.toCenterPos().getX(), safePos.getY(), safePos.toCenterPos().getZ(), null, player.getYaw(), player.getPitch());
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
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
        //TODO: replace with config or replace method with more effective algorithm
        int radius = 20;

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
        }
        return pos;
    }
}