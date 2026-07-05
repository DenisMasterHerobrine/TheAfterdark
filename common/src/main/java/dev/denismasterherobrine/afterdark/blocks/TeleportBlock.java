package dev.denismasterherobrine.afterdark.blocks;

import com.mojang.serialization.MapCodec;
import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.blocks.entity.TeleportBlockEntity;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import dev.denismasterherobrine.afterdark.util.PlayerEntityAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TeleportBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<TeleportBlock> CODEC = simpleCodec(TeleportBlock::new);

    public TeleportBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE));
    }

    public TeleportBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0, 0, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.25, 0.25, 0.25, 0.375, 0.375, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.25, 0.25, 0.625, 0.375, 0.375, 0.75), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.625, 0.25, 0.625, 0.75, 0.375, 0.75), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.625, 0.25, 0.25, 0.75, 0.375, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.625, 0.625, 0.25, 0.75, 0.75, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.75, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.25, 0.625, 0.25, 0.375, 0.75, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.25, 0.625, 0.625, 0.375, 0.75, 0.75), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.625, 0.625, 0.625, 0.75, 0.75, 0.75), BooleanOp.OR);

        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        return makeShape();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeleportBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && world.getServer() != null) {
            if (player.level() == world.getServer().getLevel(AfterdarkRegistry.AFTERDARK_LEVEL) && Config.INSTANCE.canReturnWithoutCatalyst) {
                teleportFromDimension(player);
            } else {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof TeleportBlockEntity teleportBlockEntity) {
                    if (teleportBlockEntity.getRemainingTeleports() > 0 || Config.INSTANCE.TeleportCatalystUses < 1) {
                        PlayerEntityAccess playerAccess = (PlayerEntityAccess) player;
                        if (player.level() == world.getServer().getLevel(AfterdarkRegistry.AFTERDARK_LEVEL)) {
                            if (teleportBlockEntity.getRemainingTeleports() > 0) {
                                teleportBlockEntity.setRemainingTeleports(teleportBlockEntity.getRemainingTeleports() - 1);
                            }
                            teleportFromDimension(player);

                        } else {
                            if (teleportBlockEntity.getRemainingTeleports() > 0) {
                                teleportBlockEntity.setRemainingTeleports(teleportBlockEntity.getRemainingTeleports() - 1);
                            }
                            playerAccess.the_afterdark$setLastWorld(player.level().dimension().location().toString());
                            teleportToDimension(player);
                        }
                    } else if (stack.getItem() == AfterdarkRegistry.TELEPORT_CATALYST_ITEM && stack.getCount() > 0) {
                        stack.shrink(1);
                        teleportBlockEntity.renewTeleports();
                    } else {
                        player.displayClientMessage(Component.translatable("chat.the_afterdark.teleport_missing_catalyst"), false);
                    }
                }
            }

            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void teleportToDimension(Player player) {
        if (player.getServer() != null) {
            BlockPos safePos = getSafeTeleportPos(player.getServer().getLevel(AfterdarkRegistry.AFTERDARK_LEVEL), player.blockPosition(), player);
            player.teleportTo(player.getServer().getLevel(AfterdarkRegistry.AFTERDARK_LEVEL), safePos.getCenter().x(), safePos.getY(), safePos.getCenter().z(), RelativeMovement.unpack(0), player.getYRot(), player.getXRot());
        }
    }

    public void teleportFromDimension(Player player) {
        if (player.getServer() != null) {
            ResourceKey<Level> playerLastWorld;

            if (!Config.INSTANCE.shouldTeleportReturnToSetWorld) {
                String lastWorld = ((PlayerEntityAccess) player).the_afterdark$getLastWorld();
                if (lastWorld == null) {
                    playerLastWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(Config.INSTANCE.returnSetWorld));
                } else {
                    playerLastWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(lastWorld));
                    if (playerLastWorld == null) {
                        playerLastWorld = Level.OVERWORLD;
                    }
                }
            } else {
                playerLastWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(Config.INSTANCE.returnSetWorld));
            }
            BlockPos safePos = getSafeTeleportPos(player.getServer().getLevel(playerLastWorld), player.blockPosition(), player);
            player.teleportTo(player.getServer().getLevel(playerLastWorld), safePos.getCenter().x(), safePos.getY(), safePos.getCenter().z(), RelativeMovement.unpack(0), player.getYRot(), player.getXRot());
        }
    }

    public boolean isTeleportSafe(Level world, BlockPos pos, Player player) {
        if (world.getBlockState(pos.below()).isRedstoneConductor(world, pos.below())) {
            for (int i = 0; i < player.getBbHeight(); i++) {
                if (!world.getBlockState(pos.above(i)).isAir()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public BlockPos getSafeTeleportPos(Level world, BlockPos pos, Player player) {
        int radius = Config.INSTANCE.SafeTeleportCheckRadius;

        if (isTeleportSafe(world, pos, player)) {
            return pos;
        } else {
            for (int r = 0; r <= radius; r++) {
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        for (int z = -r; z <= r; z++) {
                            BlockPos checkPos = pos.offset(x, y, z);
                            if (isTeleportSafe(world, checkPos, player)) {
                                return checkPos;
                            }
                        }
                    }
                }
            }
            for (int y = world.getMinBuildHeight(); y <= world.getHeight(); y++) {
                BlockPos checkPos = pos.atY(y);
                if (isTeleportSafe(world, checkPos, player)) {
                    return checkPos;
                }
            }
            for (int i = 0; i < world.getHeight() + Math.abs(world.getMinBuildHeight()); i++) {
                int y = pos.getY() + i;
                if (y <= world.getHeight()) {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && z == 0) {
                                continue;
                            }

                            BlockPos checkPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                            if (isTeleportSafe(world, checkPos, player)) {
                                return checkPos;
                            }
                        }
                    }
                }
                y = pos.getY() - i;
                if (pos.getY() - i >= world.getMinBuildHeight()) {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && z == 0) {
                                continue;
                            }

                            BlockPos checkPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                            if (isTeleportSafe(world, checkPos, player)) {
                                return checkPos;
                            }
                        }
                    }
                }
            }

            return pos;
        }
    }
}
