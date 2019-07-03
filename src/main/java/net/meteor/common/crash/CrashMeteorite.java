package net.meteor.common.crash;

import java.util.List;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.ExplosionMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CrashMeteorite extends WorldGenerator
{
	protected int crashSize;
	protected ExplosionMeteor explosion;
	protected EnumMeteor meteorType;

	public CrashMeteorite(int Size, ExplosionMeteor expl, EnumMeteor metType)
	{
		this.crashSize = MathHelper.clamp(Size, 1, 3);
		this.explosion = expl;
		this.meteorType = metType;
	}

	@Override
    public boolean generate(World world, Random random, BlockPos blockPos)
	{
		Block meteor = this.meteorType.getMaterial();
		Block rareMeteor = this.meteorType.getRareMaterial();

		//Initial spreading
		for (int y = blockPos.getY() + MeteorsMod.instance.ImpactSpread * this.crashSize; y >= blockPos.getY() - MeteorsMod.instance.ImpactSpread * this.crashSize; y--) {
			for (int startX = blockPos.getX() + MeteorsMod.instance.ImpactSpread * this.crashSize; startX >= blockPos.getX() - MeteorsMod.instance.ImpactSpread * this.crashSize; startX--) {
				for (int startZ = blockPos.getZ() + MeteorsMod.instance.ImpactSpread * this.crashSize; startZ >= blockPos.getZ() - MeteorsMod.instance.ImpactSpread * this.crashSize; startZ--) {
					if ((!world.isAirBlock(new BlockPos(startX, y, startZ))) && (meteorsAboveAndBelow(world, startX, y, startZ) == 0) && (random.nextInt(10) + 1 > 7) && (checkBlockIDs(world, startX, y, startZ))) {
						Block theBlock = random.nextInt(45) == 25 ? rareMeteor : meteor;
						if (Block.isEqualTo(theBlock, Blocks.ICE) || Block.isEqualTo(theBlock, Blocks.LAVA)) {
							world.setBlockState(new BlockPos(startX, y, startZ), theBlock.getDefaultState(), 2);
						} else {
							//1.12.2 TODO random.nextInt(4) + 1 meta
							world.setBlockState(new BlockPos(startX, y, startZ), theBlock.getDefaultState(), 3);
						}
					}
				}
			}

		}

		//Bottom layer spreading for a higher concentration in center
		for (int y = blockPos.getY() - MeteorsMod.instance.ImpactSpread * this.crashSize; y >= blockPos.getY() - (MeteorsMod.instance.ImpactSpread * this.crashSize + 1); y--) {
			for (int startX = blockPos.getX() + MeteorsMod.instance.ImpactSpread * this.crashSize; startX >= blockPos.getX() - MeteorsMod.instance.ImpactSpread * this.crashSize; startX--) {
				for (int startZ = blockPos.getZ() + MeteorsMod.instance.ImpactSpread * this.crashSize; startZ >= blockPos.getZ() - MeteorsMod.instance.ImpactSpread * this.crashSize; startZ--) {
					if ((!world.isAirBlock(new BlockPos(startX, y, startZ))) && (meteorsAboveAndBelow(world, startX, y, startZ) == 0) && (checkBlockIDs(world, startX, y, startZ))) {
						Block theBlock = random.nextInt(45) == 25 ? rareMeteor : meteor;
						if (Block.isEqualTo(theBlock, Blocks.ICE) || Block.isEqualTo(theBlock, Blocks.LAVA)) {
							world.setBlockState(new BlockPos(startX, y, startZ), theBlock.getDefaultState(), 2);
						} else {
							//1.12.2 TODO random.nextInt(4) + 1 meta
							world.setBlockState(new BlockPos(startX, y, startZ), theBlock.getDefaultState(), 3);
						}
					}
				}

			}

		}

		afterCraterFormed(world, random, blockPos.getX(), blockPos.getY(), blockPos.getZ());//1.12.2 TODO change to blockpos

		return true;
	}

	public void afterCraterFormed(World world, Random random, int xIn, int yIn, int zIn)
	{
		int aliencreepers = random.nextInt(5);
		List<BlockPos> affectedBlockPositions = this.explosion.getAffectedBlockPositions();
		for (int j1 = affectedBlockPositions.size() - 1; (j1 >= 0) && (aliencreepers > 0); j1--)
		{
			BlockPos blockPos = affectedBlockPositions.get(j1);
			int x = blockPos.getX();
			int y = blockPos.getY();
			int z = blockPos.getZ();
			boolean isAir = world.isAirBlock(blockPos);
			IBlockState k2 = world.getBlockState(blockPos.down());
			if (isAir && k2.isOpaqueCube() && (random.nextInt(10) > 4)) {
				EntityAlienCreeper creeper = new EntityAlienCreeper(world);
				creeper.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
				world.spawnEntity(creeper);
				aliencreepers--;
			}
		}
	}

	public void afterCrashCompleted(World world, int i, int j, int k) {}

	public int meteorsAbove(World world, int x, int y, int z) { int ceiling = y + 6 * this.crashSize;
	int mAbove = 0;
	for (int y1 = y; y1 <= ceiling; y1++) {
		IBlockState block = world.getBlockState(new BlockPos(x, y1, z));
		if (Block.isEqualTo(block.getBlock(), this.meteorType.getMaterial()) || Block.isEqualTo(block.getBlock(), this.meteorType.getRareMaterial())) {
			mAbove++;
		}
	}
	return mAbove; }

	public int meteorsBelow(World world, int x, int y, int z)
	{
		int floor = y - 6 * this.crashSize;
		int mBelow = 0;
		for (int y1 = y; y1 >= floor; y1--) {
			IBlockState block = world.getBlockState(new BlockPos(x, y1, z));
			if (Block.isEqualTo(block.getBlock(), this.meteorType.getMaterial()) || Block.isEqualTo(block.getBlock(), this.meteorType.getRareMaterial())) {
				mBelow++;
			}
		}
		return mBelow;
	}

	public int meteorsAboveAndBelow(World world, int x, int y, int z) {
		IBlockState bl = world.getBlockState(new BlockPos(x, y, z));
		return 0 - (Block.isEqualTo(bl.getBlock(), meteorType.getRareMaterial()) ? 1 : meteorsAbove(world, x, y, z) + meteorsBelow(world, x, y, z) - (Block.isEqualTo(bl.getBlock(), meteorType.getMaterial()) ? 1 : 0));
	}

	protected boolean checkBlockIDs(World world, int i, int j, int k) {
		IBlockState bl = world.getBlockState(new BlockPos(i, j, k));
		return (!Block.isEqualTo(bl.getBlock(), Blocks.BEDROCK) && !Block.isEqualTo(bl.getBlock(), meteorType.getMaterial()) &&
				!Block.isEqualTo(bl.getBlock(), meteorType.getRareMaterial()) && !Block.isEqualTo(bl.getBlock(), Blocks.FLOWING_WATER) &&
				!Block.isEqualTo(bl.getBlock(), Blocks.WATER));
	}
}