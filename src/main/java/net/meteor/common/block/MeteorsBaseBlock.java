package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.Random;

public class MeteorsBaseBlock extends Block
{

	protected static final Random RANDOM = new Random();

	public MeteorsBaseBlock(Material par2Material)
	{
		super(par2Material);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

}