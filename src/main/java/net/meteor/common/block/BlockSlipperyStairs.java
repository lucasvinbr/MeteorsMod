package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockSlipperyStairs extends BlockStairs implements ITileEntityProvider {

	public BlockSlipperyStairs(float slipperiness) {
		super(Blocks.PACKED_ICE.getDefaultState());
		this.slipperiness = slipperiness;
		this.setCreativeTab(null);
		setSoundType(SoundType.GLASS);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    @Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null ? tileentity.receiveClientEvent(id, param) : false;
    }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySlippery();
	}
	
	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, REDSTONE wire, etc to this block.
     */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    //TODO 1.12.2
/*	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }*/
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		
		TileEntitySlippery tileEntity = (TileEntitySlippery) world.getTileEntity(pos);
		tileEntity.setFacadeBlockName(stack.getTagCompound().getString(ItemBlockSlippery.FACADE_BLOCK_KEY));
		tileEntity.markDirty();
		
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			//TODO 1.12.2 kill meta
/*
			TileEntitySlippery teSlippery = (TileEntitySlippery) world.getTileEntity(pos);
			ItemStack slipItem = new ItemStack(this, 1, teSlippery.getFacadeBlock().damageDropped(world.getBlockMetadata(x, y, z)));
	    	NBTTagCompound nbt = slipItem.hasTagCompound() ? slipItem.getTagCompound() : new NBTTagCompound();
	    	nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, teSlippery.getFacadeBlockName());
	    	slipItem.setTagCompound(nbt);
	    	this.dropBlockAsItem(world, pos, slipItem);
*/
		}
    	return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		Item item = getItem(world, pos, state).getItem();

        if (item == Items.AIR)
        {
            return null;
        }

        //TODO 1.12.2, kill meta
		/*
        BlockSlipperyStairs block = (BlockSlipperyStairs)world.getBlock(x, y, z);
        ItemStack stack = new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        TileEntitySlippery tileEntity = (TileEntitySlippery)world.getTileEntity(x, y, z);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, tileEntity.getFacadeBlockName());
		stack.setTagCompound(nbt);
		return stack;
	*/
		return null;
	}
	
}
