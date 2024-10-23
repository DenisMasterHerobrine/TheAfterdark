package dev.denismasterherobrine.afterdark.items;

import dev.denismasterherobrine.afterdark.TheAfterdark;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TeleportCatalystItem extends Item {
    public TeleportCatalystItem() {
        super(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheAfterdark.MOD_ID, "teleport_catalyst"))));
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
        }
        return ActionResult.SUCCESS;
    }
}