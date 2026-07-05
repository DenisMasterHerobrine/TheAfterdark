package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.TeleportBlock;
import dev.denismasterherobrine.afterdark.items.TeleportCatalystItem;
import java.util.OptionalLong;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class AfterdarkRegistry {
    public static final ResourceKey<DimensionType> AFTERDARK_DIMENSION =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(TheAfterdark.MOD_ID, "afterdark"));

    public static final ResourceKey<Biome> AFTERDARK_BIOME_BASIC = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(TheAfterdark.MOD_ID, "blank_biome"));

    public static final ResourceKey<Level> AFTERDARK_LEVEL = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(TheAfterdark.MOD_ID, "afterdark"));

    public static final ResourceKey<LevelStem> AFTERDARK_DIMENSION_OPTIONS = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(TheAfterdark.MOD_ID, "afterdark"));

    public static void bootstrapDimensionType(BootstrapContext<DimensionType> context) {
        context.register(AFTERDARK_DIMENSION, new DimensionType(
                OptionalLong.of(21000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                false, // bedWorks
                true, // respawnAnchorWorks
                -128, // minY
                384, // height
                320, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.1f, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0) // monsterSettings
        ));
    }

    public static final Block TELEPORT_BLOCK = new TeleportBlock();
    public static final Item TELEPORT_BLOCK_ITEM = new BlockItem(TELEPORT_BLOCK, new Item.Properties());
    public static final Item TELEPORT_CATALYST_ITEM = new TeleportCatalystItem();

    public static CreativeModeTab AFTERDARK = CreativeModeTab.builder(null, -1)
            .title(Component.translatable("itemGroup.afterdark"))
            .icon(() -> new ItemStack(AfterdarkRegistry.TELEPORT_BLOCK_ITEM))
            .displayItems((displayContext, entries) -> {
                entries.accept(AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
                entries.accept(AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
            })
            .build();
}
