package net.meteor.common;

import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TooltipProvider {
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		int i = TileEntityFreezingMachine.getItemFreezeTime(event.getItemStack());
		if (i > 0) {
			event.getToolTip().add(I18n.translateToLocalFormatted("info.freezeTime", i));
		}
	}

}
