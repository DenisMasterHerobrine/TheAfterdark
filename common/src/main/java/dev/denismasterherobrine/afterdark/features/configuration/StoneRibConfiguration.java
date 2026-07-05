package dev.denismasterherobrine.afterdark.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class StoneRibConfiguration implements FeatureConfiguration {
    public static final Codec<StoneRibConfiguration> CODEC = RecordCodecBuilder.create((fields) -> fields.group(
            BlockStateProvider.CODEC.fieldOf("spine_provider").forGetter(config -> config.spineProvider),
            BlockStateProvider.CODEC.fieldOf("accent_provider").forGetter(config -> config.accentProvider),
            IntProvider.codec(5, 24).fieldOf("span").forGetter(config -> config.span),
            IntProvider.codec(4, 24).fieldOf("height").forGetter(config -> config.height),
            IntProvider.codec(1, 5).fieldOf("thickness").forGetter(config -> config.thickness),
            Codec.floatRange(0.0F, 1.0F).fieldOf("accent_chance").orElse(0.12F).forGetter(config -> config.accentChance),
            Codec.floatRange(0.0F, 1.0F).fieldOf("side_thickness_chance").orElse(0.4F).forGetter(config -> config.sideThicknessChance)
    ).apply(fields, StoneRibConfiguration::new));

    public final BlockStateProvider spineProvider;
    public final BlockStateProvider accentProvider;
    public final IntProvider span;
    public final IntProvider height;
    public final IntProvider thickness;
    public final float accentChance;
    public final float sideThicknessChance;

    public StoneRibConfiguration(
            BlockStateProvider spineProvider,
            BlockStateProvider accentProvider,
            IntProvider span,
            IntProvider height,
            IntProvider thickness,
            float accentChance,
            float sideThicknessChance) {
        this.spineProvider = spineProvider;
        this.accentProvider = accentProvider;
        this.span = span;
        this.height = height;
        this.thickness = thickness;
        this.accentChance = accentChance;
        this.sideThicknessChance = sideThicknessChance;
    }
}
