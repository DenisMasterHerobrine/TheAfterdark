package dev.denismasterherobrine.afterdark.forge.registry;

import com.mojang.serialization.Codec;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.entity.TeleportBlockEntity;
import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import dev.denismasterherobrine.afterdark.registry.AfterdarkFeaturesRegistry;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = TheAfterdark.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AfterdarkForgeRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TheAfterdark.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TheAfterdark.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheAfterdark.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TheAfterdark.MOD_ID);

    public static final RegistryObject<Block> TELEPORT_BLOCK = BLOCKS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK);
    public static final RegistryObject<Item> TELEPORT_BLOCK_ITEM = ITEMS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
    public static final RegistryObject<Item> TELEPORT_CATALYST_ITEM = ITEMS.register("teleport_catalyst", () -> AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
    public static final RegistryObject<BlockEntityType<?>> TELEPORT_BLOCK_ENTITY = BLOCK_ENTITIES.register(TeleportBlockEntity.TELEPORT_BE_ID, () -> BlockEntityType.Builder.create(TeleportBlockEntity::new, TELEPORT_BLOCK.get()).build(null));


    public static final DeferredRegister<ItemGroup> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.ITEM_GROUP.getKey(), TheAfterdark.MOD_ID);

    public static final RegistryObject<ItemGroup> AFTERDARK = CREATIVE_MODE_TABS.register("afterdark", () -> AfterdarkRegistry.AFTERDARK);

    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_BASALT_PILLAR =
            FEATURES.register("large_basalt_pillar", () -> AfterdarkFeaturesRegistry.LARGE_BASALT_PILLAR);
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> CRYSTAL_SPIKE = FEATURES.register("crystal_spike", () -> new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<SpiralConfiguration>> SPIRAL = FEATURES.register("spiral", () -> new SpiralFeature(SpiralConfiguration.CODEC));
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> ADDITIVE_BLOB = FEATURES.register("additive_blob", () -> new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<AnvilRockConfiguration>> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () -> new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final RegistryObject<Feature<CatchingFallConfiguration>> CATCHING_FALL = FEATURES.register("catching_fall", () -> new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final RegistryObject<Feature<DoubleBlockConfiguration>> POND = FEATURES.register("pond", () -> new PondFeature(DoubleBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> ADDITIVE_GROUND_BLOB = FEATURES.register("additive_ground_blob", () -> new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_CALCITE_PILLAR =
            FEATURES.register("large_calcite_pillar", () -> AfterdarkFeaturesRegistry.LARGE_CALCITE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_PRISMARINE_PILLAR =
            FEATURES.register("large_prismarine_pillar", () -> AfterdarkFeaturesRegistry.LARGE_PRISMARINE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_DARK_PRISMARINE_PILLAR =
            FEATURES.register("large_dark_prismarine_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DARK_PRISMARINE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_SLIME_PILLAR =
            FEATURES.register("large_slime_pillar", () -> AfterdarkFeaturesRegistry.LARGE_SLIME_PILLAR);
    public static final RegistryObject<Feature<WaterloggableRandomPatchConfiguration>> WATERLOGGABLE_RANDOM_PATCH_FEATURE = FEATURES.register("waterloggable_random_patch", () -> new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> CAVE_KELP_FEATURE = FEATURES.register("cave_kelp", () -> new CaveKelpFeature(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> CAVE_PICKLE_FEATURE = FEATURES.register("cave_pickle", () -> new CavePickleFeature(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_DEEPSLATE_PILLAR =
            FEATURES.register("large_deepslate_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DEEPSLATE_PILLAR);
    public static final RegistryObject<Feature<ProbabilityConfig>> CAVE_SEAGRASS_FEATURE = FEATURES.register("cave_seagrass", () -> new CaveSeagrassFeature(ProbabilityConfig.CODEC));
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_OBSIDIAN_PILLAR =
            FEATURES.register("large_obsidian_pillar", () -> AfterdarkFeaturesRegistry.LARGE_OBSIDIAN_PILLAR);
    public static final RegistryObject<Feature<DiskFeatureConfig>> SUPPORTED_DISK_FEATURE = FEATURES.register("supported_disk", () -> new SupportedDiskFeature(DiskFeatureConfig.CODEC));
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_PACKED_ICE_PILLAR =
            FEATURES.register("large_packed_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_PACKED_ICE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_ICE_PILLAR =
            FEATURES.register("large_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_ICE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_BLUE_ICE_PILLAR =
            FEATURES.register("large_blue_ice_pillar", () -> AfterdarkFeaturesRegistry.LARGE_BLUE_ICE_PILLAR);

    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_RAW_IRON_PILLAR =
            FEATURES.register("raw_iron_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_IRON_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_RAW_COPPER_PILLAR =
            FEATURES.register("raw_copper_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_COPPER_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_RAW_GOLD_PILLAR =
            FEATURES.register("raw_gold_pillar", () -> AfterdarkFeaturesRegistry.LARGE_RAW_GOLD_PILLAR);

    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_REDSTONE_ORE_PILLAR =
            FEATURES.register("redstone_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_REDSTONE_ORE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_LAPIS_ORE_PILLAR =
            FEATURES.register("lapis_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_LAPIS_ORE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_EMERALD_ORE_PILLAR =
            FEATURES.register("emerald_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_EMERALD_ORE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_DIAMOND_ORE_PILLAR =
            FEATURES.register("diamond_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_DIAMOND_ORE_PILLAR);
    public static final RegistryObject<Feature<PillarFeatureConfiguration>> LARGE_COAL_ORE_PILLAR =
            FEATURES.register("coal_ore_pillar", () -> AfterdarkFeaturesRegistry.LARGE_COAL_ORE_PILLAR);

    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_POOL =
            FEATURES.register("black_honey_pool", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_POOL);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_SURFACE_PATCH =
            FEATURES.register("black_honey_surface_patch", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_SURFACE_PATCH);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_RESIN_FALL =
            FEATURES.register("black_honey_resin_fall", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_RESIN_FALL);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_COCOON_CLUSTER =
            FEATURES.register("black_honey_cocoon_cluster", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_COCOON_CLUSTER);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_HIVE_CYST =
            FEATURES.register("black_honey_hive_cyst", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_HIVE_CYST);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_ROOT_ARCH =
            FEATURES.register("black_honey_root_arch", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_ROOT_ARCH);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_MOURNING_FLOWER =
            FEATURES.register("black_honey_mourning_flower", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_MOURNING_FLOWER);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> BLACK_HONEY_QUEEN_HEART =
            FEATURES.register("black_honey_queen_heart", () -> AfterdarkFeaturesRegistry.BLACK_HONEY_QUEEN_HEART);
    public static final RegistryObject<Feature<StoneRibConfiguration>> STONE_RIB =
            FEATURES.register("stone_rib", () -> AfterdarkFeaturesRegistry.STONE_RIB);
    public static final RegistryObject<Feature<LightVeinConfiguration>> LIGHT_VEIN =
            FEATURES.register("light_vein", () -> AfterdarkFeaturesRegistry.LIGHT_VEIN);
    public static final RegistryObject<Feature<DarkTarConfiguration>> DARK_TAR =
            FEATURES.register("dark_tar", () -> AfterdarkFeaturesRegistry.DARK_TAR);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> GOTHIC_COPPER_RUIN =
            FEATURES.register("gothic_copper_ruin", () -> AfterdarkFeaturesRegistry.GOTHIC_COPPER_RUIN);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> DEEP_AFTERDARK_STRUCTURE =
            FEATURES.register("deep_afterdark_structure", () -> AfterdarkFeaturesRegistry.DEEP_AFTERDARK_STRUCTURE);
    public static final RegistryObject<Feature<DefaultFeatureConfig>> AFTERDARK_MICRODECOR =
            FEATURES.register("afterdark_microdecor", () -> AfterdarkFeaturesRegistry.AFTERDARK_MICRODECOR);

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        FEATURES.register(eventBus);
    }
}
