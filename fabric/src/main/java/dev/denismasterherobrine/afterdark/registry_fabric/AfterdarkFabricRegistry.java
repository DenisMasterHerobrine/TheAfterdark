package dev.denismasterherobrine.afterdark.registry_fabric;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.loot.lootTables.FabricLootModifier;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AfterdarkFabricRegistry {
    public static void registerItems() {
        Registry.register(Registries.BLOCK, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_catalyst"), AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
        Registry.register(Registries.ITEM_GROUP, Identifier.of(TheAfterdark.MOD_ID, "afterdark"), AfterdarkRegistry.AFTERDARK);
    }

    public static void register() {
        registerItems();
        FabricLootModifier.registerLootModifier();
    }
}