package dev.denismasterherobrine.afterdark.neoforge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeDripstoneFeatureConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class AfterdarkNeoForgeRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TheAfterdark.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TheAfterdark.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TheAfterdark.MOD_ID);

    public static final DeferredBlock<Block> TELEPORT_BLOCK = BLOCKS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK);
    public static final DeferredItem<Item> TELEPORT_BLOCK_ITEM = ITEMS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
    public static final DeferredItem<Item> TELEPORT_CATALYST_ITEM = ITEMS.register("teleport_catalyst", () -> AfterdarkRegistry.TELEPORT_CATALYST_ITEM);

    public static final DeferredRegister<ItemGroup> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.ITEM_GROUP, TheAfterdark.MOD_ID);

    public static final DeferredHolder<Feature<?>, LargeBasaltPillarFeature> LARGE_BASALT_PILLAR = FEATURES.register("large_basalt_pillar", () -> new LargeBasaltPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, CrystalSpikeFeature> CRYSTAL_SPIKE = FEATURES.register("crystal_spike", () -> new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, SpiralFeature> SPIRAL = FEATURES.register("spiral", () -> new SpiralFeature(SpiralConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, AdditiveBlobFeature> ADDITIVE_BLOB = FEATURES.register("additive_blob", () -> new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, AnvilRockFeature> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () -> new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, CatchingFallFeature> CATCHING_FALL = FEATURES.register("catching_fall", () -> new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, PondFeature> POND = FEATURES.register("pond", () -> new PondFeature(DoubleBlockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, AdditiveGroundBlobFeature> ADDITIVE_GROUND_BLOB = FEATURES.register("additive_ground_blob", () -> new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, LargeCalcitePillarFeature> LARGE_CALCITE_PILLAR = FEATURES.register("large_calcite_pillar", () -> new LargeCalcitePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargePrismarinePillarFeature> LARGE_PRISMARINE_PILLAR = FEATURES.register("large_prismarine_pillar", () -> new LargePrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeDarkPrismarinePillarFeature> LARGE_DARK_PRISMARINE_PILLAR = FEATURES.register("large_dark_prismarine_pillar", () -> new LargeDarkPrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeSlimePillarFeature> LARGE_SLIME_PILLAR = FEATURES.register("large_slime_pillar", () -> new LargeSlimePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, WaterloggableRandomPatchFeature> WATERLOGGABLE_RANDOM_PATCH_FEATURE = FEATURES.register("waterloggable_random_patch", () -> new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, CaveKelpFeature> CAVE_KELP_FEATURE = FEATURES.register("cave_kelp", () -> new CaveKelpFeature(DefaultFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, CavePickleFeature> CAVE_PICKLE_FEATURE = FEATURES.register("cave_pickle", () -> new CavePickleFeature(DefaultFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeDeepslatePillarFeature> LARGE_DEEPSLATE_PILLAR = FEATURES.register("large_deepslate_pillar", () -> new LargeDeepslatePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, CaveSeagrassFeature> CAVE_SEAGRASS_FEATURE = FEATURES.register("cave_seagrass", () -> new CaveSeagrassFeature(ProbabilityConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeObsidianPillarFeature> LARGE_OBSIDIAN_PILLAR = FEATURES.register("large_obsidian_pillar", () -> new LargeObsidianPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, SupportedDiskFeature> SUPPORTED_DISK_FEATURE = FEATURES.register("supported_disk", () -> new SupportedDiskFeature(DiskFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargePackedIcePillarFeature> LARGE_PACKED_ICE_PILLAR = FEATURES.register("large_packed_ice_pillar", () -> new LargePackedIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeIcePillarFeature> LARGE_ICE_PILLAR = FEATURES.register("large_ice_pillar", () -> new LargeIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeBlueIcePillarFeature> LARGE_BLUE_ICE_PILLAR = FEATURES.register("large_blue_ice_pillar", () -> new LargeBlueIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));

    // Raw Ore Pillars
    public static final DeferredHolder<Feature<?>, LargeRawIronPillarFeature> LARGE_RAW_IRON_PILLAR = FEATURES.register("raw_iron_pillar", () -> new LargeRawIronPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeRawCopperPillarFeature> LARGE_RAW_COPPER_PILLAR = FEATURES.register("raw_copper_pillar", () -> new LargeRawCopperPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeRawGoldPillarFeature> LARGE_RAW_GOLD_PILLAR = FEATURES.register("raw_gold_pillar", () -> new LargeRawGoldPillarFeature(LargeDripstoneFeatureConfig.CODEC));

    // Ore Pillars
    public static final DeferredHolder<Feature<?>, LargeRedstoneOrePillarFeature> LARGE_REDSTONE_ORE_PILLAR = FEATURES.register("redstone_ore_pillar", () -> new LargeRedstoneOrePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeLapisOrePillarFeature> LARGE_LAPIS_ORE_PILLAR = FEATURES.register("lapis_ore_pillar", () -> new LargeLapisOrePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeEmeraldOrePillarFeature> LARGE_EMERALD_ORE_PILLAR = FEATURES.register("emerald_ore_pillar", () -> new LargeEmeraldOrePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final DeferredHolder<Feature<?>, LargeDiamondOrePillarFeature> LARGE_DIAMOND_ORE_PILLAR = FEATURES.register("diamond_ore_pillar", () -> new LargeDiamondOrePillarFeature(LargeDripstoneFeatureConfig.CODEC));

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register("afterdark", () -> AfterdarkRegistry.AFTERDARK);

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        FEATURES.register(eventBus);
    }
}