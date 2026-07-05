package dev.denismasterherobrine.afterdark.mixin;

import dev.denismasterherobrine.afterdark.hardermobs.HarderMobsManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import dev.denismasterherobrine.afterdark.hardermobs.HarderMobEntityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityHarderMobsMixin implements HarderMobEntityAccess {
    private boolean the_afterdark$harderMob;
    private String the_afterdark$harderMobTier = "";
    private String the_afterdark$harderMobRole = "";

    @Inject(method = "finalizeSpawn", at = @At("RETURN"), cancellable = true)
    private void the_afterdark$applyHarderMobs(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt, CallbackInfoReturnable<SpawnGroupData> cir) {
        cir.setReturnValue(HarderMobsManager.applyAfterInitialize((Mob) (Object) this, world, difficulty, spawnReason, cir.getReturnValue()));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void the_afterdark$tickHarderMobs(CallbackInfo ci) {
        HarderMobsManager.tick((Mob) (Object) this);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void the_afterdark$writeHarderMobsData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("the_afterdark:harder_mob", the_afterdark$harderMob);
        if (the_afterdark$harderMobTier != null && !the_afterdark$harderMobTier.isBlank()) {
            nbt.putString("the_afterdark:harder_mob_tier", the_afterdark$harderMobTier);
        }
        if (the_afterdark$harderMobRole != null && !the_afterdark$harderMobRole.isBlank()) {
            nbt.putString("the_afterdark:harder_mob_role", the_afterdark$harderMobRole);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void the_afterdark$readHarderMobsData(CompoundTag nbt, CallbackInfo ci) {
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
