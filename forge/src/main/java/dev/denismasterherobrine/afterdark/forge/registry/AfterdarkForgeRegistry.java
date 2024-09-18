package dev.denismasterherobrine.afterdark.forge.registry;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
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

    public static final RegistryObject<Block> TELEPORT_BLOCK = BLOCKS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK);
    public static final RegistryObject<Item> TELEPORT_BLOCK_ITEM = ITEMS.register("teleport_block", () -> AfterdarkRegistry.TELEPORT_BLOCK_ITEM);
    public static final RegistryObject<Item> TELEPORT_CATALYST_ITEM = ITEMS.register("teleport_catalyst", () -> AfterdarkRegistry.TELEPORT_CATALYST_ITEM);

    public static final DeferredRegister<ItemGroup> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.ITEM_GROUP.getKey(), TheAfterdark.MOD_ID);

    public static final RegistryObject<ItemGroup> AFTERDARK = CREATIVE_MODE_TABS.register("afterdark", () -> AfterdarkRegistry.AFTERDARK);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}