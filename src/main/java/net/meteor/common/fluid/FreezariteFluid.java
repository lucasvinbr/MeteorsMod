package net.meteor.common.fluid;

import net.meteor.common.MeteorBlocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.Color;

public class FreezariteFluid extends Fluid {

    public static FreezariteFluid getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final FreezariteFluid INSTANCE = new FreezariteFluid();
    }

    FreezariteFluid() {
        super("freezarite_fluid", new ResourceLocation("meteorsmod:blocks/freezarite"), new ResourceLocation("meteorsmod:blocks/freezairte_flowing"));
        this.setViscosity(5000); //
        this.setDensity(2000);
        this.setTemperature(5); // This is in kelvin, default temperature is 300K
        this.setBlock(MeteorBlocks.LIQUID_FREEZARITE);
        this.setRarity(EnumRarity.COMMON);
        this.setColor(Color.CYAN.brighter());
        this.setLuminosity(1);
    }
}
