package net.meteor.common.block;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import javax.annotation.Nonnull;

public class MeteorBlock extends Block {

    public MeteorBlock() {
        super(Material.ROCK);
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel(MeteorConstants.PICKAXE_TOOL_ID, 2);
        this.setHardness(10F)
            .setResistance(200F)
            .setLightLevel(0.5F);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(IBlockState state) {
        return 2;
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB AABB = new AxisAlignedBB(pos);
        return AABB.contract(0.006D, 0.006D, 0.006D);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random random) {

    }

    @Override
    public int tickRate(World world) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MeteorItems.itemMeteorChips;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, pos, state, player);
        if ((!world.isRemote) && (world.rand.nextInt(100) > 95)) {
            performHarvestEffects(world, pos, state, player);
        }
    }

    protected void performHarvestEffects(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        EntityAlienCreeper creeper = new EntityAlienCreeper(world);
        creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
        if (world.spawnEntity(creeper)) {
            creeper.spawnExplosionParticle();
        }
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return random.nextInt(1 + random.nextInt(fortune + 1));
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return MathHelper.getInt(MeteorsMod.RANDOM, 2, 5);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn); // TODO - maybe add potion effects like some sort of radiation?
    }
}