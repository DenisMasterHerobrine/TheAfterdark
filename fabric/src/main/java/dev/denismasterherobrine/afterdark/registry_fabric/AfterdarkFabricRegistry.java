package dev.denismasterherobrine.afterdark.registry_fabric;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.loot.lootTables.FabricLootModifier;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AfterdarkFabricRegistry {
    public static void registerItems() {
        Registry.register(Registry.BLOCK, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK);
        Registry.register(Registry.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
        Registry.register(Registry.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_catalyst"), AfterdarkRegistry.TELEPORT_CATALYST_ITEM);

    }

    public static void register() {
        registerItems();
        FabricLootModifier.registerLootModifier();
    }
}