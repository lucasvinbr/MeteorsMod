package net.meteor.common;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerPlayerBreakSpeed
{
	@SubscribeEvent
	public void onBreakSpeedModify(PlayerEvent.BreakSpeed event)
	{
		ItemStack stack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
		if (event.getEntityPlayer().isInsideOfMaterial(Material.WATER)) {
			Item i = stack.getItem();
			if ((i == MeteorItems.FrezaritePickaxe) || (i == MeteorItems.FrezariteSpade) || (i == MeteorItems.FrezariteAxe)) {
				event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
			}
		}
	}

}