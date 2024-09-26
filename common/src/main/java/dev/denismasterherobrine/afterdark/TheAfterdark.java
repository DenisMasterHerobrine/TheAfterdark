package dev.denismasterherobrine.afterdark;

import dev.denismasterherobrine.afterdark.registry.AfterdarkFeaturesRegistry;

public final class TheAfterdark {
    public static final String MOD_ID = "the_afterdark";

    public static void init() {
        System.out.println("The Afterdark is initializing...");
        AfterdarkFeaturesRegistry.register();
    }
}
