package dev.denismasterherobrine.afterdark.neoforge.loot.lootTables;

import dev.denismasterherobrine.afterdark.loot.lootTables.CommonLootModifier;
import net.minecraft.loot.LootPool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@SuppressWarnings("unused")
public class NeoForgeLootModifier {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getTable() != null) {
            if (CommonLootModifier.isTargetLootTable(event.getName())) {
                LootPool.Builder poolBuilder = CommonLootModifier.createLootPool();
                event.getTable().addPool(poolBuilder.build());
            }
        }
    }
}

