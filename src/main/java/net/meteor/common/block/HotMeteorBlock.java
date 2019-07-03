package net.meteor.common.block;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.Random;

import javax.annotation.Nonnull;

public class HotMeteorBlock extends MeteorBlock {

    public static final PropertyInteger HEAT = PropertyInteger.create("heat_level", 0, MeteorConstants.MAX_METEOR_HEAT);

    public HotMeteorBlock() {
        super();
        this.setTickRandomly(true);
        this.setHarvestLevel(MeteorConstants.PICKAXE_TOOL_ID, 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HEAT, 0));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HEAT);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return Math.max(2, Math.min(state.getValue(HEAT) * 2, MeteorConstants.MAXIMUM_LIGHT_VALUE));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        final IBlockState newState = isWaterAround(world, pos) || state.getValue(HEAT) == 0
                                     ? getCooledBlockState()
                                     : state.withProperty(HEAT, Math.max(0, state.getValue(HEAT) - 1));
        world.setBlockState(pos, newState, Constants.BlockFlags.SEND_TO_CLIENTS);
        triggerLavaMixEffects(world, pos);
    }

    @Override
    public int tickRate(World world) {
        return 30;
    }

    @Override
    protected void performHarvestEffects(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.performHarvestEffects(world, pos, state, player);
        lightPlayerOnFire(state, player);
    }

    void lightPlayerOnFire(IBlockState state, EntityPlayer player) {
        if (player.isImmuneToFire()) {
            return;
        }
        final int heatLevel = state.getValue(HEAT);

        if (RANDOM.nextInt(100) < (RANDOM.nextInt(heatLevel) / MeteorConstants.MAX_METEOR_HEAT) * 100) {
            player.setFire(1 + RANDOM.nextInt(heatLevel));
        }
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP || world.getBlockState(pos).getValue(HEAT) > 4;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        if ((random.nextInt(4) == 0)) {
            return 1 + random.nextInt(fortune + 1);
        }
        return random.nextInt(1 + random.nextInt(fortune + 1));
    }

    private boolean isWaterAround(World world, BlockPos pos) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        for (int sZ = z + 1; sZ >= z - 1; sZ--) {
            for (int sY = y + 1; sY >= y - 1; sY--) {
                for (int sX = x + 1; sX >= x - 1; sX--) {
                    Block block = world.getBlockState(new BlockPos(sX, sY, sZ)).getBlock();
                    if (block instanceof IFluidBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected IBlockState getCooledBlockState() {
        return MeteorBlocks.blockMeteor.getDefaultState();
    }

    private void triggerLavaMixEffects(World world, BlockPos pos) {
        double d0 = (double) pos.getX();
        double d1 = (double) pos.getY();
        double d2 = (double) pos.getZ();
        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
            2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote) {
            return;
        }
        if (blockIn instanceof IFluidBlock) {
            worldIn.setBlockState(pos, getCooledBlockState(), Constants.BlockFlags.SEND_TO_CLIENTS);
            triggerLavaMixEffects(worldIn, pos);
        }
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.isRemote) {
            return;
        }
        final IBlockState blockState = worldIn.getBlockState(pos);
        if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entityIn)) {
            // the hotter it is, the more damage
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F * blockState.getValue(HEAT) + 1);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

}