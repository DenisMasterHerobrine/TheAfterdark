package com.example.examplemod;

import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//#if LOADER == FORGE
//$$ import net.minecraftforge.fml.common.Mod;
//#elseif LOADER == NEOFORGE
//$$ import net.neoforged.fml.common.Mod;
//#endif
//#if LOADER <= FORGE
//$$ @Mod(ExampleMod.MOD_ID)
//#endif
public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getName());

    //#if LOADER <= FORGE
    //$$ public ExampleMod() {
    //$$     onInitialize();
    //$$ }
    //#endif

    public void onInitialize() {
        //#if LOADER == FABRIC
        String loader = "Fabric";
        //#elseif LOADER == FORGE
        //$$ String loader = "Forge";
        //#elseif LOADER == NEOFORGE
        //$$ String loader = "NeoForge";
        //#endif
        LOGGER.info("Hello {} World!", loader);
        // This is a test to assert that the access widener works.
        LOGGER.info("Minecraft: {}", Minecraft.instance);
    }
}
