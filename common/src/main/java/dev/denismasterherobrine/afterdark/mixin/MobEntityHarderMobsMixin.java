package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.hardermobs.HarderMobsManager;
import dev.denismasterherobrine.afterdark.hardermobs.HarderMobEntityAccess;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityHarderMobsMixin implements HarderMobEntityAccess {
    private boolean the_afterdark$harderMob;
    private String the_afterdark$harderMobTier = "";
    private String the_afterdark$harderMobRole = "";

    @Inject(method = "initialize", at = @At("RETURN"), cancellable = true)
    private void the_afterdark$applyHarderMobs(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        cir.setReturnValue(HarderMobsManager.applyAfterInitialize((MobEntity) (Object) this, world, difficulty, spawnReason, cir.getReturnValue()));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void the_afterdark$tickHarderMobs(CallbackInfo ci) {
        HarderMobsManager.tick((MobEntity) (Object) this);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void the_afterdark$writeHarderMobsData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("the_afterdark:harder_mob", the_afterdark$harderMob);
        if (the_afterdark$harderMobTier != null && !the_afterdark$harderMobTier.isBlank()) {
            nbt.putString("the_afterdark:harder_mob_tier", the_afterdark$harderMobTier);
        }
        if (the_afterdark$harderMobRole != null && !the_afterdark$harderMobRole.isBlank()) {
            nbt.putString("the_afterdark:harder_mob_role", the_afterdark$harderMobRole);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void the_afterdark$readHarderMobsData(NbtCompound nbt, CallbackInfo ci) {
        the_afterdark$harderMob = nbt.getBoolean("the_afterdark:harder_mob");
        the_afterdark$harderMobTier = nbt.getString("the_afterdark:harder_mob_tier");
        the_afterdark$harderMobRole = nbt.getString("the_afterdark:harder_mob_role");
    }

    @Override
    public boolean the_afterdark$isHarderMob() {
        return the_afterdark$harderMob;
    }

    @Override
    public void the_afterdark$setHarderMob(boolean harderMob) {
        the_afterdark$harderMob = harderMob;
    }

    @Override
    public String the_afterdark$getHarderMobTier() {
        return the_afterdark$harderMobTier;
    }

    @Override
    public void the_afterdark$setHarderMobTier(String tier) {
        the_afterdark$harderMobTier = tier;
    }

    @Override
    public String the_afterdark$getHarderMobRole() {
        return the_afterdark$harderMobRole;
    }

    @Override
    public void the_afterdark$setHarderMobRole(String role) {
        the_afterdark$harderMobRole = role;
    }
}
