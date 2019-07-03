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
	
	private String armorTex;
	
	public ItemMetArmor(ItemArmor.ArmorMaterial armorMaterial, int j, EntityEquipmentSlot slot)
	{
		super(armorMaterial, j, slot);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	public ItemMetArmor setArmorTexture(String tex) {
		this.armorTex = tex;
		return this;
	}

	//TODO 1.12.2
	//@Override
	//public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	//{
	//	if (slot == 2) {
	//		return MeteorsMod.MOD_ID + ":textures/armor/" + this.armorTex + "Armor_2.png";
	//	} else {
	//		return MeteorsMod.MOD_ID + ":textures/armor/" + this.armorTex + "Armor_1.png";
	//	}
	//}

	//TODO 1.12.2
	//public ItemMetArmor setTexture(String s) {
	//	return (ItemMetArmor)this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	//}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == MeteorItems.KreknoriteHelmet || this == MeteorItems.KreknoriteBody ||
				this == MeteorItems.KreknoriteLegs || this == MeteorItems.KreknoriteBoots) {
			tooltip.add(I18n.translateToLocal("info.kreknoriteArmorBonus"));
		}
	}
	
}