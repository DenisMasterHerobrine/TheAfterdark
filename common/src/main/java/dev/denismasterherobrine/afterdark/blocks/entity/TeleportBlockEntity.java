package dev.denismasterherobrine.afterdark.blocks.entity;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportBlockEntity extends BlockEntity {
    public static String TELEPORT_BE_ID = "teleport_block_entity";
    public int MAX_TELEPORTS = Config.INSTANCE.TeleportCatalystUses;
    private int remainingTeleports = 0;

    public TeleportBlockEntity(BlockPos pos, BlockState state) {
        super(Registries.BLOCK_ENTITY_TYPE.get(Identifier.of(TheAfterdark.MOD_ID, TELEPORT_BE_ID)), pos, state);
    }

    public int getRemainingTeleports() {
        return remainingTeleports;
    }

    public void setRemainingTeleports(int remainingTeleports) {
        this.remainingTeleports = remainingTeleports;
        this.markDirty();
    }

    public void renewTeleports() {
        remainingTeleports = MAX_TELEPORTS;
        this.markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        remainingTeleports = nbt.getInt("RemainingTeleports");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("RemainingTeleports", remainingTeleports);
    }
}
