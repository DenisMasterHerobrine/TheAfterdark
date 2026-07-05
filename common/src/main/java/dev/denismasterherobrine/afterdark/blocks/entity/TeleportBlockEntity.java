package dev.denismasterherobrine.afterdark.blocks.entity;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TeleportBlockEntity extends BlockEntity {
    public static String TELEPORT_BE_ID = "teleport_block_entity";
    public int MAX_TELEPORTS = Config.INSTANCE.TeleportCatalystUses;
    private int remainingTeleports = 0;

    public TeleportBlockEntity(BlockPos pos, BlockState state) {
        super(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.tryBuild(TheAfterdark.MOD_ID, TELEPORT_BE_ID)), pos, state);
    }

    public int getRemainingTeleports() {
        return remainingTeleports;
    }

    public void setRemainingTeleports(int remainingTeleports) {
        this.remainingTeleports = remainingTeleports;
        this.setChanged();
    }

    public void renewTeleports() {
        remainingTeleports = MAX_TELEPORTS;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        remainingTeleports = nbt.getInt("RemainingTeleports");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("RemainingTeleports", remainingTeleports);
    }
}
