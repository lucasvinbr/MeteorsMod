package net.meteor.plugin.baubles;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketTogglePlayerMagnetism implements IMessage {
	
	private String playerName;
	
	public PacketTogglePlayerMagnetism() {}
	
	public PacketTogglePlayerMagnetism(String pName) {
		this.playerName = pName;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.playerName = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, this.playerName);
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketTogglePlayerMagnetism, IMessage> {

		@Override
		public IMessage onMessage(PacketTogglePlayerMagnetism message, MessageContext ctx) {
			EntityPlayer player = getClientPlayer();
			EntityPlayer p = player.getEntityWorld().getPlayerEntityByName(message.playerName);
			if (p != null) {
				IBaublesItemHandler inv = BaublesApi.getBaublesHandler(p);
				ItemStack stack = inv.getStackInSlot(3);
				if (stack.getItem() == Baubles.MagnetismController) {
					boolean val = !ItemMagnetismController.getNBTData(stack);
					ItemMagnetismController.setNBTData(stack, val);
					if (player.getName().equals(message.playerName)) {
						Baubles.renderDisplayTicks = player.world.getTotalWorldTime() + 100L;
						Baubles.renderDisplay = true;
						Baubles.enabledMagnetism = val;
					}
				}
			}
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public EntityPlayer getClientPlayer() {
			return Minecraft.getMinecraft().player;
		}
		
	}

}
