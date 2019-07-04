package net.meteor.common.block;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.util.MeteorConstants;
import net.meteor.common.util.MeteorDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class FreezariteBlock extends Block {

    public static final PropertyInteger FROZEN_LEVEL = PropertyInteger.create("frozen_level", 0, MeteorConstants.MAX_METEOR_HEAT);
    private Random rand = new Random();

    public FreezariteBlock() {
        super(Material.ROCK);
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setDefaultSlipperiness(0.98F);
        setTickRandomly(true);
        this.setHardness(8.5F).setResistance(150F);
        this.setSoundType(SoundType.GLASS).setLightLevel(0.25F);
        this.setHarvestLevel(MeteorConstants.PICKAXE_TOOL_ID, 1);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        float temp = world.getBiomeForCoordsBody(pos).getTemperature(pos);
        final int currentFrozen = state.getValue(FROZEN_LEVEL);
        if (!(temp > 0.15F)) {
            if (currentFrozen < MeteorConstants.MAX_METEOR_HEAT) {
                if (world.rand.nextInt(100) < 10) {
                    world.setBlockState(pos, state.withProperty(FROZEN_LEVEL, currentFrozen + 1), Constants.BlockFlags.SEND_TO_CLIENTS);
                    // TODO - maybe throw a sound for "hardening"
                }
            }
            return;
        }
        final IBlockState newState = currentFrozen == 0
                                     ? getWarmedState()
                                     : state.withProperty(FROZEN_LEVEL, Math.max(0, currentFrozen - 1));
        world.setBlockState(pos, newState, Constants.BlockFlags.SEND_TO_CLIENTS);
        triggerFrozenMeltingEffects(world, pos);
    }

    protected IBlockState getWarmedState() {
        return Blocks.WATER.getDefaultState();
    }

    private void triggerFrozenMeltingEffects(World world, BlockPos pos) {
        double d0 = (double) pos.getX();
        double d1 = (double) pos.getY();
        double d2 = (double) pos.getZ();
        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
            2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
        world.playSound(null, (float) pos.getX() + 0.5F, (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F),
            SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, (this.blockSoundType.getVolume() + 1.0F + world.rand.nextFloat()) / 2.0F,
            this.blockSoundType.getPitch() * (world.rand.nextFloat() * 0.7F + 0.3F));

    }

    @Override
    public int tickRate(World world) {
        return 30;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MeteorItems.itemFrezaCrystal;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.isRemote) {
            return;
        }
        final IBlockState blockState = worldIn.getBlockState(pos);
        // TODO - Add Freeze absorption enchantment.
        entityIn.attackEntityFrom(MeteorDamageSources.FROZEN_FLOOR, 1.0F * (blockState.getValue(FROZEN_LEVEL) + 1));

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FROZEN_LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FROZEN_LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FROZEN_LEVEL);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return random.nextInt(1 + state.getValue(FROZEN_LEVEL) + random.nextInt(fortune + 1));
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        if (state.getValue(FROZEN_LEVEL) > 0) {
            return MathHelper.getInt(rand, 2, 5);
        }
        return MathHelper.getInt(rand, 0, 2);
    }

}