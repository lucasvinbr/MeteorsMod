package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.ClientHandler;
import net.meteor.common.climate.GhostMeteor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGhostMeteor implements IMessage {
	
	private boolean addGhostMeteor;
	private int x;
	private int y;
	private int z;
	
	public PacketGhostMeteor() {
		this.addGhostMeteor = false;
		this.x = this.y = this.z = -1;
	}
	
	public PacketGhostMeteor(boolean adding, GhostMeteor gMet) {
		this(adding, gMet.x, 0, gMet.z);
	}
	
	public PacketGhostMeteor(boolean adding, int x, int y, int z) {
		this.addGhostMeteor = adding;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.addGhostMeteor = buffer.readBoolean();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeBoolean(addGhostMeteor);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketGhostMeteor, IMessage> {

		@Override
		public IMessage onMessage(PacketGhostMeteor message, MessageContext ctx) {
			BlockPos cc = new BlockPos(message.x, message.y, message.z);
			if (message.addGhostMeteor) {
				ClientHandler.ghostMetLocs.add(cc);
			} else if (cc.getX() == -1 && cc.getY() == -1 && cc.getZ() == -1) {
				ClientHandler.ghostMetLocs.clear();
			} else {
				for (int i = 0; i < ClientHandler.ghostMetLocs.size(); i++) {
					BlockPos cc2 = ClientHandler.ghostMetLocs.get(i);
					if (cc2.getX() == cc.getX() && cc2.getZ() == cc.getZ()) {
						ClientHandler.ghostMetLocs.remove(i);
						break;
					}
				}
			}
			return null;
		}
		
	}

}
