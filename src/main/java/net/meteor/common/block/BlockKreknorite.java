package net.meteor.common.block;

import net.meteor.common.MeteorItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockKreknorite extends BlockMeteor
{
	public BlockKreknorite()
	{
		super();
		
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return MeteorItems.itemKreknoChip;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		if ((!world.isRemote) && (world.rand.nextInt(100) == 95)) {
			EntityBlaze blaze = new EntityBlaze(world);
			blaze.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
			world.spawnEntity(blaze);
			blaze.spawnExplosionParticle();
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 0) {
			world.setBlockMetadataWithNotify(x, y, z, --meta, 2);
			if (meta <= 0) {
				world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState(), 2);
				triggerLavaMixEffects(world, pos.getX(), pos.getY(), pos.getZ());
			} else {
				checkForHarden(world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		checkForHarden(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		checkForHarden((World) world, pos.getX(), pos.getY(), pos.getZ());
	}

	private void checkForHarden(World world, int x, int y, int z)
	{
		if (world.getBlockState(new BlockPos(x, y, z)).getBlock() != this)
		{
			return;
		}
		boolean flag = false;//TODO 1.12.2 looks broken
		if (flag || world.getBlockState(new BlockPos(x, y, z - 1)).getMaterial() == Material.WATER)
        {
            flag = true;
        }

        if (flag || world.getBlockState(new BlockPos(x, y, z + 1)).getMaterial() == Material.WATER)
        {
            flag = true;
        }

        if (flag || world.getBlockState(new BlockPos(x - 1, y, z)).getMaterial() == Material.WATER)
        {
            flag = true;
        }

        if (flag || world.getBlockState(new BlockPos(x + 1, y, z)).getMaterial() == Material.WATER)
        {
            flag = true;
        }

        if (flag || world.getBlockState(new BlockPos(x, y + 1, z)).getMaterial() == Material.WATER)
        {
            flag = true;
        }
		if (flag)
		{
			world.setBlockState(new BlockPos(x, y, z), Blocks.OBSIDIAN.getDefaultState(), 2);
			triggerLavaMixEffects(world, x, y, z);
		}
	}

	@Override
	protected void triggerLavaMixEffects(World world, int i, int j, int z)
	{
		world.playSound(i + 0.5F, j + 0.5F, z + 0.5F, new SoundEvent(new ResourceLocation("minecraft:random.fizz")), SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, true);
		for (int l = 0; l < 8; l++)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + 1.2D, z + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int i, int j)
	{
		return this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("meteors:Kreknorite");
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return MathHelper.getInt(rand, 3, 6);
	}
	
}