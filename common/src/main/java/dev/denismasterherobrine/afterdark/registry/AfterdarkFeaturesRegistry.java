package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class AfterdarkFeaturesRegistry {
    public static final Feature<PillarFeatureConfiguration> LARGE_BASALT_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.basaltMix());
    public static final Feature<VerticalBlobConfiguration> CRYSTAL_SPIKE = new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<SpiralConfiguration> SPIRAL = new SpiralFeature(SpiralConfiguration.CODEC);
    public static final Feature<VerticalBlobConfiguration> ADDITIVE_BLOB = new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<AnvilRockConfiguration> ANVIL_ROCK_FEATURE = new AnvilRockFeature(AnvilRockConfiguration.CODEC);
    public static final Feature<CatchingFallConfiguration> CATCHING_FALL = new CatchingFallFeature(CatchingFallConfiguration.CODEC);
    public static final Feature<DoubleBlockConfiguration> POND = new PondFeature(DoubleBlockConfiguration.CODEC);
    public static final Feature<VerticalBlobConfiguration> ADDITIVE_GROUND_BLOB = new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<PillarFeatureConfiguration> LARGE_CALCITE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.CALCITE));
    public static final Feature<PillarFeatureConfiguration> LARGE_PRISMARINE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.PRISMARINE));
    public static final Feature<PillarFeatureConfiguration> LARGE_DARK_PRISMARINE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.DARK_PRISMARINE));
    public static final Feature<PillarFeatureConfiguration> LARGE_SLIME_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.SLIME_BLOCK));
    public static final Feature<WaterloggableRandomPatchConfiguration> WATERLOGGABLE_RANDOM_PATCH_FEATURE =
            new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC);
    public static final Feature<DefaultFeatureConfig> CAVE_KELP_FEATURE = new CaveKelpFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> CAVE_PICKLE_FEATURE = new CavePickleFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<PillarFeatureConfiguration> LARGE_DEEPSLATE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.DEEPSLATE));
    public static final Feature<ProbabilityConfig> CAVE_SEAGRASS_FEATURE = new CaveSeagrassFeature(ProbabilityConfig.CODEC);
    public static final Feature<PillarFeatureConfiguration> LARGE_OBSIDIAN_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.OBSIDIAN));
    public static final Feature<DiskFeatureConfig> SUPPORTED_DISK_FEATURE = new SupportedDiskFeature(DiskFeatureConfig.CODEC);
    public static final Feature<PillarFeatureConfiguration> LARGE_PACKED_ICE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.packedIceMix());
    public static final Feature<PillarFeatureConfiguration> LARGE_ICE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.ICE));
    public static final Feature<PillarFeatureConfiguration> LARGE_BLUE_ICE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleIceFamilyStop(Blocks.BLUE_ICE));

    public static final Feature<PillarFeatureConfiguration> LARGE_RAW_IRON_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.RAW_IRON_BLOCK));
    public static final Feature<PillarFeatureConfiguration> LARGE_RAW_GOLD_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.RAW_GOLD_BLOCK));
    public static final Feature<PillarFeatureConfiguration> LARGE_RAW_COPPER_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.RAW_COPPER_BLOCK));

    public static final Feature<PillarFeatureConfiguration> LARGE_DIAMOND_ORE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.DIAMOND_ORE));
    public static final Feature<PillarFeatureConfiguration> LARGE_EMERALD_ORE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.EMERALD_ORE));
    public static final Feature<PillarFeatureConfiguration> LARGE_LAPIS_ORE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.LAPIS_ORE));
    public static final Feature<PillarFeatureConfiguration> LARGE_REDSTONE_ORE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.REDSTONE_ORE));
    public static final Feature<PillarFeatureConfiguration> LARGE_COAL_ORE_PILLAR =
            new ConfigurableDripstoneStylePillarFeature(PillarFeatureConfiguration.CODEC, PillarPlacementRules.simpleStoneOnlyStop(Blocks.COAL_ORE));

    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_POOL =
            new BlackHoneyPoolFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_SURFACE_PATCH =
            new BlackHoneySurfacePatchFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_RESIN_FALL =
            new BlackHoneyResinFallFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_COCOON_CLUSTER =
            new BlackHoneyCocoonClusterFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_HIVE_CYST =
            new BlackHoneyHiveCystFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_ROOT_ARCH =
            new BlackHoneyRootArchFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_MOURNING_FLOWER =
            new BlackHoneyMourningFlowerFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> BLACK_HONEY_QUEEN_HEART =
            new BlackHoneyQueenHeartFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<StoneRibConfiguration> STONE_RIB =
            new StoneRibFeature(StoneRibConfiguration.CODEC);
    public static final Feature<LightVeinConfiguration> LIGHT_VEIN =
            new LightVeinFeature(LightVeinConfiguration.CODEC);
    public static final Feature<DarkTarConfiguration> DARK_TAR =
            new DarkTarFeature(DarkTarConfiguration.CODEC);
    public static final Feature<DefaultFeatureConfig> GOTHIC_COPPER_RUIN =
            new GothicCopperRuinFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> DEEP_AFTERDARK_STRUCTURE =
            new DeepAfterdarkStructureFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> AFTERDARK_MICRODECOR =
            new AfterdarkMicrodecorFeature(DefaultFeatureConfig.CODEC);
}
