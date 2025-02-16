package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RedMeteorGemBlock extends Block {

	public RedMeteorGemBlock() {
		super(Material.ROCK);
		this.setCreativeTab(MeteorsMod.meteorTab);
		this.setHardness(5.0F).setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

}
