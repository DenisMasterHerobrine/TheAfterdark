package dev.denismasterherobrine.afterdark.loot.lootTables;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;

public class FabricLootModifier {
    public static void registerLootModifier() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            if (CommonLootModifier.isTargetLootTable(key.getValue())) {
                LootPool.Builder poolBuilder = CommonLootModifier.createLootPool();
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}

