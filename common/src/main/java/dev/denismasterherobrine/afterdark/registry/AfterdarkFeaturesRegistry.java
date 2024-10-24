package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeDripstoneFeatureConfig;

public class AfterdarkFeaturesRegistry {
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_BASALT_PILLAR = new LargeBasaltPillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<VerticalBlobConfiguration> CRYSTAL_SPIKE = new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<SpiralConfiguration> SPIRAL = new SpiralFeature(SpiralConfiguration.CODEC);
    public static final Feature<VerticalBlobConfiguration> ADDITIVE_BLOB = new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<AnvilRockConfiguration> ANVIL_ROCK_FEATURE = new AnvilRockFeature(AnvilRockConfiguration.CODEC);
    public static final Feature<CatchingFallConfiguration> CATCHING_FALL = new CatchingFallFeature(CatchingFallConfiguration.CODEC);
    public static final Feature<DoubleBlockConfiguration> POND = new PondFeature(DoubleBlockConfiguration.CODEC);
    public static final Feature<VerticalBlobConfiguration> ADDITIVE_GROUND_BLOB = new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_CALCITE_PILLAR = new LargeCalcitePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_PRISMARINE_PILLAR = new LargePrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_DARK_PRISMARINE_PILLAR = new LargeDarkPrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_SLIME_PILLAR = new LargeSlimePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<WaterloggableRandomPatchConfiguration> WATERLOGGABLE_RANDOM_PATCH_FEATURE = new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC);
    public static final Feature<DefaultFeatureConfig> CAVE_KELP_FEATURE = new CaveKelpFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> CAVE_PICKLE_FEATURE = new CavePickleFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_DEEPSLATE_PILLAR = new LargeDeepslatePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<ProbabilityConfig> CAVE_SEAGRASS_FEATURE = new CaveSeagrassFeature(ProbabilityConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_OBSIDIAN_PILLAR = new LargeObsidianPillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<DiskFeatureConfig> SUPPORTED_DISK_FEATURE = new SupportedDiskFeature(DiskFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_PACKED_ICE_PILLAR = new LargePackedIcePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_ICE_PILLAR = new LargeIcePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_BLUE_ICE_PILLAR = new LargeBlueIcePillarFeature(LargeDripstoneFeatureConfig.CODEC);

    // Raw Ore Pillars
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_RAW_IRON_PILLAR = new LargeRawIronPillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_RAW_GOLD_PILLAR = new LargeRawGoldPillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_RAW_COPPER_PILLAR = new LargeRawCopperPillarFeature(LargeDripstoneFeatureConfig.CODEC);

    // Ore Pillars
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_DIAMOND_ORE_PILLAR = new LargeDiamondOrePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_EMERALD_ORE_PILLAR = new LargeEmeraldOrePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_LAPIS_ORE_PILLAR = new LargeLapisOrePillarFeature(LargeDripstoneFeatureConfig.CODEC);
    public static final Feature<LargeDripstoneFeatureConfig> LARGE_REDSTONE_ORE_PILLAR = new LargeRedstoneOrePillarFeature(LargeDripstoneFeatureConfig.CODEC);

}