package net.meteor.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockMeteorsModMetadata extends ItemBlockMeteorsMod {

	public ItemBlockMeteorsModMetadata(Block bl) {
		super(bl);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	//TODO 1.12.2
	/*@Override
	public String getRegistryName(ItemStack par1ItemStack)
	{
		return this.getBlock().getRegistryName() + "." + par1ItemStack.getItemDamage();
	}*/

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

}
