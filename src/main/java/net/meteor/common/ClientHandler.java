package net.meteor.common;

import java.util.ArrayList;

import net.meteor.common.climate.CrashLocation;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.packets.PacketBlockedMeteor;
import net.meteor.common.packets.PacketButtonPress;
import net.meteor.common.packets.PacketGhostMeteor;
import net.meteor.common.packets.PacketLastCrash;
import net.meteor.common.packets.PacketMeteorCrashSound;
import net.meteor.common.packets.PacketSettings;
import net.meteor.common.packets.PacketSoonestMeteor;
import net.meteor.plugin.baubles.Baubles;
import net.meteor.plugin.baubles.PacketToggleMagnetism;
import net.meteor.plugin.baubles.PacketTogglePlayerMagnetism;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;


public class ClientHandler
{
	public static CrashLocation lastCrashLocation = null;
	public static BlockPos nearestTimeLocation = null;
	public static ArrayList<BlockPos> ghostMetLocs = new ArrayList<>(); // TODO Privatize
	
	public void registerPackets() {
		MeteorsMod.network.registerMessage(PacketBlockedMeteor.Handler.class, PacketBlockedMeteor.class, 0, Side.CLIENT);
		MeteorsMod.network.registerMessage(PacketButtonPress.Handler.class, PacketButtonPress.class, 1, Side.SERVER);
		MeteorsMod.network.registerMessage(PacketGhostMeteor.Handler.class, PacketGhostMeteor.class, 2, Side.CLIENT);
		MeteorsMod.network.registerMessage(PacketLastCrash.Handler.class, PacketLastCrash.class, 3, Side.CLIENT);
		MeteorsMod.network.registerMessage(PacketSettings.Handler.class, PacketSettings.class, 4, Side.CLIENT);
		MeteorsMod.network.registerMessage(PacketSoonestMeteor.Handler.class, PacketSoonestMeteor.class, 5, Side.CLIENT);
		if (Baubles.isBaublesLoaded()) {
			MeteorsMod.network.registerMessage(PacketToggleMagnetism.Handler.class, PacketToggleMagnetism.class, 6, Side.SERVER);
			MeteorsMod.network.registerMessage(PacketTogglePlayerMagnetism.Handler.class, PacketTogglePlayerMagnetism.class, 7, Side.CLIENT);
		}
		MeteorsMod.network.registerMessage(PacketMeteorCrashSound.Handler.class, PacketMeteorCrashSound.class, 8, Side.CLIENT);
	}

	public static BlockPos getClosestIncomingMeteor(double pX, double pZ) {
		BlockPos coords = null;
		double y = 50.0D;
		for (BlockPos ghostMetLoc : ghostMetLocs) {
			if (coords != null) {
				BlockPos loc = ghostMetLoc;
				double var1 = getDistance(pX, y, pZ, loc.getX(), y, loc.getZ());
				double var2 = getDistance(pX, y, pZ, coords.getX(), y, coords.getZ());
				if (var1 < var2)
					coords = loc;
			} else {
				coords = ghostMetLoc;
			}
		}
		return coords;
	}

	private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double var7 = x1 - x2;
		double var9 = y1 - y2;
		double var11 = z1 - z2;
		return MathHelper.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
	}
	
	public static ITextComponent createMessage(String s, TextFormatting ecf) {
		return new TextComponentString(s).setStyle(new Style().setColor(ecf));
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		MeteorsMod.network.sendTo(new PacketSettings(), player);
	}
	
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
				HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(event.getWorld().provider.getDimension());
				MeteorsMod.network.sendTo(new PacketGhostMeteor(), player);		// Clear Ghost Meteors
				metHandler.sendGhostMeteorPackets(player);
				if (metHandler.getForecast() == null) {
					MeteorsMod.log.info("FORECAST WAS NULL");
				}
				MeteorsMod.network.sendTo(new PacketLastCrash(metHandler.getForecast().getLastCrashLocation()), player);
				MeteorsMod.network.sendTo(new PacketSoonestMeteor(metHandler.getForecast().getNearestTimeMeteor()), player);
			}
		}
	}

}