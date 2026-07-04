package dev.denismasterherobrine.afterdark.events;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class AfterdarkCaveEventManager {
    private static final Map<UUID, AfterdarkPlayerEventState> STATES = new HashMap<>();
    private static final int MIN_COOLDOWN_TICKS = 20 * 60 * 8;
    private static final int MAX_COOLDOWN_TICKS = 20 * 60 * 15;
    private static final int MIN_CHECK_TICKS = 20 * 30;
    private static final int MAX_CHECK_TICKS = 20 * 60;

    private AfterdarkCaveEventManager() {
    }

    public static void tick(ServerPlayerEntity player) {
        if (player.getServer() == null || !player.getServer().isOnThread()) {
            return;
        }

        if (!Config.INSTANCE.afterdarkEventsEnabled) {
            STATES.remove(player.getUuid());
            return;
        }

        if (!(player.getWorld() instanceof ServerWorld world) || !world.getRegistryKey().equals(AfterdarkRegistry.AFTERDARK_LEVEL) || player.isSpectator() || player.isCreative()) {
            AfterdarkPlayerEventState removed = STATES.remove(player.getUuid());
            if (removed != null && removed.hasActiveEvent() && player.getWorld() instanceof ServerWorld oldWorld) {
                removed.getActiveEvent().finish(player, oldWorld, removed);
            }
            return;
        }

        AfterdarkPlayerEventState state = STATES.computeIfAbsent(player.getUuid(), uuid -> new AfterdarkPlayerEventState(randomCooldown()));

        if (state.hasActiveEvent()) {
            AfterdarkCaveEventType event = state.getActiveEvent();
            event.tick(player, world, state);
            state.decrementActiveTicks();

            if (state.getActiveTicks() <= 0) {
                event.finish(player, world, state);
                state.clearActiveEvent();
                state.setCooldownTicks(randomCooldown());
                state.resetCheckTicks(randomCheckDelay());
            }
            return;
        }

        state.decrementCooldownTicks();
        state.decrementCheckTicks();
        if (state.getCooldownTicks() > 0 || state.getCheckTicks() > 0) {
            return;
        }

        state.resetCheckTicks(randomCheckDelay());
        if (ThreadLocalRandom.current().nextFloat() > Config.INSTANCE.afterdarkEventChancePerCheck) {
            return;
        }

        AfterdarkCaveEventType event = pickEvent();
        state.start(event);
        event.begin(player, world);
    }

    private static AfterdarkCaveEventType pickEvent() {
        int totalWeight = 0;
        for (AfterdarkCaveEventType event : AfterdarkCaveEventType.values()) {
            totalWeight += event.getWeight();
        }

        int value = ThreadLocalRandom.current().nextInt(totalWeight);
        for (AfterdarkCaveEventType event : AfterdarkCaveEventType.values()) {
            value -= event.getWeight();
            if (value < 0) {
                return event;
            }
        }

        return AfterdarkCaveEventType.WALL_WHISPERS;
    }

    private static int randomCooldown() {
        return nextBetweenInclusive(MIN_COOLDOWN_TICKS, MAX_COOLDOWN_TICKS);
    }

    private static int randomCheckDelay() {
        return nextBetweenInclusive(MIN_CHECK_TICKS, MAX_CHECK_TICKS);
    }

    private static int nextBetweenInclusive(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
