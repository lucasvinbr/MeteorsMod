package net.meteor.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;

public class AchievementMeteorsMod extends Advancement
{

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Item par5Item, Advancement par6Achievement)
	{
		this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Block par5Block, Advancement par6Achievement) {
		this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Advancement par6Achievement) {
		super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement);
		NBTTagCompound tag = par5ItemStack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setBoolean("enchant-set", true);
		par5ItemStack.setTagCompound(tag);
	}
	
}