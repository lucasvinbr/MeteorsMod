package net.meteor.plugin.baubles;

import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HandlerKey {
	
	private static KeyBinding toggleMagnetism;
	
	public void init() {
		toggleMagnetism = new KeyBinding("key.toggleMagnetism", Keyboard.KEY_Y, "key.categories.meteors");
		ClientRegistry.registerKeyBinding(toggleMagnetism);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (toggleMagnetism.isPressed()) {
			IInventory inv = BaublesApi.getBaubles(Minecraft.getMinecraft().player);
			ItemStack stack = inv.getStackInSlot(3);
			if (stack != null) {
				if (stack.getItem() == Baubles.MagnetismController) {
					MeteorsMod.network.sendToServer(new PacketToggleMagnetism());
				}
			}
		}
	}
	
	public static String getKey() {
		return Keyboard.getKeyName(toggleMagnetism.getKeyCode());
	}

}
