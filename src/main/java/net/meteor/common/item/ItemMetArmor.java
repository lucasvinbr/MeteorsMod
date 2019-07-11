package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemMetArmor extends ItemArmor
{

	public ItemMetArmor(ItemArmor.ArmorMaterial armorMaterial, int renderIndexIn, EntityEquipmentSlot slot)
	{
		super(armorMaterial, renderIndexIn, slot);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == MeteorItems.KreknoriteHelmet || this == MeteorItems.KreknoriteBody ||
				this == MeteorItems.KreknoriteLegs || this == MeteorItems.KreknoriteBoots) {
			tooltip.add(I18n.translateToLocal("info.kreknoriteArmorBonus"));
		}
	}
	
}