package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.ClientHandler;
import net.meteor.common.climate.GhostMeteor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSoonestMeteor implements IMessage {
	
	private BlockPos soonestMeteorLoc;
	
	public PacketSoonestMeteor() {}
	
	public PacketSoonestMeteor(GhostMeteor gMeteor) {
		if (gMeteor != null) {
			this.soonestMeteorLoc = new BlockPos(gMeteor.x, 0, gMeteor.z);
		} else {
			this.soonestMeteorLoc = new BlockPos(-1, -1, -1);
		}
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		int x, y, z;
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		if (y == -1) {
			this.soonestMeteorLoc = null;
		} else {
			this.soonestMeteorLoc = new BlockPos(x, y, z);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(soonestMeteorLoc.getX());
		buffer.writeInt(soonestMeteorLoc.getY());
		buffer.writeInt(soonestMeteorLoc.getZ());
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketSoonestMeteor, IMessage> {

		@Override
		public IMessage onMessage(PacketSoonestMeteor message, MessageContext ctx) {
			ClientHandler.nearestTimeLocation = message.soonestMeteorLoc;
			return null;
		}
		
	}

}
