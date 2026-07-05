package dev.denismasterherobrine.afterdark.neoforge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.entity.TeleportBlockEntity;
import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import dev.denismasterherobrine.afterdark.registry.AfterdarkFeaturesRegistry;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class AfterdarkNeoForgeRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, TheAfterdark.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheAfterdark.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, TheAfterdark.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, TheAfterdark.MOD_ID);

    public static final DeferredHolder<Block, Block> TELEPORT_BLOCK = BLOCKS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK);
    public static final DeferredHolder<Item, Item> TELEPORT_BLOCK_ITEM = ITEMS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
    public static final DeferredHolder<Item, Item> TELEPORT_CATALYST_ITEM = ITEMS.register("teleport_catalyst", () -> AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> TELEPORT_BLOCK_ENTITY = BLOCK_ENTITIES.register(TeleportBlockEntity.TELEPORT_BE_ID, () -> BlockEntityType.Builder.of(TeleportBlockEntity::new, TELEPORT_BLOCK.get()).build(null));


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TheAfterdark.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AFTERDARK = CREATIVE_MODE_TABS.register("afterdark", () -> AfterdarkRegistry.AFTERDARK);

    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_BASALT_PILLAR =
            FEATURES.register("large_basalt_pillar", () -> AfterdarkFeaturesRegistry.LARGE_BASALT_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<VerticalBlobConfiguration>> CRYSTAL_SPIKE = FEATURES.register("crystal_spike", () -> new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<SpiralConfiguration>> SPIRAL = FEATURES.register("spiral", () -> new SpiralFeature(SpiralConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<VerticalBlobConfiguration>> ADDITIVE_BLOB = FEATURES.register("additive_blob", () -> new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<AnvilRockConfiguration>> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () -> new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<CatchingFallConfiguration>> CATCHING_FALL = FEATURES.register("catching_fall", () -> new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<DoubleBlockConfiguration>> POND = FEATURES.register("pond", () -> new PondFeature(DoubleBlockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<VerticalBlobConfiguration>> ADDITIVE_GROUND_BLOB = FEATURES.register("additive_ground_blob", () -> new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_CALCITE_PILLAR =
            FEATURES.register("large_calcite_pillar", () -> AfterdarkFeaturesRegistry.LARGE_CALCITE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_PRISMARINE_PILLAR =
            FEATURES.register("large_prismarine_pillar", () -> AfterdarkFeaturesRegistry.LARGE_PRISMARINE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_DARK_PRISMARINE_PILLAR =
            FEATURES.register("large_dark_prismarine_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DARK_PRISMARINE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_SLIME_PILLAR =
            FEATURES.register("large_slime_pillar", () -> AfterdarkFeaturesRegistry.LARGE_SLIME_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<WaterloggableRandomPatchConfiguration>> WATERLOGGABLE_RANDOM_PATCH_FEATURE = FEATURES.register("waterloggable_random_patch", () -> new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CAVE_KELP_FEATURE = FEATURES.register("cave_kelp", () -> new CaveKelpFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CAVE_PICKLE_FEATURE = FEATURES.register("cave_pickle", () -> new CavePickleFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_DEEPSLATE_PILLAR =
            FEATURES.register("large_deepslate_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DEEPSLATE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<ProbabilityFeatureConfiguration>> CAVE_SEAGRASS_FEATURE = FEATURES.register("cave_seagrass", () -> new CaveSeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_OBSIDIAN_PILLAR =
            FEATURES.register("large_obsidian_pillar", () -> AfterdarkFeaturesRegistry.LARGE_OBSIDIAN_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<DiskConfiguration>> SUPPORTED_DISK_FEATURE = FEATURES.register("supported_disk", () -> new SupportedDiskFeature(DiskConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_PACKED_ICE_PILLAR =
            FEATURES.register("large_packed_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_PACKED_ICE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_ICE_PILLAR =
            FEATURES.register("large_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_ICE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_BLUE_ICE_PILLAR =
            FEATURES.register("large_blue_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_BLUE_ICE_PILLAR);

    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_RAW_IRON_PILLAR =
            FEATURES.register("raw_iron_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_IRON_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_RAW_COPPER_PILLAR =
            FEATURES.register("raw_copper_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_COPPER_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_RAW_GOLD_PILLAR =
            FEATURES.register("raw_gold_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_GOLD_PILLAR);

    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_REDSTONE_ORE_PILLAR =
            FEATURES.register("redstone_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_REDSTONE_ORE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_LAPIS_ORE_PILLAR =
            FEATURES.register("lapis_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_LAPIS_ORE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_EMERALD_ORE_PILLAR =
            FEATURES.register("emerald_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_EMERALD_ORE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_DIAMOND_ORE_PILLAR =
            FEATURES.register("diamond_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DIAMOND_ORE_PILLAR);
    public static final DeferredHolder<Feature<?>, Feature<PillarFeatureConfiguration>> LARGE_COAL_ORE_PILLAR =
            FEATURES.register("coal_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_COAL_ORE_PILLAR);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_POOL =
            FEATURES.register("black_honey_pool", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_POOL);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_SURFACE_PATCH =
            FEATURES.register("black_honey_surface_patch", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_SURFACE_PATCH);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_RESIN_FALL =
            FEATURES.register("black_honey_resin_fall", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_RESIN_FALL);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_COCOON_CLUSTER =
            FEATURES.register("black_honey_cocoon_cluster", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_COCOON_CLUSTER);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_HIVE_CYST =
            FEATURES.register("black_honey_hive_cyst", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_HIVE_CYST);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_ROOT_ARCH =
            FEATURES.register("black_honey_root_arch", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_ROOT_ARCH);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_MOURNING_FLOWER =
            FEATURES.register("black_honey_mourning_flower", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_MOURNING_FLOWER);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BLACK_HONEY_QUEEN_HEART =
            FEATURES.register("black_honey_queen_heart", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_QUEEN_HEART);
    public static final DeferredHolder<Feature<?>, Feature<StoneRibConfiguration>> STONE_RIB =
            FEATURES.register("stone_rib", () -> AfterdarkFeaturesRegistry.STONE_RIB);
    public static final DeferredHolder<Feature<?>, Feature<LightVeinConfiguration>> LIGHT_VEIN =
            FEATURES.register("light_vein", () -> AfterdarkFeaturesRegistry.LIGHT_VEIN);
    public static final DeferredHolder<Feature<?>, Feature<DarkTarConfiguration>> DARK_TAR =
            FEATURES.register("dark_tar", () -> AfterdarkFeaturesRegistry.DARK_TAR);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GOTHIC_COPPER_RUIN =
            FEATURES.register("gothic_copper_ruin", () -> AfterdarkFeaturesRegistry.GOTHIC_COPPER_RUIN);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> DEEP_AFTERDARK_STRUCTURE =
            FEATURES.register("deep_afterdark_structure", () -> AfterdarkFeaturesRegistry.DEEP_AFTERDARK_STRUCTURE);
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> AFTERDARK_MICRODECOR =
            FEATURES.register("afterdark_microdecor", () -> AfterdarkFeaturesRegistry.AFTERDARK_MICRODECOR);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        FEATURES.register(eventBus);
    }
}

