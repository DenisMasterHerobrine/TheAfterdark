package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.blocks.TeleportBlock;
import dev.denismasterherobrine.afterdark.items.TeleportCatalystItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class AfterdarkRegistry {
    public static final RegistryKey<DimensionType> AFTERDARK_DIMENSION =
            RegistryKey.of(BuiltinRegistries.DIMENSION_TYPE.getKey(), new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<Biome> AFTERDARK_BIOME_BASIC = RegistryKey.of(BuiltinRegistries.BIOME.getKey(), new Identifier(TheAfterdark.MOD_ID, "blank_biome"));

    public static final RegistryKey<World> AFTERDARK_LEVEL = RegistryKey.of(Registry.WORLD_KEY, new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<DimensionOptions> AFTERDARK_DIMENSION_OPTIONS = RegistryKey.of(Registry.DIMENSION_KEY, new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static void bootstrapDimensionType() {
        DimensionType dimensionType = new DimensionType(
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
        );

        Registry.register(BuiltinRegistries.DIMENSION_TYPE, AFTERDARK_DIMENSION.getValue(), dimensionType);
    }

    public static final Block TELEPORT_BLOCK = new TeleportBlock();

    public static final Item TELEPORT_CATALYST_ITEM = new TeleportCatalystItem();
    public static ItemGroup AFTERDARK = new ItemGroup(ItemGroup.GROUPS.length - 1, "afterdark") {
        public ItemStack createIcon() {
            return new ItemStack(TELEPORT_BLOCK_ITEM);
        }

        public void appendStacks(DefaultedList<ItemStack> stacks) {
            super.appendStacks(stacks);
            stacks.add(new ItemStack(TELEPORT_CATALYST_ITEM));
            stacks.add(new ItemStack(TELEPORT_BLOCK_ITEM));
        }
    };

    public static final Item TELEPORT_BLOCK_ITEM = new BlockItem(TELEPORT_BLOCK, new Item.Settings().group(AFTERDARK));


}
