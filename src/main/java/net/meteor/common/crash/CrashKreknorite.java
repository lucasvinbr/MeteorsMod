package net.meteor.common.crash;

import net.meteor.common.EnumMeteor;
import net.meteor.common.ExplosionMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.SBAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class CrashKreknorite extends CrashMeteorite
{
	public CrashKreknorite(int Size, ExplosionMeteor expl, EnumMeteor metType)
	{
		super(Size, expl, metType);
	}

	@Override
    public void afterCraterFormed(World world, Random random, int xIn, int yIn, int zIn) {
		if (this.crashSize >= MeteorsMod.instance.MinMeteorSizeForPortal) {
			createPortal(world, xIn, yIn, zIn, random.nextBoolean());
		}
		int blazes = random.nextInt(3);
		List<BlockPos> affectedBlockPositions = this.explosion.getAffectedBlockPositions();
		for (int j1 = affectedBlockPositions.size() - 1; (j1 >= 0) &&
				(blazes > 0); j1--)
		{
			BlockPos blockPos = affectedBlockPositions.get(j1);
			int x = blockPos.getX();
			int y = blockPos.getY();
			int z = blockPos.getZ();
			BlockPos pos = new BlockPos(x, y, z);
			boolean isAir = world.isAirBlock(pos);
			IBlockState k2 = world.getBlockState(pos.down());
			if (isAir && k2.isOpaqueCube() && (random.nextInt(10) > 4)) {
				EntityBlaze blaze = new EntityBlaze(world);
				blaze.setPositionAndRotation(x, y, z, 0.0F, 0.0F);
				world.spawnEntity(blaze);
				blazes--;
			}
		}
	}

	private void createPortal(World world, int i, int j, int k, boolean directionFlag) {
		if (directionFlag) {
			//Base
			SBAPI.generateCuboid(world, i + 1, j + 1, k, i, j + 1, k, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 2, k + 1, i - 1, j + 2, k - 1, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 3, j + 3, k + 2, i - 2, j + 3, k - 2, Blocks.NETHER_BRICK);
			//Portal
			SBAPI.generateCuboid(world, i + 2, j + 3, k, i - 1, j + 7, k, Blocks.OBSIDIAN, 0, true);
			//Columns
			SBAPI.generateCuboid(world, i + 3, j + 4, k + 2, i + 3, j + 8, k + 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 3, j + 4, k - 2, i + 3, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i - 2, j + 4, k - 2, i - 2, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i - 2, j + 4, k + 2, i - 2, j + 8, k + 2, Blocks.NETHER_BRICK);
			//First level of roof
			SBAPI.generateCuboid(world, i + 3, j + 8, k + 3, i - 2, j + 8, k + 3, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 3, j + 8, k - 3, i - 2, j + 8, k - 3, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 4, j + 8, k + 2, i + 4, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i - 3, j + 8, k + 2, i - 3, j + 8, k - 2, Blocks.NETHER_BRICK);
			//Second level of roof
			SBAPI.generateCuboid(world, i + 3, j + 9, k + 2, i - 2, j + 9, k - 2, Blocks.NETHER_BRICK);
			world.setBlockState(new BlockPos(i + 3, j + 9, k + 2), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i + 3, j + 9, k - 2), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i - 2, j + 9, k - 2), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i - 2, j + 9, k + 2), Blocks.AIR.getDefaultState(), 3);
			SBAPI.generateCuboid(world, i + 2, j + 9, k + 1, i - 1, j + 9, k - 1, Blocks.GLOWSTONE);
			//Top level of roof
			SBAPI.generateCuboid(world, i + 2, j + 10, k + 1, i - 1, j + 10, k - 1, Blocks.NETHER_BRICK);
		} else {
			//Base
			SBAPI.generateCuboid(world, i, j + 1, k + 1, i, j + 1, k, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 1, j + 2, k + 2, i - 1, j + 2, k - 1, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 3, k + 3, i - 2, j + 3, k - 2, Blocks.NETHER_BRICK);
			//Portal
			SBAPI.generateCuboid(world, i, j + 3, k + 2, i, j + 7, k - 1, Blocks.OBSIDIAN, 0, true);
			//Columns
			SBAPI.generateCuboid(world, i - 2, j + 4, k + 3, i - 2, j + 8, k + 3, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i - 2, j + 4, k - 2, i - 2, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 4, k - 2, i + 2, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 4, k + 3, i + 2, j + 8, k + 3, Blocks.NETHER_BRICK);
			//First level of roof
			SBAPI.generateCuboid(world, i + 3, j + 8, k + 3, i + 3, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i - 3, j + 8, k + 3, i - 3, j + 8, k - 2, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 8, k + 4, i - 2, j + 8, k + 4, Blocks.NETHER_BRICK);
			SBAPI.generateCuboid(world, i + 2, j + 8, k - 3, i - 2, j + 8, k - 3, Blocks.NETHER_BRICK);
			//Second level of roof
			SBAPI.generateCuboid(world, i + 2, j + 9, k + 3, i - 2, j + 9, k - 2, Blocks.NETHER_BRICK);
			world.setBlockState(new BlockPos(i - 2, j + 9, k + 3), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i - 2, j + 9, k - 2), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i + 2, j + 9, k - 2), Blocks.AIR.getDefaultState(), 3);
			world.setBlockState(new BlockPos(i + 2, j + 9, k + 3), Blocks.AIR.getDefaultState(), 3);
			SBAPI.generateCuboid(world, i + 1, j + 9, k + 2, i - 1, j + 9, k - 1, Blocks.GLOWSTONE);
			//Top level of roof
			SBAPI.generateCuboid(world, i + 1, j + 10, k + 2, i - 1, j + 10, k - 1, Blocks.NETHER_BRICK);
		}
		world.setBlockState(new BlockPos(i, j + 4, k), Blocks.FIRE.getDefaultState(), 3);
	}

}