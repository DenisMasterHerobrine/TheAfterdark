package dev.denismasterherobrine.afterdark.registry;

import dev.architectury.injectables.annotations.PlatformOnly;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.features.*;
import dev.denismasterherobrine.afterdark.features.configuration.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
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

    public static void register() {
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_basalt_pillar"), LARGE_BASALT_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "additive_ground_blob"), ADDITIVE_GROUND_BLOB);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "crystal_spike"), CRYSTAL_SPIKE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "additive_blob"), ADDITIVE_BLOB);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "spiral"), SPIRAL);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "anvil_rock"), ANVIL_ROCK_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "catching_fall"), CATCHING_FALL);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "pond"), POND);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_calcite_pillar"), LARGE_CALCITE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_prismarine_pillar"), LARGE_PRISMARINE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_dark_prismarine_pillar"), LARGE_DARK_PRISMARINE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_slime_pillar"), LARGE_SLIME_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "waterloggable_random_patch"), WATERLOGGABLE_RANDOM_PATCH_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "cave_kelp"), CAVE_KELP_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "cave_pickle"), CAVE_PICKLE_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_deepslate_pillar"), LARGE_DEEPSLATE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "cave_seagrass"), CAVE_SEAGRASS_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_obsidian_pillar"), LARGE_OBSIDIAN_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "supported_disk"), SUPPORTED_DISK_FEATURE);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_packed_ice_pillar"), LARGE_PACKED_ICE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_ice_pillar"), LARGE_ICE_PILLAR);
        Registry.register(Registries.FEATURE, new Identifier(TheAfterdark.MOD_ID, "large_blue_ice_pillar"), LARGE_BLUE_ICE_PILLAR);
    }
}