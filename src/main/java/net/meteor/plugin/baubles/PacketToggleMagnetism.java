package net.meteor.plugin.baubles;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.meteor.common.MeteorsMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleMagnetism implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	// Server Side
	public static class Handler implements IMessageHandler<PacketToggleMagnetism, IMessage> {

		@Override
		public IMessage onMessage(PacketToggleMagnetism message, MessageContext ctx) {
			IBaublesItemHandler inv = BaublesApi.getBaublesHandler(ctx.getServerHandler().player);
			ItemStack stack = inv.getStackInSlot(3);
			if (stack != ItemStack.EMPTY) {
				if (stack.getItem() == Baubles.MagnetismController) {
					if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
						boolean val = !ItemMagnetismController.getNBTData(stack);
						ItemMagnetismController.setNBTData(stack, val);	
					}
					MeteorsMod.network.sendToAll(new PacketTogglePlayerMagnetism(ctx.getServerHandler().player.getName()));
				}
			}
			return null;
		}
		
	}

}
