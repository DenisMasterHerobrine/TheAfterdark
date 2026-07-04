package dev.denismasterherobrine.afterdark.events;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public enum AfterdarkCaveEventType {
    WALL_WHISPERS(35, 20 * 45, SoundEvents.AMBIENT_CAVE.value()),
    DEPTH_HUNGER(20, 20 * 60, SoundEvents.ENTITY_WARDEN_HEARTBEAT),
    DEAD_LIGHT(28, 20 * 35, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value()),
    STRANGER_FOOTSTEPS(28, 20 * 40, SoundEvents.ENTITY_ZOMBIE_STEP),
    STONE_HEARTBEAT(22, 20 * 55, SoundEvents.ENTITY_WARDEN_HEARTBEAT),
    MEMORY_DROP(16, 20 * 25, SoundEvents.ENTITY_ENDERMAN_TELEPORT),
    BLIND_SWARM(18, 20 * 45, SoundEvents.ENTITY_BAT_AMBIENT),
    CAVE_CHILL(20, 20 * 60, SoundEvents.BLOCK_GLASS_BREAK),
    DEEP_RAGE(16, 20 * 60, SoundEvents.ENTITY_WARDEN_ANGRY),
    RIFT_CALL(20, 20 * 60, SoundEvents.BLOCK_END_PORTAL_SPAWN),
    BONE_DUST(18, 20 * 55, SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT),
    ORE_SONG(22, 20 * 70, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value()),
    BLACK_TIDE(24, 20 * 70, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK),
    STONE_SLEEP(18, 20 * 55, SoundEvents.ENTITY_WARDEN_SNIFF),
    SCRAPING_BELOW(20, 20 * 45, SoundEvents.BLOCK_STONE_BREAK),
    ASHEN_BREATH(18, 20 * 45, SoundEvents.BLOCK_FIRE_EXTINGUISH),
    UNSEEN_WATCHER(18, 20 * 55, SoundEvents.ENTITY_ENDERMAN_STARE),
    MIRROR_ECHO(20, 20 * 60, SoundEvents.BLOCK_NOTE_BLOCK_BELL.value()),
    STONE_CRACK(20, 20 * 50, SoundEvents.BLOCK_ANVIL_LAND),
    SILENCE_AFTER_SCREAM(16, 20 * 45, SoundEvents.ENTITY_WARDEN_ROAR),
    TORCH_CURSE(18, 20 * 60, SoundEvents.BLOCK_SCULK_SENSOR_CLICKING),
    FALSE_SAFETY(14, 20 * 70, SoundEvents.BLOCK_BEACON_AMBIENT),
    MOTHER_MOUNTAIN_HUM(18, 20 * 70, SoundEvents.ENTITY_WARDEN_SONIC_CHARGE),
    DEPTH_SEAL(12, 20 * 80, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE),
    TRACE_LEFT_BEHIND(18, 20 * 65, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM);

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

    public void begin(ServerPlayerEntity player, ServerWorld world) {
        player.sendMessage(Text.translatable(translationKey("start")), true);
        play(player, world, omenSound, 0.9F, 0.55F + randomFloat() * 0.35F);
        pulse(world, player, 24, 0.08D);

        switch (this) {
            case DEPTH_HUNGER -> player.addStatusEffect(effect(StatusEffects.HUNGER, 160, 0));
            case DEAD_LIGHT -> player.addStatusEffect(effect(StatusEffects.DARKNESS, 120, 0));
            case MEMORY_DROP -> {
                player.addStatusEffect(effect(StatusEffects.NAUSEA, 220, 0));
                player.addStatusEffect(effect(StatusEffects.DARKNESS, 100, 0));
                teleportSafely(player, world, 4);
            }
            case BLIND_SWARM -> spawnBats(world, player, 7);
            case CAVE_CHILL -> {
                player.addStatusEffect(effect(StatusEffects.SLOWNESS, 180, 0));
                player.addStatusEffect(effect(StatusEffects.MINING_FATIGUE, 180, 0));
            }
            case DEEP_RAGE -> empowerHostiles(world, player, 24, StatusEffects.STRENGTH, 600, 0);
            case BONE_DUST -> player.addStatusEffect(effect(StatusEffects.WEAKNESS, 260, 0));
            case ORE_SONG -> player.addStatusEffect(effect(StatusEffects.LUCK, durationTicks, 0));
            case STONE_SLEEP -> player.addStatusEffect(effect(StatusEffects.MINING_FATIGUE, 160, 0));
            case ASHEN_BREATH -> player.addStatusEffect(effect(StatusEffects.POISON, 80, 0));
            case TORCH_CURSE -> player.addStatusEffect(effect(StatusEffects.UNLUCK, 240, 0));
            case FALSE_SAFETY -> player.addStatusEffect(effect(StatusEffects.REGENERATION, 260, 0));
            case MOTHER_MOUNTAIN_HUM -> player.addStatusEffect(effect(StatusEffects.HASTE, durationTicks, 1));
            case DEPTH_SEAL -> player.addStatusEffect(effect(StatusEffects.WEAKNESS, 240, 0));
        }
    }

    public void tick(ServerPlayerEntity player, ServerWorld world, AfterdarkPlayerEventState state) {
        int age = state.getActiveAge();

        switch (this) {
            case WALL_WHISPERS -> {
                every(age, 60, () -> play(player, world, SoundEvents.AMBIENT_CAVE.value(), 0.55F, randomPitch(player, 0.45F, 1.2F)));
                every(age, 80, () -> player.addStatusEffect(effect(StatusEffects.DARKNESS, 80, 0)));
                every(age, 25, () -> world.spawnParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1.1D, player.getZ(), 3, 3.5D, 1.3D, 3.5D, 0.01D));
            }
            case DEPTH_HUNGER -> {
                every(age, 100, () -> player.addStatusEffect(effect(StatusEffects.HUNGER, 140, 0)));
                every(age, 140, () -> player.addStatusEffect(effect(StatusEffects.HASTE, 100, 0)));
            }
            case DEAD_LIGHT -> {
                every(age, 70, () -> player.addStatusEffect(effect(StatusEffects.BLINDNESS, 45, 0)));
                every(age, 30, () -> world.spawnParticles(ParticleTypes.SMOKE, player.getX(), player.getY() + 1.0D, player.getZ(), 7, 4.0D, 1.2D, 4.0D, 0.01D));
            }
            case STRANGER_FOOTSTEPS -> {
                every(age, 45, () -> playBehind(player, world, SoundEvents.ENTITY_ZOMBIE_STEP, 0.8F, 0.55F));
                if (age == 20 * 22) {
                    spawnZombie(world, player, true);
                }
            }
            case STONE_HEARTBEAT -> every(age, 55, () -> {
                play(player, world, SoundEvents.ENTITY_WARDEN_HEARTBEAT, 1.8F, 0.55F);
                player.takeKnockback(0.35D, randomDouble() - 0.5D, randomDouble() - 0.5D);
                empowerHostiles(world, player, 18, StatusEffects.STRENGTH, 120, 0);
            });
            case MEMORY_DROP -> every(age, 90, () -> {
                player.addStatusEffect(effect(StatusEffects.NAUSEA, 120, 0));
                play(player, world, SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.7F, 0.4F);
                teleportSafely(player, world, 3);
            });
            case BLIND_SWARM -> {
                every(age, 80, () -> spawnBats(world, player, 3));
                every(age, 50, () -> play(player, world, SoundEvents.ENTITY_BAT_LOOP, 0.7F, 0.65F));
            }
            case CAVE_CHILL -> {
                every(age, 90, () -> {
                    if (!isNearHeat(world, player.getBlockPos())) {
                        player.addStatusEffect(effect(StatusEffects.SLOWNESS, 120, 0));
                        player.addStatusEffect(effect(StatusEffects.MINING_FATIGUE, 120, 0));
                    } else {
                        player.addStatusEffect(effect(StatusEffects.REGENERATION, 80, 0));
                    }
                });
                every(age, 30, () -> world.spawnParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY() + 1.0D, player.getZ(), 5, 3.0D, 1.2D, 3.0D, 0.01D));
            }
            case DEEP_RAGE -> every(age, 100, () -> {
                empowerHostiles(world, player, 28, StatusEffects.SPEED, 160, 0);
                empowerHostiles(world, player, 28, StatusEffects.STRENGTH, 160, 0);
                if (randomFloat() < 0.28F) {
                    spawnZombie(world, player, false);
                }
            });
            case RIFT_CALL -> {
                every(age, 45, () -> {
                    playOffset(player, world, SoundEvents.BLOCK_PORTAL_AMBIENT, 14.0D, 1.4F, 0.45F);
                    world.spawnParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.2D, player.getZ(), 20, 7.0D, 2.0D, 7.0D, 0.03D);
                });
                every(age, 180, () -> player.addStatusEffect(effect(StatusEffects.DARKNESS, 80, 0)));
            }
            case BONE_DUST -> {
                every(age, 70, () -> empowerHostiles(world, player, 24, StatusEffects.RESISTANCE, 140, 0));
                every(age, 35, () -> world.spawnParticles(ParticleTypes.WHITE_ASH, player.getX(), player.getY() + 1.0D, player.getZ(), 10, 5.0D, 1.5D, 5.0D, 0.01D));
                if (age == 20 * 28) {
                    spawnSkeleton(world, player);
                }
            }
            case ORE_SONG -> every(age, 85, () -> {
                playOffset(player, world, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), 8.0D, 1.2F, randomPitch(player, 0.8F, 1.6F));
                player.addStatusEffect(effect(StatusEffects.HASTE, 120, 0));
                world.spawnParticles(ParticleTypes.ENCHANT, player.getX(), player.getY() + 1.0D, player.getZ(), 8, 4.0D, 1.2D, 4.0D, 0.02D);
            });
            case BLACK_TIDE -> every(age, 200, () -> {
                player.addStatusEffect(effect(StatusEffects.DARKNESS, 100, 0));
                play(player, world, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, 1.0F, 0.7F);
                if (randomFloat() < 0.35F) {
                    spawnZombie(world, player, false);
                }
            });
            case STONE_SLEEP -> every(age, 100, () -> {
                if (player.getVelocity().horizontalLengthSquared() < 0.005D) {
                    player.addStatusEffect(effect(StatusEffects.SLOWNESS, 160, 1));
                    player.addStatusEffect(effect(StatusEffects.MINING_FATIGUE, 160, 0));
                    play(player, world, SoundEvents.BLOCK_SCULK_SENSOR_CLICKING, 0.7F, 0.6F);
                }
            });
            case SCRAPING_BELOW -> every(age, 70, () -> {
                BlockPos below = player.getBlockPos().down();
                world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, world.getBlockState(below)), below.getX() + 0.5D, below.getY() + 1.0D, below.getZ() + 0.5D, 16, 0.35D, 0.05D, 0.35D, 0.02D);
                play(player, world, SoundEvents.BLOCK_STONE_BREAK, 1.0F, 0.45F);
                if (age > 100 && randomFloat() < 0.24F) {
                    spawnZombie(world, player, false);
                }
            });
            case ASHEN_BREATH -> every(age, 100, () -> {
                if (!player.isTouchingWater()) {
                    player.addStatusEffect(effect(StatusEffects.POISON, 60, 0));
                }
                world.spawnParticles(ParticleTypes.ASH, player.getX(), player.getY() + 1.1D, player.getZ(), 12, 5.0D, 1.2D, 5.0D, 0.02D);
            });
            case UNSEEN_WATCHER -> every(age, 90, () -> {
                state.addMarks(1);
                player.addStatusEffect(effect(StatusEffects.GLOWING, 35, 0));
                play(player, world, SoundEvents.ENTITY_ENDERMAN_STARE, 0.7F, 0.55F);
                if (state.getMarks() >= 4) {
                    state.setMarks(0);
                    spawnZombie(world, player, true);
                }
            });
            case MIRROR_ECHO -> every(age, 75, () -> {
                playOffset(player, world, randomBoolean() ? SoundEvents.BLOCK_NOTE_BLOCK_BELL.value() : SoundEvents.ENTITY_SKELETON_STEP, 12.0D, 1.0F, randomPitch(player, 0.5F, 1.5F));
                if (randomFloat() < 0.20F) {
                    player.addStatusEffect(effect(StatusEffects.LUCK, 100, 0));
                }
            });
            case STONE_CRACK -> every(age, 100, () -> {
                play(player, world, SoundEvents.BLOCK_ANVIL_LAND, 1.1F, 0.6F);
                world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.STONE.getDefaultState()), player.getX(), player.getY() + 2.2D, player.getZ(), 24, 2.4D, 0.3D, 2.4D, 0.08D);
                if (randomFloat() < 0.35F) {
                    player.damage(world.getDamageSources().fallingAnvil(player), 2.0F);
                }
            });
            case SILENCE_AFTER_SCREAM -> {
                if (age == 20 * 25) {
                    play(player, world, SoundEvents.ENTITY_WARDEN_ROAR, 2.0F, 0.75F);
                    player.addStatusEffect(effect(StatusEffects.DARKNESS, 140, 0));
                    spawnZombie(world, player, true);
                }
            }
            case TORCH_CURSE -> every(age, 80, () -> {
                if (world.getLightLevel(player.getBlockPos()) > 8) {
                    player.addStatusEffect(effect(StatusEffects.WEAKNESS, 80, 0));
                    play(player, world, SoundEvents.BLOCK_SCULK_SENSOR_CLICKING, 0.8F, 0.5F);
                    world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, player.getX(), player.getY() + 1.0D, player.getZ(), 8, 2.8D, 1.0D, 2.8D, 0.01D);
                }
            });
            case FALSE_SAFETY -> {
                every(age, 120, () -> player.addStatusEffect(effect(StatusEffects.REGENERATION, 100, 0)));
                if (age == durationTicks - 80) {
                    player.sendMessage(Text.translatable(translationKey("warning")), true);
                    play(player, world, SoundEvents.ENTITY_WARDEN_HEARTBEAT, 1.4F, 0.45F);
                }
            }
            case MOTHER_MOUNTAIN_HUM -> every(age, 100, () -> {
                player.addStatusEffect(effect(StatusEffects.HASTE, 140, 1));
                player.addStatusEffect(effect(StatusEffects.DARKNESS, 60, 0));
                if (randomFloat() < 0.25F) {
                    spawnZombie(world, player, false);
                }
            });
            case DEPTH_SEAL -> every(age, 120, () -> {
                player.addStatusEffect(effect(StatusEffects.WEAKNESS, 100, 0));
                player.addStatusEffect(effect(StatusEffects.SLOWNESS, 80, 0));
                pulse(world, player, 14, 0.02D);
            });
            case TRACE_LEFT_BEHIND -> every(age, 85, () -> {
                state.addMarks(1);
                world.spawnParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 0.2D, player.getZ(), 8, 2.0D, 0.2D, 2.0D, 0.01D);
                if (state.getMarks() >= 5) {
                    state.setMarks(0);
                    spawnZombie(world, player, true);
                    play(player, world, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, 1.2F, 0.6F);
                }
            });
        }
    }

    public void finish(ServerPlayerEntity player, ServerWorld world, AfterdarkPlayerEventState state) {
        switch (this) {
            case DEEP_RAGE -> player.addStatusEffect(effect(StatusEffects.RESISTANCE, 20 * 20, 0));
            case BONE_DUST -> player.addExperience(4 + randomInt(6));
            case ORE_SONG -> player.addStatusEffect(effect(StatusEffects.LUCK, 20 * 25, 0));
            case FALSE_SAFETY -> {
                player.addStatusEffect(effect(StatusEffects.DARKNESS, 120, 0));
                spawnZombie(world, player, true);
                spawnZombie(world, player, false);
            }
            case MOTHER_MOUNTAIN_HUM -> player.addStatusEffect(effect(StatusEffects.RESISTANCE, 20 * 15, 0));
            case DEPTH_SEAL -> player.addStatusEffect(effect(StatusEffects.REGENERATION, 20 * 12, 0));
        }

        player.sendMessage(Text.translatable(translationKey("end")), true);
        state.setMarks(0);
    }

    private static StatusEffectInstance effect(StatusEffect effect, int duration, int amplifier) {
        return new StatusEffectInstance(effect, duration, amplifier, true, false, true);
    }

    private static void every(int age, int interval, Runnable runnable) {
        if (age > 0 && age % interval == 0) {
            runnable.run();
        }
    }

    private static float randomPitch(ServerPlayerEntity player, float min, float max) {
        return min + randomFloat() * (max - min);
    }

    private static void play(ServerPlayerEntity player, ServerWorld world, SoundEvent sound, float volume, float pitch) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundCategory.AMBIENT, volume, pitch);
    }

    private static void playBehind(ServerPlayerEntity player, ServerWorld world, SoundEvent sound, float volume, float pitch) {
        double yaw = Math.toRadians(player.getYaw() + 180.0F);
        double x = player.getX() - Math.sin(yaw) * 4.0D;
        double z = player.getZ() + Math.cos(yaw) * 4.0D;
        world.playSound(null, x, player.getY(), z, sound, SoundCategory.HOSTILE, volume, pitch);
    }

    private static void playOffset(ServerPlayerEntity player, ServerWorld world, SoundEvent sound, double radius, float volume, float pitch) {
        double angle = randomDouble() * Math.PI * 2.0D;
        double distance = 3.0D + randomDouble() * radius;
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;
        world.playSound(null, x, player.getY(), z, sound, SoundCategory.AMBIENT, volume, pitch);
    }

    private static void pulse(ServerWorld world, ServerPlayerEntity player, int count, double speed) {
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), count, 4.0D, 1.5D, 4.0D, speed);
    }

    private static void spawnBats(ServerWorld world, ServerPlayerEntity player, int count) {
        for (int i = 0; i < count; i++) {
            BatEntity bat = new BatEntity(EntityType.BAT, world);
            BlockPos pos = randomNearbyAir(world, player, 7, 3);
            if (pos != null) {
                bat.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, randomFloat() * 360.0F, 0.0F);
                world.spawnEntity(bat);
            }
        }
    }

    private static void spawnZombie(ServerWorld world, ServerPlayerEntity player, boolean strong) {
        ZombieEntity zombie = new ZombieEntity(world);
        BlockPos pos = randomNearbyAir(world, player, 11, 2);
        if (pos == null) {
            return;
        }

        zombie.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        zombie.setTarget(player);
        if (strong) {
            zombie.addStatusEffect(effect(StatusEffects.STRENGTH, 20 * 40, 0));
            zombie.addStatusEffect(effect(StatusEffects.SPEED, 20 * 40, 0));
        }
        world.spawnEntity(zombie);
    }

    private static void spawnSkeleton(ServerWorld world, ServerPlayerEntity player) {
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);
        BlockPos pos = randomNearbyAir(world, player, 12, 2);
        if (pos == null) {
            return;
        }

        skeleton.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        skeleton.setTarget(player);
        skeleton.addStatusEffect(effect(StatusEffects.RESISTANCE, 20 * 40, 0));
        world.spawnEntity(skeleton);
    }

    private static void empowerHostiles(ServerWorld world, ServerPlayerEntity player, int radius, StatusEffect effect, int duration, int amplifier) {
        List<HostileEntity> entities = world.getEntitiesByClass(HostileEntity.class, Box.of(player.getPos(), radius * 2.0D, radius * 2.0D, radius * 2.0D), hostile -> hostile.isAlive());
        for (HostileEntity hostile : entities) {
            hostile.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, true, true, true));
        }
    }

    private static void teleportSafely(ServerPlayerEntity player, ServerWorld world, int radius) {
        BlockPos pos = randomNearbyAir(world, player, radius, 1);
        if (pos != null) {
            player.requestTeleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        }
    }

    private static BlockPos randomNearbyAir(ServerWorld world, ServerPlayerEntity player, int radius, int vertical) {
        BlockPos origin = player.getBlockPos();
        for (int i = 0; i < 18; i++) {
            int x = origin.getX() + randomBetweenInclusive(-radius, radius);
            int y = origin.getY() + randomBetweenInclusive(-vertical, vertical);
            int z = origin.getZ() + randomBetweenInclusive(-radius, radius);
            BlockPos pos = new BlockPos(x, y, z);
            if (world.isAir(pos) && world.isAir(pos.up()) && !world.isAir(pos.down())) {
                return pos;
            }
        }
        return null;
    }

    private static boolean isNearHeat(ServerWorld world, BlockPos origin) {
        for (BlockPos pos : BlockPos.iterateOutwards(origin, 5, 3, 5)) {
            if (world.getBlockState(pos).isOf(Blocks.LAVA) || world.getBlockState(pos).isOf(Blocks.FIRE) || world.getBlockState(pos).isOf(Blocks.SOUL_FIRE) || world.getLightLevel(pos) >= 13) {
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
