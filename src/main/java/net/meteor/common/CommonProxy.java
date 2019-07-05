package net.meteor.common;

import java.util.HashMap;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public HashMap<Integer, HandlerMeteor> metHandlers = new HashMap<>();

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMeteorShield.class, new ResourceLocation(MeteorsMod.MOD_ID,"TileEntityMeteorShield"));
		GameRegistry.registerTileEntity(TileEntityMeteorTimer.class, new ResourceLocation(MeteorsMod.MOD_ID,"TileEntityMeteorTimer"));
		GameRegistry.registerTileEntity(TileEntityFreezingMachine.class, new ResourceLocation(MeteorsMod.MOD_ID,"TileEntityIceMaker"));
		GameRegistry.registerTileEntity(TileEntitySlippery.class, new ResourceLocation(MeteorsMod.MOD_ID,"TileEntitySlippery"));
	}

	public void loadStuff() {}
	
	public void preInit() {}

}