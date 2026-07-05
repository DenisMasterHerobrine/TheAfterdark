package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.PlayerEntityAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityNBTMixin implements PlayerEntityAccess {
    @Unique
    private String the_afterdark$lastWorld;

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
        if (the_afterdark$lastWorld != null) {
            nbt.putString("lastWorld", the_afterdark$lastWorld);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
        the_afterdark$lastWorld = nbt.getString("lastWorld");
    }

    @Override
    public void the_afterdark$setLastWorld(String worldId) {
        this.the_afterdark$lastWorld = worldId;
    }

    @Override
    public String the_afterdark$getLastWorld() {
        return this.the_afterdark$lastWorld;
    }
}