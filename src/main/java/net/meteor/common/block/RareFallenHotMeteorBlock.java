package net.meteor.common.block;

import net.meteor.common.MeteorBlocks;
import net.minecraft.block.state.IBlockState;

import java.util.Random;

public class RareFallenHotMeteorBlock extends HotMeteorBlock {

    public RareFallenHotMeteorBlock() {
        super();
    }

    @Override
    protected IBlockState getCooledBlockState() {
        return MeteorBlocks.blockRareMeteor.getDefaultState();
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 1 + random.nextInt(fortune + state.getValue(HEAT));
    }
}