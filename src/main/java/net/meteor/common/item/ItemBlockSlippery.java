package net.meteor.common.item;

import java.util.List;

import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemBlockSlippery extends ItemBlock {
	
	public static final String FACADE_BLOCK_KEY = "slipFacadeBlock";

	public ItemBlockSlippery(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		checkNBT(itemStack);
		int slipperiness = getSlipperiness(this.getBlock());
        tooltip.add(I18n.translateToLocalFormatted("info.slipperyBlock.one", slipperiness));
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		checkNBT(itemStack);
		Block storedBlock = getStoredBlock(itemStack);
		String facadeName = new ItemStack(Item.getItemFromBlock(storedBlock), 1, itemStack.getItemDamage()).getDisplayName();
		return I18n.translateToLocalFormatted("tile.slipperyBlock.name", facadeName);
    }

	@Override
	public void onUpdate(ItemStack itemStack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		checkNBT(itemStack);
	}
	
	@Override
	public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
	
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(world, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && world.mayPlace(this.block, pos, false, facing, player))
        {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (placeBlockAt(itemstack, player, world, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                iblockstate1 = world.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, world, pos, player);
                world.playSound(player, pos, getStoredBlock(itemstack).getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
	
	public static void checkNBT(ItemStack itemStack) {
		if (itemStack == null) return;
		NBTTagCompound nbt = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		if (!nbt.hasKey(FACADE_BLOCK_KEY)) {
			Block def = Blocks.STONE;
			if (itemStack.getItem() instanceof ItemBlockSlippery) {
				ItemBlockSlippery itemBlock = (ItemBlockSlippery) itemStack.getItem();
				if (itemBlock.getBlock() instanceof BlockSlipperyStairs) {
					def = Blocks.OAK_STAIRS;
				}
			}
			nbt.setString(FACADE_BLOCK_KEY, TileEntitySlippery.getNameFromBlock(def).toString());
			itemStack.setTagCompound(nbt);
		}
	}
	
	public static Block getStoredBlock(ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			Block def = Blocks.STONE;
			if (itemStack.getItem() instanceof ItemBlockSlippery) {
				ItemBlockSlippery itemBlock = (ItemBlockSlippery) itemStack.getItem();
				if (itemBlock.getBlock() instanceof BlockSlipperyStairs) {
					def = Blocks.OAK_STAIRS;
				}
			}
			return TileEntitySlippery.getBlockFromName(itemStack.getTagCompound().getString(FACADE_BLOCK_KEY), def);
		}
		return Blocks.STONE;
	}
	
	public static int getSlipperiness(Block block) {
		return (int) ((block.slipperiness - 0.94F) / 0.04F);
	}

}
