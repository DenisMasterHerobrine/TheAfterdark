package dev.denismasterherobrine.afterdark.forge.registry;

import com.mojang.serialization.Codec;
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
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheAfterdark.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TheAfterdark.MOD_ID);

    public static final RegistryObject<Block> TELEPORT_BLOCK = BLOCKS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK);
    public static final RegistryObject<Item> TELEPORT_BLOCK_ITEM = ITEMS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
    public static final RegistryObject<Item> TELEPORT_CATALYST_ITEM = ITEMS.register("teleport_catalyst", () -> AfterdarkRegistry.TELEPORT_CATALYST_ITEM);

    public static final DeferredRegister<ItemGroup> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.ITEM_GROUP.getKey(), TheAfterdark.MOD_ID);

    public static final RegistryObject<ItemGroup> AFTERDARK = CREATIVE_MODE_TABS.register("afterdark", () -> AfterdarkRegistry.AFTERDARK);

    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_BASALT_PILLAR = FEATURES.register("large_basalt_pillar", () -> new LargeBasaltPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> CRYSTAL_SPIKE = FEATURES.register("crystal_spike", () -> new CrystalSpikeFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<SpiralConfiguration>> SPIRAL = FEATURES.register("spiral", () -> new SpiralFeature(SpiralConfiguration.CODEC));
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> ADDITIVE_BLOB = FEATURES.register("additive_blob", () -> new AdditiveBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<AnvilRockConfiguration>> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () -> new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final RegistryObject<Feature<CatchingFallConfiguration>> CATCHING_FALL = FEATURES.register("catching_fall", () -> new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final RegistryObject<Feature<DoubleBlockConfiguration>> POND = FEATURES.register("pond", () -> new PondFeature(DoubleBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<VerticalBlobConfiguration>> ADDITIVE_GROUND_BLOB = FEATURES.register("additive_ground_blob", () -> new AdditiveGroundBlobFeature(VerticalBlobConfiguration.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_CALCITE_PILLAR = FEATURES.register("large_calcite_pillar", () -> new LargeCalcitePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_PRISMARINE_PILLAR = FEATURES.register("large_prismarine_pillar", () -> new LargePrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_DARK_PRISMARINE_PILLAR = FEATURES.register("large_dark_prismarine_pillar", () -> new LargeDarkPrismarinePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_SLIME_PILLAR = FEATURES.register("large_slime_pillar", () -> new LargeSlimePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<WaterloggableRandomPatchConfiguration>> WATERLOGGABLE_RANDOM_PATCH_FEATURE = FEATURES.register("waterloggable_random_patch", () -> new WaterloggableRandomPatchFeature(WaterloggableRandomPatchConfiguration.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> CAVE_KELP_FEATURE = FEATURES.register("cave_kelp", () -> new CaveKelpFeature(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DefaultFeatureConfig>> CAVE_PICKLE_FEATURE = FEATURES.register("cave_pickle", () -> new CavePickleFeature(DefaultFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_DEEPSLATE_PILLAR = FEATURES.register("large_deepslate_pillar", () -> new LargeDeepslatePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<ProbabilityConfig>> CAVE_SEAGRASS_FEATURE = FEATURES.register("cave_seagrass", () -> new CaveSeagrassFeature(ProbabilityConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_OBSIDIAN_PILLAR = FEATURES.register("large_obsidian_pillar", () -> new LargeObsidianPillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<DiskFeatureConfig>> SUPPORTED_DISK_FEATURE = FEATURES.register("supported_disk", () -> new SupportedDiskFeature(DiskFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_PACKED_ICE_PILLAR = FEATURES.register("large_packed_ice_pillar", () -> new LargePackedIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_ICE_PILLAR = FEATURES.register("large_ice_pillar", () -> new LargeIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));
    public static final RegistryObject<Feature<LargeDripstoneFeatureConfig>> LARGE_BLUE_ICE_PILLAR = FEATURES.register("large_blue_ice_pillar", () -> new LargeBlueIcePillarFeature(LargeDripstoneFeatureConfig.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        FEATURES.register(eventBus);
    }
}