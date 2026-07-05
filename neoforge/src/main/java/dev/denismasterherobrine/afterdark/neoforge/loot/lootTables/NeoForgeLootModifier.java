package dev.denismasterherobrine.afterdark.neoforge.loot.lootTables;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.loot.lootTables.CommonLootModifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@SuppressWarnings("unused")
public class NeoForgeLootModifier {
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getTable() != null) {
            if (CommonLootModifier.isTargetLootTable(event.getName())) {
                LootPool.Builder poolBuilder = CommonLootModifier.createLootPool();
                event.getTable().addPool(poolBuilder.build());
            }
            if (CommonLootModifier.isAbandonedMineshaftLootTable(event.getName())) {
                LootPool.Builder poolBuilder = CommonLootModifier.createAfterdarkMineshaftTreasurePool();
                event.getTable().addPool(poolBuilder.build());
            }
        }
    }
}


