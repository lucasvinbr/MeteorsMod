package net.meteor.common.crash;

import java.util.List;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.ExplosionMeteor;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CrashFrezarite extends CrashMeteorite
{
	public CrashFrezarite(int Size, ExplosionMeteor expl, EnumMeteor metType)
	{
		super(Size, expl, metType);
	}

	@Override
    public void afterCraterFormed(World world, Random random, int xIn, int yIn, int zIn) {
		List<BlockPos> arraylist = this.explosion.getAffectedBlockPositions();
		for (int j1 = arraylist.size() - 1; j1 >= 0; j1--) {
			BlockPos blockPos = arraylist.get(j1);
			int x = blockPos.getX();
			int y = blockPos.getY();
			int z = blockPos.getZ();
			BlockPos pos = new BlockPos(x, y, z);
			boolean j2 = world.isAirBlock(pos);
			IBlockState block = world.getBlockState(pos.down());
			if (j2 && block.isOpaqueCube() && (random.nextInt(2) == 0)) {
				world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, MathHelper.getInt(random, 1,5)), 2);
			}
		}
	}
}