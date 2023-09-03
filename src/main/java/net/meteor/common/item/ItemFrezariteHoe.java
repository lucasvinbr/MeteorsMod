package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemFrezariteHoe extends ItemHoe
{
	public ItemFrezariteHoe(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

    public EnumActionResult onItemUse(EntityPlayer p_onItemUse_1_, World p_onItemUse_2_, BlockPos p_onItemUse_3_, EnumHand p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        ItemStack itemstack = p_onItemUse_1_.getHeldItem(p_onItemUse_4_);
        if (!p_onItemUse_1_.canPlayerEdit(p_onItemUse_3_.offset(p_onItemUse_5_), p_onItemUse_5_, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            int hook = ForgeEventFactory.onHoeUse(itemstack, p_onItemUse_1_, p_onItemUse_2_, p_onItemUse_3_);
            if (hook != 0) {
                return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            } else {
                IBlockState iblockstate = p_onItemUse_2_.getBlockState(p_onItemUse_3_);
                Block block = iblockstate.getBlock();
                if (p_onItemUse_5_ != EnumFacing.DOWN && p_onItemUse_2_.isAirBlock(p_onItemUse_3_.up())) {
                    if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                        this.setBlock(itemstack, p_onItemUse_1_, p_onItemUse_2_, p_onItemUse_3_, Blocks.FARMLAND.getDefaultState());
                        return EnumActionResult.SUCCESS;
                    }

                    if (block == Blocks.DIRT) {
                        switch((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT)) {
                            case DIRT:
                                Block block1 = Blocks.FARMLAND;
                                SoundType stepSound = block1.getSoundType();
                                p_onItemUse_2_.playSound((double)((float)p_onItemUse_3_.getX() + 0.5F), (double)((float)p_onItemUse_3_.getY() + 0.5F), (double)((float)p_onItemUse_3_.getZ() + 0.5F), stepSound.getStepSound(), SoundCategory.BLOCKS, (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F, true);
                                p_onItemUse_2_.setBlockState(p_onItemUse_3_, block1.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7), 3);
                                return EnumActionResult.SUCCESS;
                            case COARSE_DIRT:
                                this.setBlock(itemstack, p_onItemUse_1_, p_onItemUse_2_, p_onItemUse_3_, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                                return EnumActionResult.SUCCESS;
                        }
                    }
                }

                return EnumActionResult.PASS;
            }
        }
    }

	@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezHoe.one"));
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezHoe.two"));
	}

	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}

}