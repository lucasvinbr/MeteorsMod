package net.meteor.common.block;

import net.meteor.common.MeteorsMod;
import net.meteor.common.fluid.FreezariteFluid;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class LiquidFreezariteBlock extends BlockFluidClassic {

    public static final Material FREEZARITE_LIQUID_MATERIAL = new MaterialLiquid(MapColor.CYAN);

    public LiquidFreezariteBlock() {
        super(FreezariteFluid.getInstance(), FREEZARITE_LIQUID_MATERIAL);
        this.setRegistryName("freezarite_liquid");
        this.setTranslationKey("freezarite_liquid");
        this.setCreativeTab(MeteorsMod.meteorTab);
        this.setQuantaPerBlock(4);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.motionX *= (double)(1.0F - this.getQuantaPercentage(world, pos) / 2.0F);
        entity.motionZ *= (double)(1.0F - this.getQuantaPercentage(world, pos) / 2.0F);
        if (!world.isRemote && entity instanceof EntityLivingBase) {
            // TODO - apply freezing attribute/potion effect
            entity.attackEntityFrom(MeteorConstants.DamageSources.FROZEN_FLOOR,  (float)(4 - this.getMetaFromState(state) + 1));
        }
    }


}
