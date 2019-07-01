package net.meteor.common.tileentity;

import net.meteor.common.item.ItemBlockSlippery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

public class TileEntitySlippery extends TileEntity {
	
	private String blockName = "stone";
	private Block facadeBlock = Blocks.STONE;
	
	public TileEntitySlippery() {}
	
	public TileEntitySlippery(String blockName) {
		this.blockName = blockName;
		this.facadeBlock = getBlockFromName(this.blockName);
	}
	
	public static Block getBlockFromName(String name) {
		return getBlockFromName(name, Blocks.STONE);
	}
	
	public static Block getBlockFromName(String name, Block def) {
		Block block = GameData.getBlockRegistry().getObject(name);
		return block == null ? def : block;
	}
	
	public static String getNameFromBlock(Block block) {
		return GameData.getBlockRegistry().getNameForObject(block);
	}
	
	public void setFacadeBlockName(String name) {
		this.blockName = name;
		this.facadeBlock = getBlockFromName(this.blockName);
	}
	
	public String getFacadeBlockName() {
		return blockName;
	}
	
	public Block getFacadeBlock() {
		return facadeBlock;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.blockName = nbt.getString(ItemBlockSlippery.FACADE_BLOCK_KEY);
		this.facadeBlock = getBlockFromName(this.blockName);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, blockName);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new SPacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

}
