package net.meteor.common.block;

import java.util.*;

import net.meteor.common.MeteorsMod;
import net.meteor.common.block.propertys.UnlistedPropertyCopiedBlock;
import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.meteor.common.item.ItemBlockSlippery.checkNBT;

public class BlockSlippery extends BlockContainer {

    public static final UnlistedPropertyCopiedBlock COPIEDBLOCK = new UnlistedPropertyCopiedBlock();

    public BlockSlippery(float slipperiness, int level) {
        super(Material.ICE);
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setDefaultSlipperiness(slipperiness);
        setSoundType(SoundType.GLASS);
    }

    public static boolean canBeSlippery(Block block) {
        if ((block instanceof BlockSlippery || block instanceof BlockSlipperyStairs) && block.slipperiness < 1.1F) {
            return true;
        }

        if(block instanceof ITileEntityProvider)
            return false;

        if(block instanceof BlockSlab)
            return false;

        return true;//TODO this may end badly
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

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        TileEntitySlippery tileEntity = (TileEntitySlippery) world.getTileEntity(pos);
        tileEntity.setFacadeBlockName(stack.getTagCompound().getString(ItemBlockSlippery.FACADE_BLOCK_KEY));
        tileEntity.markDirty();

        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        Item item = super.getPickBlock(state, target, world, pos, player).getItem();//TODO 1.12.2 confirm

        if (item == Items.AIR) {
            return ItemStack.EMPTY;
        }
        //TODO 1.12.2 kill meta
		ItemStack stack = new ItemStack(ItemBlock.getItemFromBlock(state.getBlock()), 1);
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        TileEntitySlippery tileEntity = (TileEntitySlippery)world.getTileEntity(pos);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, tileEntity.getFacadeBlockName());
		stack.setTagCompound(nbt);
		return stack;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[] {};
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {COPIEDBLOCK};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {  // avoid crash in case of mismatch
            TileEntitySlippery tileEntity = (TileEntitySlippery) world.getTileEntity(pos);
            IExtendedBlockState retval = (IExtendedBlockState)state;
            IBlockState blockCamo = tileEntity.getFacadeBlock().getDefaultState();
            retval = retval.withProperty(COPIEDBLOCK, blockCamo);
            return retval;
        }
        return state;
    }


}
