package dev.denismasterherobrine.afterdark;

import net.minecraftforge.fml.common.Mod;

import dev.denismasterherobrine.TheAfterdark;

@Mod(TheAfterdark.MOD_ID)
public final class TheAfterdarkForge {
    public TheAfterdarkForge() {
        // Run our common setup.
        TheAfterdark.init();
    }
}
