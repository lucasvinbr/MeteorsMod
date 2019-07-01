package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMeteorShield extends BlockContainerMeteorsMod
{
	
	public BlockMeteorShield()
	{
		super(Material.ROCK);
		this.setLightOpacity(0);
		this.setBlockBounds(0.0625F, 0.375F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if ((placer instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer)placer;
			if (!worldIn.isRemote) {
				player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.charging"), TextFormatting.YELLOW));
			}
			TileEntityMeteorShield shield = (TileEntityMeteorShield) worldIn.getTileEntity(pos);
			shield.owner = player.getName();
			worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), new SoundEvent(new ResourceLocation("meteors:shield.humm")), SoundCategory.BLOCKS, 1.0F, 1.0F, true);
		}
		
		int l = MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
        	worldIn.setBlockMetadataWithNotify(x, y, z, 1, 2);
        }

        if (l == 1)
        {
        	worldIn.setBlockMetadataWithNotify(x, y, z, 0, 2);
        }

        if (l == 2)
        {
        	worldIn.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3)
        {
        	worldIn.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        
        super.onBlockPlacedBy(worldIn, x, y, z, placer, stack);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityMeteorShield shield = (TileEntityMeteorShield)world.getTileEntity(pos);
		if (!world.isRemote) {
			if (MeteorsMod.proxy.metHandlers.get(world.provider.getDimension()).getShieldManager().meteorShields.remove(shield)) {
				//MeteorsMod.log.info("METEOR SHIELD SHOULD BE REMOVED");
			}
			world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new SoundEvent(new ResourceLocation("meteors:shield.powerdown")), SoundCategory.BLOCKS, 1.0F, 1.0F, true);
		}
		
		if (shield != null)
        {
            for (int i1 = 0; i1 < shield.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = shield.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0; world.spawnEntity(entityitem))
                    {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.getCount())
                        {
                            j1 = itemstack.getCount();
                        }

                        itemstack.shrink(j1);
                        entityitem = new EntityItem(world, (double)((float)pos.getX() + f), (double)((float)pos.getY() + f1), (double)((float)pos.getZ() + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                        }
                    }
                }
            }
            shield.invalidate();
        }
		
		super.breakBlock(world, pos, state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random random)
	{
		if (MeteorsMod.instance.meteorShieldSound && random.nextInt(256) == 100) {
			world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new SoundEvent(new ResourceLocation("meteors:shield.humm")), SoundCategory.BLOCKS, 0.6F, 1.0F, false);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		player.openGui(MeteorsMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMeteorShield();
	}
	
	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, REDSTONE wire, etc to this block.
     */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType() {
        return -1;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
}