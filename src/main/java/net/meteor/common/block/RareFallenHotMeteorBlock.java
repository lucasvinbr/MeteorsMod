package net.meteor.common.block;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class RareFallenHotMeteorBlock extends HotMeteorBlock {

    public RareFallenHotMeteorBlock() {
        super();
    }

    @Override
    protected IBlockState getCooledBlockState() {
        return MeteorBlocks.RARE_METEOR.getDefaultState();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MeteorItems.itemRedMeteorGem;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return MathHelper.getInt(MeteorsMod.RANDOM, 2, 5);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 1 + random.nextInt(fortune + state.getValue(HEAT));
    }
}