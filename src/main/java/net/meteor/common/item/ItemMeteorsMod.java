package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemMeteorsMod extends Item
{
	public ItemMeteorsMod()
	{
		super();
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == MeteorItems.itemRedMeteorGem) {
			tooltip.add(TextFormatting.WHITE + I18n.translateToLocal("item.RedMeteorGem.desc.one"));
			tooltip.add(TextFormatting.WHITE + I18n.translateToLocal("item.RedMeteorGem.desc.two"));
		}
	}

}