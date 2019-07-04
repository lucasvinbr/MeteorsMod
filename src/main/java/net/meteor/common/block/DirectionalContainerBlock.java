package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class DirectionalContainerBlock extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public DirectionalContainerBlock() {
        super(Material.ROCK);
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public abstract TileEntity createNewTileEntity(World world, int meta);

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX,
        float hitY, float hitZ) {
        player.openGui(MeteorsMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @SuppressWarnings("NullableProblems")
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
        EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing enumfacing = placer.getHorizontalFacing().getOpposite();
        state = state.withProperty(FACING, enumfacing);
        worldIn.setBlockState(pos, state, Constants.BlockFlags.DEFAULT);

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

}
