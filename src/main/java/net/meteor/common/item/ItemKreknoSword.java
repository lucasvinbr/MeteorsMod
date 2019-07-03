package net.meteor.common.item;

import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;

public class ItemKreknoSword extends ItemEnchSword
{
	public ItemKreknoSword(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		setEnch(Enchantments.FIRE_ASPECT, 2);
	}

	@Override
	public float getAttackDamage()
	{
		return 8;
	}
}