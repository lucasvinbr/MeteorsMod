package net.meteor.common.block.propertys;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by TheGreyGhost on 20/04/2015.
 */
public class UnlistedPropertyCopiedBlock implements IUnlistedProperty<IBlockState>
{
    @Override
    public String getName() {
        return "UnlistedPropertyCopiedBlock";
    }

    @Override
    public boolean isValid(IBlockState value) {
        return true;
    }

    @Override
    public Class<IBlockState> getType() {
        return IBlockState.class;
    }

    @Override
    public String valueToString(IBlockState value) {
        return value.toString();
    }
}