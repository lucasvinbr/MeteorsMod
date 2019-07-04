package net.meteor.common.tileentity;

import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySlippery extends TileEntity {

	private IBlockState facadeBlock = Blocks.STONE.getDefaultState();

	public TileEntitySlippery() {}

	public TileEntitySlippery(IBlockState blockName) {
		this.facadeBlock = blockName;
	}

	public void setFacadeBlockName(String name) {
		this.blockName = name;
		this.facadeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.blockName));
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
		final String blockState = nbt.getString(MeteorConstants.Slippery.FACADE_BLOCK_KEY);
		final String blockId = blockState.split("\\[")[0];
		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
		if (block == Blocks.AIR) { // We don't want to default to air.
			block = Blocks.STONE;
		}
		this.facadeBlock = block;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, blockName);
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1));
    }

}
