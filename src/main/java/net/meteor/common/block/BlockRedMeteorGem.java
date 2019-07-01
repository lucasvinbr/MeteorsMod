package net.meteor.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockRedMeteorGem extends BlockMeteorsMod {

	public BlockRedMeteorGem() {
		super(Material.ROCK);
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

}
