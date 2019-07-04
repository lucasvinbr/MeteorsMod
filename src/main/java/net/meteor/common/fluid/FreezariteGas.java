package net.meteor.common.fluid;

import net.meteor.common.MeteorBlocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.Color;

public class FreezariteGas extends Fluid {

    public static FreezariteGas getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final FreezariteGas INSTANCE = new FreezariteGas();
    }

    FreezariteGas() {
        super("freezarite_fluid", new ResourceLocation("meteorsmod:blocks/freezarite"), new ResourceLocation("meteorsmod:blocks/freezairte_flowing"));
        this.setViscosity(400); //
        this.setDensity(100);
        this.setTemperature(100); // This is in kelvin, default temperature is 300K, we're saying the "solid" is at sub 5k, liquid is at 5k-100k, gas at 100k
        this.setBlock(MeteorBlocks.FREEZARITE_GAS);
        this.setRarity(EnumRarity.COMMON);
        this.setColor(Color.CYAN.brighter().brighter().brighter());
        this.setLuminosity(0);
        this.setGaseous(true);
    }
}
