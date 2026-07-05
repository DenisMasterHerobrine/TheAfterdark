package dev.denismasterherobrine.afterdark.loot.lootTables;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import java.util.List;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CommonLootModifier {
    private static final ResourceLocation ABANDONED_MINESHAFT_LOOT_TABLE = ResourceLocation.withDefaultNamespace("chests/abandoned_mineshaft");

    public static LootPool.Builder createLootPool() {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(Config.INSTANCE.shouldSpawnCatalyst ? 1 : 0))
                .when(LootItemRandomChanceCondition.randomChance(Config.INSTANCE.catalystSpawnChance))
                .add(LootItem.lootTableItem(AfterdarkRegistry.TELEPORT_CATALYST_ITEM))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)));
    }

    public static LootPool.Builder createAfterdarkMineshaftTreasurePool() {
        return LootPool.lootPool()
                .setRolls(UniformGenerator.between(0.0f, 1.0f))
                .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(AfterdarkRegistry.AFTERDARK_LEVEL)))
                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 5.0f))))
                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 8.0f))))
                .add(LootItem.lootTableItem(Items.ECHO_SHARD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f))))
                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f))));
    }

    public static boolean isTargetLootTable(ResourceLocation id) {
        List<String> lootTables = List.of(Config.INSTANCE.lootTables);
        for (String lootTable : lootTables) {
            if (id.toString().equals(lootTable)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAbandonedMineshaftLootTable(ResourceLocation id) {
        return ABANDONED_MINESHAFT_LOOT_TABLE.equals(id);
    }

    public static boolean isTargetLootTable(ResourceKey<LootTable> key) {
        return isTargetLootTable(key.location());
    }

    public static boolean isAbandonedMineshaftLootTable(ResourceKey<LootTable> key) {
        return isAbandonedMineshaftLootTable(key.location());
    }
}

