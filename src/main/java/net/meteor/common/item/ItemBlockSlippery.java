package net.meteor.common.item;

import java.util.List;

import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
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
		return I18n.translateToLocalFormatted(this.getRegistryName() + ".name", facadeName);
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
        Block block = world.getBlockState(pos).getBlock();
        ItemStack itemStack = player.getHeldItem(hand);
        Block storedBlock = getStoredBlock(itemStack);
        BlockPos newPos = new BlockPos(pos);

        if (block == Blocks.SNOW_LAYER/* && (world.getBlockMetadata(x, y, z) & 7) < 1*/)//TODO 1.12.2 what is this checking?
        {
            facing = EnumFacing.UP;
        }
        else if (block != Blocks.VINE && block != Blocks.TALLGRASS && block != Blocks.DEADBUSH && !block.isReplaceable(world, pos))
        {
            if (facing == EnumFacing.DOWN)
            {
                newPos = newPos.down();
            }

            if (facing == EnumFacing.UP)
            {
                newPos = newPos.up();
            }

            if (facing == EnumFacing.NORTH)
            {
                newPos = newPos.north();
            }

            if (facing == EnumFacing.SOUTH)
            {
                newPos = newPos.south();
            }

            if (facing == EnumFacing.WEST)
            {
                newPos = newPos.west();
            }

            if (facing == EnumFacing.EAST)
            {
                newPos = newPos.east();
            }
        }

        if (itemStack.getCount() == 0)
        {
            return EnumActionResult.FAIL;
        }
        else if (!player.canPlayerEdit(newPos, facing, itemStack))
        {
            return EnumActionResult.FAIL;
        }
        else if (pos.getY() == 255 && storedBlock.getMaterial(storedBlock.getDefaultState()).isSolid())
        {
            return EnumActionResult.FAIL;
        }
        else if (world.mayPlace(storedBlock, newPos, false, facing, player))
        {
            int i1 = this.getMetadata(itemStack.getItemDamage());
            IBlockState newState = storedBlock.getDefaultState();//TODO 1.12.2 verify, guessing we should save the block state too ?

            if (placeBlockAt(itemStack, player, world, newPos, facing, hitX, hitY, hitZ, newState))
            {
                world.playSound((double)((float)newPos.getX() + 0.5F), (double)((float)newPos.getY() + 0.5F), (double)((float)newPos.getZ() + 0.5F), storedBlock.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (storedBlock.getSoundType().getVolume() + 1.0F) / 2.0F, storedBlock.getSoundType().getPitch() * 0.8F, true);
                itemStack.shrink(1);
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
