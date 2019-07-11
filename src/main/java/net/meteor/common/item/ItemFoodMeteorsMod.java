package net.meteor.common.item;

import net.meteor.common.MeteorsMod;
import net.minecraft.item.ItemFood;

public class ItemFoodMeteorsMod extends ItemFood
{
	public ItemFoodMeteorsMod(int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	public ItemFoodMeteorsMod(int amount, boolean isWolfFood)
    {
        this(amount, 0.6F, isWolfFood);
    }

}