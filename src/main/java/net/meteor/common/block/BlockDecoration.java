package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDecoration extends Block {

	public BlockDecoration(String textures) {
		super(Material.IRON);
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setRegistryName(textures);
		this.setHardness(5.0F).setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
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
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

}
