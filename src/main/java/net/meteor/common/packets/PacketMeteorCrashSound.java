package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketMeteorCrashSound implements IMessage {
	
	public int x, z;
	
	public PacketMeteorCrashSound() {}
	
	public PacketMeteorCrashSound(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.x = buffer.readInt();
		this.z = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(z);
	}
	
	public static class Handler implements IMessageHandler<PacketMeteorCrashSound, IMessage> {

		@Override
		public IMessage onMessage(PacketMeteorCrashSound message, MessageContext ctx) {

			EntityPlayer localPlayer = getClientPlayer();

			double xDiff = message.x - localPlayer.posX;
			double zDiff = message.z - localPlayer.posZ;
			double xMod = xDiff / 128.0D * 4.0D;
			double zMod = zDiff / 128.0D * 4.0D;
			localPlayer.world.playSound(localPlayer.posX + xMod, localPlayer.posY + 1.0D, localPlayer.posZ + zMod, new SoundEvent(new ResourceLocation(MeteorsMod.MOD_ID + ":meteor.crash")), SoundCategory.BLOCKS, 1.0F, 1.0F, true);

	        return null;
		}
		
		@SideOnly(Side.CLIENT)
		public EntityPlayer getClientPlayer() {
			return Minecraft.getMinecraft().player;
		}
		
	}

}
