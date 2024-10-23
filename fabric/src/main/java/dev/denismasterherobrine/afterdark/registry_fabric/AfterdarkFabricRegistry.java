package dev.denismasterherobrine.afterdark.registry_fabric;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.loot.lootTables.FabricLootModifier;
import dev.denismasterherobrine.afterdark.registry.AfterdarkFeaturesRegistry;
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

    public static void registerFeatures() {
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_basalt_pillar"), AfterdarkFeaturesRegistry.LARGE_BASALT_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "additive_ground_blob"), AfterdarkFeaturesRegistry.ADDITIVE_GROUND_BLOB);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "crystal_spike"), AfterdarkFeaturesRegistry.CRYSTAL_SPIKE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "additive_blob"), AfterdarkFeaturesRegistry.ADDITIVE_BLOB);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "spiral"), AfterdarkFeaturesRegistry.SPIRAL);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "anvil_rock"), AfterdarkFeaturesRegistry.ANVIL_ROCK_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "catching_fall"), AfterdarkFeaturesRegistry.CATCHING_FALL);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "pond"), AfterdarkFeaturesRegistry.POND);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_calcite_pillar"), AfterdarkFeaturesRegistry.LARGE_CALCITE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_prismarine_pillar"), AfterdarkFeaturesRegistry.LARGE_PRISMARINE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_dark_prismarine_pillar"), AfterdarkFeaturesRegistry.LARGE_DARK_PRISMARINE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_slime_pillar"), AfterdarkFeaturesRegistry.LARGE_SLIME_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "waterloggable_random_patch"), AfterdarkFeaturesRegistry.WATERLOGGABLE_RANDOM_PATCH_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "cave_kelp"), AfterdarkFeaturesRegistry.CAVE_KELP_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "cave_pickle"), AfterdarkFeaturesRegistry.CAVE_PICKLE_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_deepslate_pillar"), AfterdarkFeaturesRegistry.LARGE_DEEPSLATE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "cave_seagrass"), AfterdarkFeaturesRegistry.CAVE_SEAGRASS_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_obsidian_pillar"), AfterdarkFeaturesRegistry.LARGE_OBSIDIAN_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "supported_disk"), AfterdarkFeaturesRegistry.SUPPORTED_DISK_FEATURE);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_packed_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_PACKED_ICE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_ICE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "large_blue_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_BLUE_ICE_PILLAR);

        // Raw Ore Pillars
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "raw_iron_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_IRON_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "raw_gold_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_GOLD_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "raw_copper_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_COPPER_PILLAR);

        // Ore Pillars
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "diamond_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_DIAMOND_ORE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "emerald_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_EMERALD_ORE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "lapis_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_LAPIS_ORE_PILLAR);
        Registry.register(Registries.FEATURE, Identifier.of(TheAfterdark.MOD_ID, "redstone_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_REDSTONE_ORE_PILLAR);
    }

    public static void register() {
        registerItems();
        FabricLootModifier.registerLootModifier();
        registerFeatures();
    }
}