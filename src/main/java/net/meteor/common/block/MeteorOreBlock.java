package net.meteor.common.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class MeteorOreBlock extends MeteorsBaseBlock {

    private final Item droppedItem;

    public MeteorOreBlock(Item droppedItem) {
        super(Material.IRON);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(10F).setResistance(200F);
        this.setSoundType(SoundType.STONE);
        this.droppedItem = droppedItem;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.droppedItem;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 1 + random.nextInt(fortune + 1);
    }

}