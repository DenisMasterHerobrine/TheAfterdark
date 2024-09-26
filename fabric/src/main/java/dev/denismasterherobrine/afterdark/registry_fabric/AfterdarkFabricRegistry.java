package dev.denismasterherobrine.afterdark.registry_fabric;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class AfterdarkFabricRegistry {

    public static void registerItems() {
        Registry.register(Registries.BLOCK, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_catalyst"), AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
        Registry.register(Registries.ITEM_GROUP, Identifier.of(TheAfterdark.MOD_ID, "afterdark"), AfterdarkRegistry.AFTERDARK);
    }

    public static void modifyLootTables() {
        List<Identifier> lootTablesIds = List.of(
                new Identifier("minecraft", "chests/abandoned_mineshaft"),
                new Identifier("minecraft", "chests/ancient_city"),
                new Identifier("minecraft", "chests/buried_treasure"),
                new Identifier("minecraft", "chests/end_city_treasure"),
                new Identifier("minecraft", "chests/ruined_portal"),
                new Identifier("minecraft", "chests/nether_bridge")
        );

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (lootTablesIds.contains(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(AfterdarkRegistry.TELEPORT_CATALYST_ITEM))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
    }

    public static void register() {
        registerItems();
        modifyLootTables();
    }
}