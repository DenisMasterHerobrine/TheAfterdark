package dev.denismasterherobrine.afterdark.events;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public enum AfterdarkCaveEventType {
    WALL_WHISPERS(35, 20 * 45, SoundEvents.AMBIENT_CAVE.value()),
    DEPTH_HUNGER(20, 20 * 60, SoundEvents.WARDEN_HEARTBEAT),
    DEAD_LIGHT(28, 20 * 35, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value()),
    STRANGER_FOOTSTEPS(28, 20 * 40, SoundEvents.ZOMBIE_STEP),
    STONE_HEARTBEAT(22, 20 * 55, SoundEvents.WARDEN_HEARTBEAT),
    MEMORY_DROP(16, 20 * 25, SoundEvents.ENDERMAN_TELEPORT),
    BLIND_SWARM(18, 20 * 45, SoundEvents.BAT_AMBIENT),
    CAVE_CHILL(20, 20 * 60, SoundEvents.GLASS_BREAK),
    DEEP_RAGE(16, 20 * 60, SoundEvents.WARDEN_ANGRY),
    RIFT_CALL(20, 20 * 60, SoundEvents.END_PORTAL_SPAWN),
    BONE_DUST(18, 20 * 55, SoundEvents.WITHER_SKELETON_AMBIENT),
    ORE_SONG(22, 20 * 70, SoundEvents.NOTE_BLOCK_CHIME.value()),
    BLACK_TIDE(24, 20 * 70, SoundEvents.SCULK_SHRIEKER_SHRIEK),
    STONE_SLEEP(18, 20 * 55, SoundEvents.WARDEN_SNIFF),
    SCRAPING_BELOW(20, 20 * 45, SoundEvents.STONE_BREAK),
    ASHEN_BREATH(18, 20 * 45, SoundEvents.FIRE_EXTINGUISH),
    UNSEEN_WATCHER(18, 20 * 55, SoundEvents.ENDERMAN_STARE),
    MIRROR_ECHO(20, 20 * 60, SoundEvents.NOTE_BLOCK_BELL.value()),
    STONE_CRACK(20, 20 * 50, SoundEvents.ANVIL_LAND),
    SILENCE_AFTER_SCREAM(16, 20 * 45, SoundEvents.WARDEN_ROAR),
    TORCH_CURSE(18, 20 * 60, SoundEvents.SCULK_CLICKING),
    FALSE_SAFETY(14, 20 * 70, SoundEvents.BEACON_AMBIENT),
    MOTHER_MOUNTAIN_HUM(18, 20 * 70, SoundEvents.WARDEN_SONIC_CHARGE),
    DEPTH_SEAL(12, 20 * 80, SoundEvents.RESPAWN_ANCHOR_CHARGE),
    TRACE_LEFT_BEHIND(18, 20 * 65, SoundEvents.SCULK_CATALYST_BLOOM);

    private final int weight;
    private final int durationTicks;
    private final SoundEvent omenSound;

    AfterdarkCaveEventType(int weight, int durationTicks, SoundEvent omenSound) {
        this.weight = weight;
        this.durationTicks = durationTicks;
        this.omenSound = omenSound;
    }

    public int getWeight() {
        return weight;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public String translationKey(String suffix) {
        return "event.the_afterdark." + name().toLowerCase(Locale.ROOT) + "." + suffix;
    }

    public void begin(ServerPlayer player, ServerLevel world) {
        player.displayClientMessage(Component.translatable(translationKey("start")), true);
        play(player, world, omenSound, 0.9F, 0.55F + randomFloat() * 0.35F);
        pulse(world, player, 24, 0.08D);

        switch (this) {
            case DEPTH_HUNGER -> player.addEffect(effect(MobEffects.HUNGER, 160, 0));
            case DEAD_LIGHT -> player.addEffect(effect(MobEffects.DARKNESS, 120, 0));
            case MEMORY_DROP -> {
                player.addEffect(effect(MobEffects.CONFUSION, 220, 0));
                player.addEffect(effect(MobEffects.DARKNESS, 100, 0));
                teleportSafely(player, world, 4);
            }
            case BLIND_SWARM -> spawnBats(world, player, 7);
            case CAVE_CHILL -> {
                player.addEffect(effect(MobEffects.MOVEMENT_SLOWDOWN, 180, 0));
                player.addEffect(effect(MobEffects.DIG_SLOWDOWN, 180, 0));
            }
            case DEEP_RAGE -> empowerHostiles(world, player, 24, MobEffects.DAMAGE_BOOST, 600, 0);
            case BONE_DUST -> player.addEffect(effect(MobEffects.WEAKNESS, 260, 0));
            case ORE_SONG -> player.addEffect(effect(MobEffects.LUCK, durationTicks, 0));
            case STONE_SLEEP -> player.addEffect(effect(MobEffects.DIG_SLOWDOWN, 160, 0));
            case ASHEN_BREATH -> player.addEffect(effect(MobEffects.POISON, 80, 0));
            case TORCH_CURSE -> player.addEffect(effect(MobEffects.UNLUCK, 240, 0));
            case FALSE_SAFETY -> player.addEffect(effect(MobEffects.REGENERATION, 260, 0));
            case MOTHER_MOUNTAIN_HUM -> player.addEffect(effect(MobEffects.DIG_SPEED, durationTicks, 1));
            case DEPTH_SEAL -> player.addEffect(effect(MobEffects.WEAKNESS, 240, 0));
        }
    }

    public void tick(ServerPlayer player, ServerLevel world, AfterdarkPlayerEventState state) {
        int age = state.getActiveAge();

        switch (this) {
            case WALL_WHISPERS -> {
                every(age, 60, () -> play(player, world, SoundEvents.AMBIENT_CAVE.value(), 0.55F, randomPitch(player, 0.45F, 1.2F)));
                every(age, 80, () -> player.addEffect(effect(MobEffects.DARKNESS, 80, 0)));
                every(age, 25, () -> world.sendParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1.1D, player.getZ(), 3, 3.5D, 1.3D, 3.5D, 0.01D));
            }
            case DEPTH_HUNGER -> {
                every(age, 100, () -> player.addEffect(effect(MobEffects.HUNGER, 140, 0)));
                every(age, 140, () -> player.addEffect(effect(MobEffects.DIG_SPEED, 100, 0)));
            }
            case DEAD_LIGHT -> {
                every(age, 70, () -> player.addEffect(effect(MobEffects.BLINDNESS, 45, 0)));
                every(age, 30, () -> world.sendParticles(ParticleTypes.SMOKE, player.getX(), player.getY() + 1.0D, player.getZ(), 7, 4.0D, 1.2D, 4.0D, 0.01D));
            }
            case STRANGER_FOOTSTEPS -> {
                every(age, 45, () -> playBehind(player, world, SoundEvents.ZOMBIE_STEP, 0.8F, 0.55F));
                if (age == 20 * 22) {
                    spawnZombie(world, player, true);
                }
            }
            case STONE_HEARTBEAT -> every(age, 55, () -> {
                play(player, world, SoundEvents.WARDEN_HEARTBEAT, 1.8F, 0.55F);
                player.knockback(0.35D, randomDouble() - 0.5D, randomDouble() - 0.5D);
                empowerHostiles(world, player, 18, MobEffects.DAMAGE_BOOST, 120, 0);
            });
            case MEMORY_DROP -> every(age, 90, () -> {
                player.addEffect(effect(MobEffects.CONFUSION, 120, 0));
                play(player, world, SoundEvents.ENDERMAN_TELEPORT, 0.7F, 0.4F);
                teleportSafely(player, world, 3);
            });
            case BLIND_SWARM -> {
                every(age, 80, () -> spawnBats(world, player, 3));
                every(age, 50, () -> play(player, world, SoundEvents.BAT_LOOP, 0.7F, 0.65F));
            }
            case CAVE_CHILL -> {
                every(age, 90, () -> {
                    if (!isNearHeat(world, player.blockPosition())) {
                        player.addEffect(effect(MobEffects.MOVEMENT_SLOWDOWN, 120, 0));
                        player.addEffect(effect(MobEffects.DIG_SLOWDOWN, 120, 0));
                    } else {
                        player.addEffect(effect(MobEffects.REGENERATION, 80, 0));
                    }
                });
                every(age, 30, () -> world.sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY() + 1.0D, player.getZ(), 5, 3.0D, 1.2D, 3.0D, 0.01D));
            }
            case DEEP_RAGE -> every(age, 100, () -> {
                empowerHostiles(world, player, 28, MobEffects.MOVEMENT_SPEED, 160, 0);
                empowerHostiles(world, player, 28, MobEffects.DAMAGE_BOOST, 160, 0);
                if (randomFloat() < 0.28F) {
                    spawnZombie(world, player, false);
                }
            });
            case RIFT_CALL -> {
                every(age, 45, () -> {
                    playOffset(player, world, SoundEvents.PORTAL_AMBIENT, 14.0D, 1.4F, 0.45F);
                    world.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.2D, player.getZ(), 20, 7.0D, 2.0D, 7.0D, 0.03D);
                });
                every(age, 180, () -> player.addEffect(effect(MobEffects.DARKNESS, 80, 0)));
            }
            case BONE_DUST -> {
                every(age, 70, () -> empowerHostiles(world, player, 24, MobEffects.DAMAGE_RESISTANCE, 140, 0));
                every(age, 35, () -> world.sendParticles(ParticleTypes.WHITE_ASH, player.getX(), player.getY() + 1.0D, player.getZ(), 10, 5.0D, 1.5D, 5.0D, 0.01D));
                if (age == 20 * 28) {
                    spawnSkeleton(world, player);
                }
            }
            case ORE_SONG -> every(age, 85, () -> {
                playOffset(player, world, SoundEvents.NOTE_BLOCK_CHIME.value(), 8.0D, 1.2F, randomPitch(player, 0.8F, 1.6F));
                player.addEffect(effect(MobEffects.DIG_SPEED, 120, 0));
                world.sendParticles(ParticleTypes.ENCHANT, player.getX(), player.getY() + 1.0D, player.getZ(), 8, 4.0D, 1.2D, 4.0D, 0.02D);
            });
            case BLACK_TIDE -> every(age, 200, () -> {
                player.addEffect(effect(MobEffects.DARKNESS, 100, 0));
                play(player, world, SoundEvents.SCULK_SHRIEKER_SHRIEK, 1.0F, 0.7F);
                if (randomFloat() < 0.35F) {
                    spawnZombie(world, player, false);
                }
            });
            case STONE_SLEEP -> every(age, 100, () -> {
                if (player.getDeltaMovement().horizontalDistanceSqr() < 0.005D) {
                    player.addEffect(effect(MobEffects.MOVEMENT_SLOWDOWN, 160, 1));
                    player.addEffect(effect(MobEffects.DIG_SLOWDOWN, 160, 0));
                    play(player, world, SoundEvents.SCULK_CLICKING, 0.7F, 0.6F);
                }
            });
            case SCRAPING_BELOW -> every(age, 70, () -> {
                BlockPos below = player.blockPosition().below();
                world.sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, world.getBlockState(below)), below.getX() + 0.5D, below.getY() + 1.0D, below.getZ() + 0.5D, 16, 0.35D, 0.05D, 0.35D, 0.02D);
                play(player, world, SoundEvents.STONE_BREAK, 1.0F, 0.45F);
                if (age > 100 && randomFloat() < 0.24F) {
                    spawnZombie(world, player, false);
                }
            });
            case ASHEN_BREATH -> every(age, 100, () -> {
                if (!player.isInWater()) {
                    player.addEffect(effect(MobEffects.POISON, 60, 0));
                }
                world.sendParticles(ParticleTypes.ASH, player.getX(), player.getY() + 1.1D, player.getZ(), 12, 5.0D, 1.2D, 5.0D, 0.02D);
            });
            case UNSEEN_WATCHER -> every(age, 90, () -> {
                state.addMarks(1);
                player.addEffect(effect(MobEffects.GLOWING, 35, 0));
                play(player, world, SoundEvents.ENDERMAN_STARE, 0.7F, 0.55F);
                if (state.getMarks() >= 4) {
                    state.setMarks(0);
                    spawnZombie(world, player, true);
                }
            });
            case MIRROR_ECHO -> every(age, 75, () -> {
                playOffset(player, world, randomBoolean() ? SoundEvents.NOTE_BLOCK_BELL.value() : SoundEvents.SKELETON_STEP, 12.0D, 1.0F, randomPitch(player, 0.5F, 1.5F));
                if (randomFloat() < 0.20F) {
                    player.addEffect(effect(MobEffects.LUCK, 100, 0));
                }
            });
            case STONE_CRACK -> every(age, 100, () -> {
                play(player, world, SoundEvents.ANVIL_LAND, 1.1F, 0.6F);
                world.sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.STONE.defaultBlockState()), player.getX(), player.getY() + 2.2D, player.getZ(), 24, 2.4D, 0.3D, 2.4D, 0.08D);
                if (randomFloat() < 0.35F) {
                    player.hurt(world.damageSources().anvil(player), 2.0F);
                }
            });
            case SILENCE_AFTER_SCREAM -> {
                if (age == 20 * 25) {
                    play(player, world, SoundEvents.WARDEN_ROAR, 2.0F, 0.75F);
                    player.addEffect(effect(MobEffects.DARKNESS, 140, 0));
                    spawnZombie(world, player, true);
                }
            }
            case TORCH_CURSE -> every(age, 80, () -> {
                if (world.getMaxLocalRawBrightness(player.blockPosition()) > 8) {
                    player.addEffect(effect(MobEffects.WEAKNESS, 80, 0));
                    play(player, world, SoundEvents.SCULK_CLICKING, 0.8F, 0.5F);
                    world.sendParticles(ParticleTypes.SCULK_CHARGE_POP, player.getX(), player.getY() + 1.0D, player.getZ(), 8, 2.8D, 1.0D, 2.8D, 0.01D);
                }
            });
            case FALSE_SAFETY -> {
                every(age, 120, () -> player.addEffect(effect(MobEffects.REGENERATION, 100, 0)));
                if (age == durationTicks - 80) {
                    player.displayClientMessage(Component.translatable(translationKey("warning")), true);
                    play(player, world, SoundEvents.WARDEN_HEARTBEAT, 1.4F, 0.45F);
                }
            }
            case MOTHER_MOUNTAIN_HUM -> every(age, 100, () -> {
                player.addEffect(effect(MobEffects.DIG_SPEED, 140, 1));
                player.addEffect(effect(MobEffects.DARKNESS, 60, 0));
                if (randomFloat() < 0.25F) {
                    spawnZombie(world, player, false);
                }
            });
            case DEPTH_SEAL -> every(age, 120, () -> {
                player.addEffect(effect(MobEffects.WEAKNESS, 100, 0));
                player.addEffect(effect(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
                pulse(world, player, 14, 0.02D);
            });
            case TRACE_LEFT_BEHIND -> every(age, 85, () -> {
                state.addMarks(1);
                world.sendParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 0.2D, player.getZ(), 8, 2.0D, 0.2D, 2.0D, 0.01D);
                if (state.getMarks() >= 5) {
                    state.setMarks(0);
                    spawnZombie(world, player, true);
                    play(player, world, SoundEvents.SCULK_CATALYST_BLOOM, 1.2F, 0.6F);
                }
            });
        }
    }

    public void finish(ServerPlayer player, ServerLevel world, AfterdarkPlayerEventState state) {
        switch (this) {
            case DEEP_RAGE -> player.addEffect(effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 0));
            case BONE_DUST -> player.giveExperiencePoints(4 + randomInt(6));
            case ORE_SONG -> player.addEffect(effect(MobEffects.LUCK, 20 * 25, 0));
            case FALSE_SAFETY -> {
                player.addEffect(effect(MobEffects.DARKNESS, 120, 0));
                spawnZombie(world, player, true);
                spawnZombie(world, player, false);
            }
            case MOTHER_MOUNTAIN_HUM -> player.addEffect(effect(MobEffects.DAMAGE_RESISTANCE, 20 * 15, 0));
            case DEPTH_SEAL -> player.addEffect(effect(MobEffects.REGENERATION, 20 * 12, 0));
        }

        player.displayClientMessage(Component.translatable(translationKey("end")), true);
        state.setMarks(0);
    }

    private static MobEffectInstance effect(Holder<MobEffect> effect, int duration, int amplifier) {
        return new MobEffectInstance(effect, duration, amplifier, true, false, true);
    }

    private static void every(int age, int interval, Runnable runnable) {
        if (age > 0 && age % interval == 0) {
            runnable.run();
        }
    }

    private static float randomPitch(ServerPlayer player, float min, float max) {
        return min + randomFloat() * (max - min);
    }

    private static void play(ServerPlayer player, ServerLevel world, SoundEvent sound, float volume, float pitch) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.AMBIENT, volume, pitch);
    }

    private static void playBehind(ServerPlayer player, ServerLevel world, SoundEvent sound, float volume, float pitch) {
        double yaw = Math.toRadians(player.getYRot() + 180.0F);
        double x = player.getX() - Math.sin(yaw) * 4.0D;
        double z = player.getZ() + Math.cos(yaw) * 4.0D;
        world.playSound(null, x, player.getY(), z, sound, SoundSource.HOSTILE, volume, pitch);
    }

    private static void playOffset(ServerPlayer player, ServerLevel world, SoundEvent sound, double radius, float volume, float pitch) {
        double angle = randomDouble() * Math.PI * 2.0D;
        double distance = 3.0D + randomDouble() * radius;
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;
        world.playSound(null, x, player.getY(), z, sound, SoundSource.AMBIENT, volume, pitch);
    }

    private static void pulse(ServerLevel world, ServerPlayer player, int count, double speed) {
        world.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), count, 4.0D, 1.5D, 4.0D, speed);
    }

    private static void spawnBats(ServerLevel world, ServerPlayer player, int count) {
        for (int i = 0; i < count; i++) {
            Bat bat = new Bat(EntityType.BAT, world);
            BlockPos pos = randomNearbyAir(world, player, 7, 3);
            if (pos != null) {
                bat.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, randomFloat() * 360.0F, 0.0F);
                world.addFreshEntity(bat);
            }
        }
    }

    private static void spawnZombie(ServerLevel world, ServerPlayer player, boolean strong) {
        Zombie zombie = new Zombie(world);
        BlockPos pos = randomNearbyAir(world, player, 11, 2);
        if (pos == null) {
            return;
        }

        zombie.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        zombie.setTarget(player);
        if (strong) {
            zombie.addEffect(effect(MobEffects.DAMAGE_BOOST, 20 * 40, 0));
            zombie.addEffect(effect(MobEffects.MOVEMENT_SPEED, 20 * 40, 0));
        }
        world.addFreshEntity(zombie);
    }

    private static void spawnSkeleton(ServerLevel world, ServerPlayer player) {
        Skeleton skeleton = new Skeleton(EntityType.SKELETON, world);
        BlockPos pos = randomNearbyAir(world, player, 12, 2);
        if (pos == null) {
            return;
        }

        skeleton.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        skeleton.setTarget(player);
        skeleton.addEffect(effect(MobEffects.DAMAGE_RESISTANCE, 20 * 40, 0));
        world.addFreshEntity(skeleton);
    }

    private static void empowerHostiles(ServerLevel world, ServerPlayer player, int radius, Holder<MobEffect> effect, int duration, int amplifier) {
        List<Monster> entities = world.getEntitiesOfClass(Monster.class, AABB.ofSize(player.position(), radius * 2.0D, radius * 2.0D, radius * 2.0D), hostile -> hostile.isAlive());
        for (Monster hostile : entities) {
            hostile.addEffect(new MobEffectInstance(effect, duration, amplifier, true, true, true));
        }
    }

    private static void teleportSafely(ServerPlayer player, ServerLevel world, int radius) {
        BlockPos pos = randomNearbyAir(world, player, radius, 1);
        if (pos != null) {
            player.teleportTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        }
    }

    private static BlockPos randomNearbyAir(ServerLevel world, ServerPlayer player, int radius, int vertical) {
        BlockPos origin = player.blockPosition();
        for (int i = 0; i < 18; i++) {
            int x = origin.getX() + randomBetweenInclusive(-radius, radius);
            int y = origin.getY() + randomBetweenInclusive(-vertical, vertical);
            int z = origin.getZ() + randomBetweenInclusive(-radius, radius);
            BlockPos pos = new BlockPos(x, y, z);
            if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above()) && !world.isEmptyBlock(pos.below())) {
                return pos;
            }
        }
        return null;
    }

    private static boolean isNearHeat(ServerLevel world, BlockPos origin) {
        for (BlockPos pos : BlockPos.withinManhattan(origin, 5, 3, 5)) {
            if (world.getBlockState(pos).is(Blocks.LAVA) || world.getBlockState(pos).is(Blocks.FIRE) || world.getBlockState(pos).is(Blocks.SOUL_FIRE) || world.getMaxLocalRawBrightness(pos) >= 13) {
                return true;
            }
        }
        return false;
    }
    private static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    private static int randomBetweenInclusive(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    private static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    private static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
