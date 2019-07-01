package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorItems;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMeteor extends BlockMeteorsMod
{
	
	protected IIcon hotTex;
	protected Random rand = new Random();
	
	public BlockMeteor()
	{
		super(Material.ROCK);
		this.setTickRandomly(true);
		this.setHarvestLevel("pickaxe", 2);
		this.setHarvestLevel("pickaxe", 1, 0);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return MeteorItems.itemMeteorChips;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if ((meta == 0) && (random.nextInt(4) == 0)) {
			return 1 + random.nextInt(fortune + 1);
		}
		return random.nextInt(1 + meta + random.nextInt(fortune + 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random random)
	{
		if (worldIn.getBlockMetadata(xIn, yIn, zIn) > 0) {
			if (random.nextInt(32) == 0)
	        {
	            worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), new SoundEvent(new ResourceLocation("minecraft:fire.fire")), SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
	        }
			renderGlowParticles(worldIn, pos.getX(), pos.getY(), pos.getZ(), random);
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderGlowParticles(World world, int xIn, int yIn, int zIn, Random random)
	{
		double d = 0.0625D;
		for (int l = 0; l < 6; l++)
		{
			double d1 = xIn + random.nextFloat();
			double d2 = yIn + random.nextFloat();
			double d3 = zIn + random.nextFloat();
			if ((l == 0) && !world.getBlockState(new BlockPos(xIn, yIn + 1, zIn)).isOpaqueCube())
			{
				d2 = yIn + 1 + d;
			}
			if ((l == 1) && !world.getBlockState(new BlockPos(xIn, yIn - 1, zIn)).isOpaqueCube())
			{
				d2 = yIn - d;
			}
			if ((l == 2) && !world.getBlockState(new BlockPos(xIn, yIn, zIn + 1)).isOpaqueCube())
			{
				d3 = zIn + 1 + d;
			}
			if ((l == 3) && !world.getBlockState(new BlockPos(xIn, yIn, zIn - 1)).isOpaqueCube())
			{
				d3 = zIn - d;
			}
			if ((l == 4) && !world.getBlockState(new BlockPos(xIn + 1, yIn, zIn)).isOpaqueCube())
			{
				d1 = xIn + 1 + d;
			}
			if ((l == 5) && !world.getBlockState(new BlockPos(xIn - 1, yIn, zIn)).isOpaqueCube())
			{
				d1 = xIn - d;
			}
			if ((d1 < xIn) || (d1 > xIn + 1) || (d2 < 0.0D) || (d2 > yIn + 1) || (d3 < zIn) || (d3 > zIn + 1))
			{
				ClientProxy.spawnParticle("meteordust", d1, d2, d3, 0.0D, 0.0D, 0.0D, world, -1);
			}
		}
	}

	@Override
	public int tickRate(World world)
	{
		return 30;
	}

	@SuppressWarnings({"deprecation"})
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		AxisAlignedBB AABB = new AxisAlignedBB(pos);
		return AABB.contract(0.006D, 0.006D, 0.006D);
	}

	@Override
	public boolean isBurning(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockMetadata(x, y, z) > 0;
	}
	
	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
		int metadata = world.getBlockMetadata(x, y, z);
		if ((side == EnumFacing.UP) && (metadata > 0)) {
			return true;
		}
		return super.isFireSource(world, pos, side);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 0) {
			if (isWaterAround(world, x, y, z)) {
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				triggerLavaMixEffects(world, x, y, z);
			} else {
				world.setBlockMetadataWithNotify(x, y, z, --meta, 2);
				if (meta == 0) {
					triggerLavaMixEffects(world, x, y, z);
				}
			}
		}
	}
	
	protected void triggerLavaMixEffects(World world, int x, int y, int z)
	{
		world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, new SoundEvent(new ResourceLocation("minecraft:random.fizz")), SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
		for (int l = 0; l < 8; l++)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + 1.2D, z + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}

	private boolean isWaterAround(World world, int x, int y, int z) {
		for (int sY = y + 1; sY >= y - 1; sY--) {
			for (int sX = x + 1; sX >= x - 1; sX--) {
				for (int sZ = z + 1; sZ >= z - 1; sZ--) {
					Block block = world.getBlockState(new BlockPos(sX, sY, sZ)).getBlock();
					if (Block.isEqualTo(block, Blocks.WATER) || Block.isEqualTo(block, Blocks.FLOWING_WATER)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int i, int j)
	{
		if (j == 0) {
			super.getIcon(i, j);
			return this.blockIcon;
		}
		return this.hotTex;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("meteors:Meteor");
		this.hotTex = par1IconRegister.registerIcon("meteors:Meteor_Hot");
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		super.onBlockHarvested(world, pos, state, player);
		if ((!world.isRemote) && (world.rand.nextInt(100) == 95)) {
			EntityAlienCreeper creeper = new EntityAlienCreeper(world);
			creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
			world.spawnEntity(creeper);
			creeper.spawnExplosionParticle();
		}
	}

	@Override
	public int getLightValue(IBlockState state)
	{
		if (iba.getBlockMetadata(x, y, z) > 0) {
			return state.getLightValue();
		}
		return 0;
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return MathHelper.getInt(rand, 2, 5);
	}
	
}