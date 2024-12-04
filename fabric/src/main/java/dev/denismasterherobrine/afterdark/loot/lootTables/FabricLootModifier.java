package dev.denismasterherobrine.afterdark.loot.lootTables;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;

public class FabricLootModifier {
    public static void registerLootModifier() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, wrapperLookup) -> {
            if (CommonLootModifier.isTargetLootTable(key.getValue())) {
                LootPool.Builder poolBuilder = CommonLootModifier.createLootPool();
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}

