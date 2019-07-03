package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockContainerMeteorsMod extends BlockContainer {

    protected BlockContainerMeteorsMod(Material par2Material) {
        super(par2Material);
        this.setCreativeTab(MeteorsMod.meteorTab);
    }

}
