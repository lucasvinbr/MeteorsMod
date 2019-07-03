package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
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

	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
        ItemStack heldItem = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, heldItem))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            UseHoeEvent event = new UseHoeEvent(player, heldItem, world, pos);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return EnumActionResult.FAIL;
            }

            if (event.getResult() == Event.Result.ALLOW)
            {
                heldItem.damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }

            Block block = world.getBlockState(pos).getBlock();

            if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up()) && (block == Blocks.GRASS || block == Blocks.DIRT))
            {
                Block block1 = Blocks.FARMLAND;
                SoundType stepSound = block1.getSoundType();
                world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), stepSound.getStepSound(), SoundCategory.BLOCKS, (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F, true);

                if (world.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    world.setBlockState(pos, block1.getDefaultState().withProperty(BlockFarmland.MOISTURE, 14), 3);
                    heldItem.damageItem(1, player);
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
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

	//TODO 1.12.2
	//public Item setTexture(String s) {
	//	return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	//}
	
}