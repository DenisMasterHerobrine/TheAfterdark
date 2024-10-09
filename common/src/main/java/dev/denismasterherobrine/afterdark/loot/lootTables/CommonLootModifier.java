package dev.denismasterherobrine.afterdark.loot.lootTables;

import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.List;

public class CommonLootModifier {
    private static final List<Identifier> lootTablesIds = List.of(
            Identifier.of("minecraft", "chests/abandoned_mineshaft"),
            Identifier.of("minecraft", "chests/ancient_city"),
            Identifier.of("minecraft", "chests/buried_treasure"),
            Identifier.of("minecraft", "chests/end_city_treasure"),
            Identifier.of("minecraft", "chests/ruined_portal"),
            Identifier.of("minecraft", "chests/nether_bridge")
    );

    public static LootPool.Builder createLootPool() {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(0.05f))
                .with(ItemEntry.builder(AfterdarkRegistry.TELEPORT_CATALYST_ITEM))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
    }

    public static boolean isTargetLootTable(Identifier id) {
        return lootTablesIds.contains(id);
    }
}

