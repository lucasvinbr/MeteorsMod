package net.meteor.common.block;

import net.meteor.common.MeteorItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class KreknoriteBlock extends HotMeteorBlock {

    public KreknoriteBlock() {
        super();
        this.setHardness(11F).setResistance(350F).setLightLevel(0.7F);
    }

    @Override
    protected IBlockState getCooledBlockState() {
        return Blocks.OBSIDIAN.getDefaultState();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MeteorItems.itemKreknoChip;
    }

    @Override
    protected void performHarvestEffects(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        EntityBlaze blaze = new EntityBlaze(world);
        blaze.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
        world.spawnEntity(blaze);
        blaze.spawnExplosionParticle();
        lightPlayerOnFire(state, player);
    }
}