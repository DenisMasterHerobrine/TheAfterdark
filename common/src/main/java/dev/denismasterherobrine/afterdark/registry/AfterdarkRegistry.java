package dev.denismasterherobrine.afterdark.registry;

import dev.denismasterherobrine.TheAfterdark;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
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
            RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<Biome> AFTERDARK_BIOME_BASIC = RegistryKey.of(RegistryKeys.BIOME, new Identifier(TheAfterdark.MOD_ID, "blank_biome"));

    public static final RegistryKey<World> AFTERDARK_LEVEL = RegistryKey.of(RegistryKeys.WORLD, new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static final RegistryKey<DimensionOptions> AFTERDARK_DIMENSION_OPTIONS = RegistryKey.of(RegistryKeys.DIMENSION, new Identifier(TheAfterdark.MOD_ID, "afterdark"));

    public static void bootstrapDimensionType(Registerable<DimensionType> context) {
        context.register(AFTERDARK_DIMENSION, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                false, // bedWorks
                true, // respawnAnchorWorks
                -64, // minY
                128, // height
                128, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                DimensionTypes.OVERWORLD_ID, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0)
        ));
    }
}
