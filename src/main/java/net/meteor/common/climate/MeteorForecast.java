package net.meteor.common.climate;

import java.util.ArrayList;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.packets.PacketSoonestMeteor;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MeteorForecast {
	
	private World theWorld;
	private ClimateUpdater worldTick;
	private ArrayList<GhostMeteor> ghostMets;
	private CrashLocation lastCrash = null;
	
	public MeteorForecast(ClimateUpdater wTick, ArrayList<GhostMeteor> gMets, CrashLocation lCrash, World world) {
		this.worldTick = wTick;
		this.ghostMets = gMets;
		this.lastCrash = lCrash;
		this.theWorld = world;
	}
	
	public void setLastCrashLocation(CrashLocation loc) {
		this.lastCrash = loc;
	}
	
	public CrashLocation getLastCrashLocation() {
		return this.lastCrash;
	}
	
	public int getSecondsUntilNewMeteor() {
		return worldTick.getSecondsUntilNewMeteor();
	}
	
	public GhostMeteor getNearestTimeMeteor() {
		if (this.theWorld == null) return null;
		GhostMeteor closestMeteor = null;
        for (GhostMeteor ghostMet : this.ghostMets) {
            if (closestMeteor != null) {
                if (ghostMet.type != EnumMeteor.KITTY) {
                    int var1 = closestMeteor.getRemainingTicks();
                    int var2 = ghostMet.getRemainingTicks();
                    if (var2 < var1)
                        closestMeteor = ghostMet;
                }
            } else if (ghostMet.type != EnumMeteor.KITTY) {
                closestMeteor = ghostMet;
            }

        }

		return closestMeteor;
	}
	
	public void updateNearestTimeForClients() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = getNearestTimeMeteor();
			MeteorsMod.network.sendToDimension(new PacketSoonestMeteor(gMeteor), theWorld.provider.getDimension());
		}
	}
	
	public void clearMeteors() {
		ghostMets.clear();
	}

}
