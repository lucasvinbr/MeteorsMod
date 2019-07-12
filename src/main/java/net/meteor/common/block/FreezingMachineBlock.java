package net.meteor.common.block;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorBlocks;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class FreezingMachineBlock extends DirectionalContainerBlock {

    final boolean isFreezing;

    public FreezingMachineBlock(boolean isFreezing) {
        setSoundType(SoundType.METAL);
        this.isFreezing = isFreezing;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFreezingMachine();
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (active)
        {
            worldIn.setBlockState(pos, MeteorBlocks.FREEZER_FREEZING.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, MeteorBlocks.FREEZER_FREEZING.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, MeteorBlocks.FREEZER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, MeteorBlocks.FREEZER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = stateIn.getValue(FACING);
        if (this.isFreezing)
        {
            float d0 = (float)pos.getX() + 0.5F;
            float d1 = (float)pos.getY() + 0.0625F + rand.nextFloat() * 14.0F / 16.0F;
            float d2 = (float)pos.getZ() + 0.5F;
            float d4 = rand.nextFloat() * 0.6F - 0.3F;

            switch (enumfacing)
            {
                case WEST:
                    ClientProxy.spawnParticle("frezadust", d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, worldIn, -1);
                    break;
                case EAST:
                    ClientProxy.spawnParticle("frezadust", d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, worldIn, -1);
                    break;
                case NORTH:
                    ClientProxy.spawnParticle("frezadust", d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D, worldIn, -1);
                    break;
                case SOUTH:
                    ClientProxy.spawnParticle("frezadust", d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D, worldIn, -1);
            }
        }
    }

}
