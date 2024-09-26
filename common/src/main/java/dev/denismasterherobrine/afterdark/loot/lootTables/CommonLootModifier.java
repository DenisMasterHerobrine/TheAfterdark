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
            new Identifier("minecraft", "chests/abandoned_mineshaft"),
            new Identifier("minecraft", "chests/ancient_city"),
            new Identifier("minecraft", "chests/buried_treasure"),
            new Identifier("minecraft", "chests/end_city_treasure"),
            new Identifier("minecraft", "chests/ruined_portal"),
            new Identifier("minecraft", "chests/nether_bridge")
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

