package dev.denismasterherobrine.afterdark.forge.loot.lootTables;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.loot.lootTables.CommonLootModifier;
import net.minecraft.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = TheAfterdark.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeLootModifier {
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

