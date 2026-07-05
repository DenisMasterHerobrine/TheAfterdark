package dev.denismasterherobrine.afterdark.loot.lootTables;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;

public class FabricLootModifier {
    public static void registerLootModifier() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (CommonLootModifier.isTargetLootTable(key)) {
                LootPool.Builder poolBuilder = CommonLootModifier.createLootPool();
                tableBuilder.pool(poolBuilder.build());
            }
            if (CommonLootModifier.isAbandonedMineshaftLootTable(key)) {
                LootPool.Builder poolBuilder = CommonLootModifier.createAfterdarkMineshaftTreasurePool();
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}

