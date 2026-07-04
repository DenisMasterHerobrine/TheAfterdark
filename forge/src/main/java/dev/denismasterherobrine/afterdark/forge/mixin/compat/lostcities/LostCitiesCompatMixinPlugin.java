package dev.denismasterherobrine.afterdark.forge.mixin.compat.lostcities;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

//
public final class LostCitiesCompatMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return isLostCitiesLoaded();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        if (!isLostCitiesLoaded()) {
            return Collections.emptyList();
        }

        return List.of(
                "LostCityFeatureCompatMixin",
                "LostCitiesSectionCacheBoundsMixin",
                "PlacedFeatureInsideLostSphereMixin"
        );
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static boolean isLostCitiesLoaded() {
        try {
            Class.forName("mcjty.lostcities.LostCities", false, LostCitiesCompatMixinPlugin.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
