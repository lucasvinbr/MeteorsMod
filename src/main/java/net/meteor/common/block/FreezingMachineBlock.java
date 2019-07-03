package net.meteor.common.block;

import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FreezingMachineBlock extends DirectionalContainerBlock {

    public FreezingMachineBlock() {
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFreezingMachine();
    }

}
