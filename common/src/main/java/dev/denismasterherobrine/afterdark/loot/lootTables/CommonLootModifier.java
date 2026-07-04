package dev.denismasterherobrine.afterdark.loot.lootTables;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.util.Identifier;

import java.util.List;

public class CommonLootModifier {
    private static final Identifier ABANDONED_MINESHAFT_LOOT_TABLE = new Identifier("minecraft", "chests/abandoned_mineshaft");

    public static LootPool.Builder createLootPool() {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(Config.INSTANCE.shouldSpawnCatalyst ? 1 : 0))
                .conditionally(RandomChanceLootCondition.builder(Config.INSTANCE.catalystSpawnChance))
                .with(ItemEntry.builder(AfterdarkRegistry.TELEPORT_CATALYST_ITEM))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)));
    }

    public static LootPool.Builder createAfterdarkMineshaftTreasurePool() {
        return LootPool.builder()
                .rolls(UniformLootNumberProvider.create(0.0f, 1.0f))
                .conditionally(LocationCheckLootCondition.builder(LocationPredicate.Builder.create().dimension(AfterdarkRegistry.AFTERDARK_LEVEL)))
                .with(ItemEntry.builder(Items.DIAMOND).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                .with(ItemEntry.builder(Items.EMERALD).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f))))
                .with(ItemEntry.builder(Items.AMETHYST_SHARD).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 8.0f))))
                .with(ItemEntry.builder(Items.ECHO_SHARD).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))))
                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))));
    }

    public static boolean isTargetLootTable(Identifier id) {
        List<String> lootTables = List.of(Config.INSTANCE.lootTables);
        for (String lootTable : lootTables) {
            if (id.toString().equals(lootTable)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAbandonedMineshaftLootTable(Identifier id) {
        return ABANDONED_MINESHAFT_LOOT_TABLE.equals(id);
    }
}

