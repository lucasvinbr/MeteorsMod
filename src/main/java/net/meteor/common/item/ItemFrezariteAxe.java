package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemFrezariteAxe extends ItemAxe
{
	public ItemFrezariteAxe(Item.ToolMaterial toolMaterial, float damage, float speed)
	{
		super(toolMaterial, damage, speed);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezAxe.one"));
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezAxe.two"));
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