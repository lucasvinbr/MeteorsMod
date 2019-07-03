package net.meteor.common.block;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDecoration extends MeteorsBaseBlock {
	
	private String[] blockTextureNames;
	//private IIcon[] icons;

	public BlockDecoration(String... textures) {
		super(Material.IRON);
		this.blockTextureNames = textures;
	}

	//TODO 1.12.2
	/*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
		return meta >= this.icons.length ? null : this.icons[meta];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
		icons = new IIcon[blockTextureNames.length];
		for (int i = 0; i < blockTextureNames.length; i++) {
			icons[i] = reg.registerIcon(MeteorsMod.MOD_ID + ":" + blockTextureNames[i]);
		}
    }*/
	
	/**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
    public int damageDropped(IBlockState p_149692_1_)
    {//TODO 1.12.2
        return 0;
    }

    //TODO 1.12.2
/*	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        for (int i = 0; i < 3; ++i)
        {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
	*/

	@Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
    {
        return true;
    }

}
