package net.meteor.common.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockMeteorOre extends BlockMeteorsMod
{
	private final Item droppedItem;
	
	public BlockMeteorOre(Item droppedItem)
	{
		super(Material.IRON);
		this.setHarvestLevel("pickaxe", 2);
		this.droppedItem = droppedItem;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1 + random.nextInt(fortune + 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return this.droppedItem;
	}
	
}