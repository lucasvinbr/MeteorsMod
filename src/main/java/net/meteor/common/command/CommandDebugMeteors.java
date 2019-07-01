package net.meteor.common.command;

import java.util.Iterator;

import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.GhostMeteor;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.MeteorForecast;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandDebugMeteors extends CommandBase {

	@Override
	public String getName() {
		return "listMeteors";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/listMeteors [clear]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		HandlerMeteor metH = MeteorsMod.proxy.metHandlers.get(world.provider.getDimension());
		if (metH != null) {
			MeteorForecast forecast = metH.getForecast();
			
			if (args.length >= 1 && args[0].equalsIgnoreCase("clear")) {
				forecast.clearMeteors();
				sender.sendMessage(new TextComponentString("Meteors Cleared"));
				return;
			}
			
			int secs = forecast.getSecondsUntilNewMeteor();
			sender.sendMessage(new TextComponentString(secs + " Seconds Until Possible New Meteor"));
			
			sender.sendMessage(new TextComponentString(metH.ghostMets.size() + " Meteor(s) in Orbit"));
			for (GhostMeteor met : metH.ghostMets) {
				sender.sendMessage(new TextComponentString("T:" + met.type.toString() + " S:" + met.size + " X:" + met.x + " Z:" + met.z + " RT:" + met.getRemainingTicks()));
			}
		}
	}

}
