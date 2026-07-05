package dev.denismasterherobrine.afterdark.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeleportCatalystItem extends Item {
    public TeleportCatalystItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {}
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}