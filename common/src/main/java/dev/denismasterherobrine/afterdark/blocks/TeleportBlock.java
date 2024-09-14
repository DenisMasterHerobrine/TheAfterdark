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
        super(Block.Settings.copy(net.minecraft.block.Blocks.STONE));
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.getStackInHand(hand).getItem() == AfterdarkRegistry.TELEPORT_CATALYST_ITEM && player.getStackInHand(hand).getCount() > 0) {
                player.getStackInHand(hand).decrement(1);
                // TEMPORARY
                if (world.getServer() != null) {
                    if (player.getWorld() == world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL)) {
                        player.teleport(world.getServer().getWorld(World.OVERWORLD), pos.getX(), pos.getY(), pos.getZ(), null, player.getYaw(), player.getPitch());
                    } else {
                        player.teleport(world.getServer().getWorld(AfterdarkRegistry.AFTERDARK_LEVEL), pos.getX(), pos.getY(), pos.getZ(), null, player.getYaw(), player.getPitch());
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
    }

}