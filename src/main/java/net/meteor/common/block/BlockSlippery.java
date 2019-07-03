package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSlippery extends BlockContainerMeteorsMod {

	public BlockSlippery(float slipperiness) {
		super(Material.ICE);
		this.slipperiness = slipperiness;
		setSoundType(SoundType.GLASS);
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
/*
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }


	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon("ice");
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
	    	this.dropBlockAsItem(world, pos, slipItem, 0);
*/
		}
    	return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		Item item = super.getPickBlock(state, target, world, pos, player).getItem();//TODO 1.12.2 confirm

        if (item == Items.AIR)
        {
            return null;
        }
        //TODO 1.12.2 kill meta
		/*ItemStack stack = new ItemStack(item, 1, world.getBlockMetadata(x, y, z));
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        TileEntitySlippery tileEntity = (TileEntitySlippery)world.getTileEntity(pos);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, tileEntity.getFacadeBlockName());
		stack.setTagCompound(nbt);
		return stack;
	*/
		return null;
	}
	
	public static boolean canBeSlippery(Block block) {
		if ((block instanceof BlockSlippery || block instanceof BlockSlipperyStairs) && block.slipperiness < 1.1F) {
			return true;
		}
		//TODO 1.12.2
/*
		if (block.getRenderType() == 0 || block.getRenderType() == 31 || block.getRenderType() == 10 || block.getRenderType() == 39) {
			return !(block instanceof ITileEntityProvider) && !(block instanceof BlockSlab);
		}
*/
		return false;
	}

}
