package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

}
