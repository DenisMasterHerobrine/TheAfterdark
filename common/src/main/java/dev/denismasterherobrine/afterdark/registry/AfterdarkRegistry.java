package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.TeleportBlock;
import dev.denismasterherobrine.afterdark.items.TeleportCatalystItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class AfterdarkRegistry {
    public static final RegistryKey<DimensionType> AFTERDARK_DIMENSION =
            RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<Biome> AFTERDARK_BIOME_BASIC = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(TheAfterdark.MOD_ID, "blank_biome"));

    public static final RegistryKey<World> AFTERDARK_LEVEL = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<DimensionOptions> AFTERDARK_DIMENSION_OPTIONS = RegistryKey.of(RegistryKeys.DIMENSION, Identifier.of(TheAfterdark.MOD_ID, "afterdark"));

    public static void bootstrapDimensionType(Registerable<DimensionType> context) {
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
                DimensionTypes.OVERWORLD_ID, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 0) // monsterSettings
        ));
    }

    public static final Block TELEPORT_BLOCK = new TeleportBlock();
    public static final Item TELEPORT_BLOCK_ITEM = new BlockItem(TELEPORT_BLOCK, new Item.Settings());
    public static final Item TELEPORT_CATALYST_ITEM = new TeleportCatalystItem();

    public static ItemGroup AFTERDARK = ItemGroup.create(null, -1)
            .displayName(Text.translatable("itemGroup.afterdark"))
            .icon(() -> new ItemStack(AfterdarkRegistry.TELEPORT_BLOCK_ITEM))
            .entries((displayContext, entries) -> {
                entries.add(AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
                entries.add(AfterdarkRegistry.TELEPORT_CATALYST_ITEM);
            })
            .build();
}
