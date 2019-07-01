package net.meteor.plugin.baubles;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MagnetizationOverlay {
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Text event) {
		if (Baubles.renderDisplay) {
			long time = Minecraft.getMinecraft().world.getTotalWorldTime();
			if (time < Baubles.renderDisplayTicks) {
				event.getRight().add(I18n.translateToLocal(Baubles.enabledMagnetism ? "info.magnetisationController.enabled" : "info.magnetisationController.disabled"));
			} else {
				Baubles.renderDisplay = false;
			}
		}
	}

}
