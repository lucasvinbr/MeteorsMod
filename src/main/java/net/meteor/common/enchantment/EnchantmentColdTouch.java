package net.meteor.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentColdTouch extends Enchantment
{
	public EnchantmentColdTouch(Enchantment.Rarity rarityIn)
	{
		super(rarityIn, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[] {EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET});
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 15 + (par1 - 1) * 6;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 45;
	}

	@Override
	public int getMaxLevel()
	{
		return 2;
	}

}