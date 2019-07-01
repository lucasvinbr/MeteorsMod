package net.meteor.common.crash;

import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.entity.EntityCometKitty;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CrashComet extends WorldGenerator {
	
	private EnumMeteor meteorType;
	
	public CrashComet(EnumMeteor type) {
		this.meteorType = type;
	}

	@Override
	public boolean generate(World world, Random random, BlockPos blockPos) {
		
		if (meteorType == EnumMeteor.KITTY) {
			EntityCometKitty kitty = new EntityCometKitty(world);
			kitty.setPosition(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
			world.spawnEntity(kitty);
		}

		int chance = 15;
		int blocksPlaced = 0;
		int px = blockPos.getX();
		int py = blockPos.getY();
		int pz = blockPos.getZ();

		for (int y = blockPos.getY() - 2; y <= blockPos.getY() + 2; y++) {
			for (int x = blockPos.getX() - 2; x <= blockPos.getX() + 2; x++) {
				for (int z = blockPos.getZ() - 2; z <= blockPos.getZ() + 2; z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (world.getBlockState(pos).getMaterial().isReplaceable()) {
						px = x;
						py = y;
						pz = z;
						if (random.nextInt(100) + 1 > chance) {
							world.setBlockState(pos, meteorType.getMaterial().getDefaultState(), 3);//1.12.2 TODO random.nextInt(4) + 1 meta?
							blocksPlaced++;
							chance = 15 + (20 * blocksPlaced);
						}
					}
				}
			}
		}
		
		// Helps to ensure a block gets placed if extremely unlucky
		if (blocksPlaced == 0) {
			BlockPos pos = new BlockPos(px, py, pz);
			if (world.getBlockState(pos).getMaterial().isReplaceable()) {
				world.setBlockState(pos, meteorType.getMaterial().getDefaultState(), 3);//1.12.2 TODO random.nextInt(4) + 1 meta?
			}
		}
		
		return true;
	}

}
