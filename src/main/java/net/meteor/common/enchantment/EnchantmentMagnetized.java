package net.meteor.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentMagnetized extends Enchantment
{
	public EnchantmentMagnetized(Enchantment.Rarity rarityIn)
	{
		super(rarityIn, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD});
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 15 + (par1 - 1) * 6;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 40;
	}

	@Override
	public int getMaxLevel()
	{
		return 4;
	}

}