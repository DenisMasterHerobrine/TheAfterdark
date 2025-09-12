package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.util.PlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityNBTMixin implements PlayerEntityAccess {
    @Unique
    private String the_afterdark$lastWorld;

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (the_afterdark$lastWorld != null) {
            nbt.putString("lastWorld", the_afterdark$lastWorld);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        the_afterdark$lastWorld = nbt.getString("lastWorld").isPresent() ? nbt.getString("lastWorld").get() : "minecraft:overworld";
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