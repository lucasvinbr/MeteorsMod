package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFrezarite extends BlockMeteorsMod
{
	
	private Random rand = new Random();
	
	public BlockFrezarite()
	{
		super(Material.ROCK);
		this.slipperiness = 0.98F;
		setTickRandomly(true);
		this.setHarvestLevel("pickaxe", 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return MeteorItems.itemFrezaCrystal;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return random.nextInt(1 + meta + random.nextInt(fortune + 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random random)
	{
		if (world.getBlockMetadata(x, y, z) > 0) {
			renderGlowParticles(world, pos.getX(), pos.getY(), pos.getZ(), random);
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderGlowParticles(World world, int x, int y, int z, Random random)
	{
		double d = 0.0625D;
		for (int l = 0; l < 6; l++)
		{
			double d1 = (float)x + random.nextFloat();
			double d2 = (float)y + random.nextFloat();
			double d3 = (float)z + random.nextFloat();
			if ((l == 0) && !world.getBlockState(new BlockPos(x, y + 1, z)).isOpaqueCube())
			{
				d2 = y + 1 + d;
			}
			if ((l == 1) && !world.getBlockState(new BlockPos(x, y - 1, z)).isOpaqueCube())
			{
				d2 = y - d;
			}
			if ((l == 2) && !world.getBlockState(new BlockPos(x, y, z + 1)).isOpaqueCube())
			{
				d3 = z + 1 + d;
			}
			if ((l == 3) && !world.getBlockState(new BlockPos(x, y, z - 1)).isOpaqueCube())
			{
				d3 = z - d;
			}
			if ((l == 4) && !world.getBlockState(new BlockPos(x + 1, y, z)).isOpaqueCube())
			{
				d1 = x + 1 + d;
			}
			if ((l == 5) && !world.getBlockState(new BlockPos(x - 1, y, z)).isOpaqueCube())
			{
				d1 = x - d;
			}
			if ((d1 < x) || (d1 > x + 1) || (d2 < 0.0D) || (d2 > y + 1) || (d3 < z) || (d3 > z + 1))
			{
				ClientProxy.spawnParticle("frezadust", d1, d2, d3, 0.0D, 0.0D, 0.0D, world, -1);
			}
		}
	}

	@Override
	public int tickRate(World world)
	{
		return 30;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float temp = world.getBiomeForCoordsBody(pos).getTemperature(pos);
		if ((meta > 0) && (temp > 0.15F)) {
			world.setBlockMetadataWithNotify(x, y, z, --meta, 2);
			if (meta <= 0) {
				world.setBlockState(pos, Blocks.WATER.getDefaultState());
				world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), new SoundEvent(new ResourceLocation("minecraft:random.glass")), SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		if (metadata > 0) {
			return MathHelper.getInt(rand, 2, 5);
		}
		return MathHelper.getInt(rand, 0, 2);
	}
	
}