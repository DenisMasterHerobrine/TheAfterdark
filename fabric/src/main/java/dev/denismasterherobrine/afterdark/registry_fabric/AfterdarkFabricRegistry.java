package dev.denismasterherobrine.afterdark.registry_fabric;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.entity.TeleportBlockEntity;
import dev.denismasterherobrine.afterdark.loot.lootTables.FabricLootModifier;
import dev.denismasterherobrine.afterdark.registry.AfterdarkFeaturesRegistry;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class AfterdarkFabricRegistry {
    public static void registerItems() {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "teleport_block"), AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "teleport_catalyst"), AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "afterdark"), AfterdarkRegistry.AFTERDARK);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, TeleportBlockEntity.TELEPORT_BE_ID), FabricBlockEntityTypeBuilder.create(TeleportBlockEntity::new, AfterdarkRegistry.TELEPORT_BLOCK).build());
    }

    public static void registerFeatures() {
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_basalt_pillar"), AfterdarkFeaturesRegistry.LARGE_BASALT_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "additive_ground_blob"), AfterdarkFeaturesRegistry.ADDITIVE_GROUND_BLOB);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "crystal_spike"), AfterdarkFeaturesRegistry.CRYSTAL_SPIKE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "additive_blob"), AfterdarkFeaturesRegistry.ADDITIVE_BLOB);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "spiral"), AfterdarkFeaturesRegistry.SPIRAL);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "anvil_rock"), AfterdarkFeaturesRegistry.ANVIL_ROCK_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "catching_fall"), AfterdarkFeaturesRegistry.CATCHING_FALL);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "pond"), AfterdarkFeaturesRegistry.POND);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_calcite_pillar"), AfterdarkFeaturesRegistry.LARGE_CALCITE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_prismarine_pillar"), AfterdarkFeaturesRegistry.LARGE_PRISMARINE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_dark_prismarine_pillar"), AfterdarkFeaturesRegistry.LARGE_DARK_PRISMARINE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_slime_pillar"), AfterdarkFeaturesRegistry.LARGE_SLIME_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "waterloggable_random_patch"), AfterdarkFeaturesRegistry.WATERLOGGABLE_RANDOM_PATCH_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "cave_kelp"), AfterdarkFeaturesRegistry.CAVE_KELP_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "cave_pickle"), AfterdarkFeaturesRegistry.CAVE_PICKLE_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_deepslate_pillar"), AfterdarkFeaturesRegistry.LARGE_DEEPSLATE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "cave_seagrass"), AfterdarkFeaturesRegistry.CAVE_SEAGRASS_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_obsidian_pillar"), AfterdarkFeaturesRegistry.LARGE_OBSIDIAN_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "supported_disk"), AfterdarkFeaturesRegistry.SUPPORTED_DISK_FEATURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_packed_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_PACKED_ICE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_ICE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "large_blue_ice_pillar"), AfterdarkFeaturesRegistry.LARGE_BLUE_ICE_PILLAR);

        // Raw Ore Pillars
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "raw_iron_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_IRON_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "raw_gold_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_GOLD_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "raw_copper_pillar"), AfterdarkFeaturesRegistry.LARGE_RAW_COPPER_PILLAR);

        // Ore Pillars
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "diamond_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_DIAMOND_ORE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "emerald_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_EMERALD_ORE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "lapis_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_LAPIS_ORE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "redstone_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_REDSTONE_ORE_PILLAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "coal_ore_pillar"), AfterdarkFeaturesRegistry.LARGE_COAL_ORE_PILLAR);

        // Black Honey Grove
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_pool"), AfterdarkFeaturesRegistry.BLACK_HONEY_POOL);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_surface_patch"), AfterdarkFeaturesRegistry.BLACK_HONEY_SURFACE_PATCH);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_resin_fall"), AfterdarkFeaturesRegistry.BLACK_HONEY_RESIN_FALL);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_cocoon_cluster"), AfterdarkFeaturesRegistry.BLACK_HONEY_COCOON_CLUSTER);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_hive_cyst"), AfterdarkFeaturesRegistry.BLACK_HONEY_HIVE_CYST);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_root_arch"), AfterdarkFeaturesRegistry.BLACK_HONEY_ROOT_ARCH);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_mourning_flower"), AfterdarkFeaturesRegistry.BLACK_HONEY_MOURNING_FLOWER);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "black_honey_queen_heart"), AfterdarkFeaturesRegistry.BLACK_HONEY_QUEEN_HEART);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "stone_rib"), AfterdarkFeaturesRegistry.STONE_RIB);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "light_vein"), AfterdarkFeaturesRegistry.LIGHT_VEIN);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "dark_tar"), AfterdarkFeaturesRegistry.DARK_TAR);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "gothic_copper_ruin"), AfterdarkFeaturesRegistry.GOTHIC_COPPER_RUIN);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "deep_afterdark_structure"), AfterdarkFeaturesRegistry.DEEP_AFTERDARK_STRUCTURE);
        Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.tryBuild(TheAfterdark.MOD_ID, "afterdark_microdecor"), AfterdarkFeaturesRegistry.AFTERDARK_MICRODECOR);
    }

    public static void register() {
        registerItems();
        FabricLootModifier.registerLootModifier();
        registerFeatures();
    }
}
