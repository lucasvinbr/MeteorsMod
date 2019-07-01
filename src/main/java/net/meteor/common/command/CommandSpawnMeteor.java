package net.meteor.common.command;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityMeteor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandSpawnMeteor extends CommandBase {

	@Override
	public String getName() {
		return "meteor";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/meteor <x> <y> <z> <size> [delay] [type] [summoned]";
	}

	@Override
	public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			if (args.length < 4) {
				sender.sendMessage(new TextComponentString("Usage: " + getUsage(sender)));
				return;
			}
			
			World world = sender.getEntityWorld();
			int type = world.rand.nextInt(EnumMeteor.values().length);
			if (args.length >= 6) {
				type = parseInt(args[5], 0, EnumMeteor.values().length - 1);
			}
			int delay = 0;
			if (args.length >= 5) {
				delay = Integer.parseInt(args[4]);
			}
			boolean summoned = false;
			if (args.length >= 7) {
				summoned = Boolean.parseBoolean(args[6]);
			}
			double x = sender.getPosition().getX();
			double y = sender.getPosition().getY();
			double z = sender.getPosition().getZ();
	        x = parseCoordinate(x, args[0], true).getResult();
	        y = parseCoordinate(y, args[1], true).getResult();
	        z = parseCoordinate(z, args[2], true).getResult();
			EntityMeteor meteor = new EntityMeteor(world, parseInt(args[3], 1, 3), x, z, EnumMeteor.getTypeFromID(type), summoned);
			meteor.posY = y;
			meteor.prevPosY = meteor.posY;
			meteor.spawnPauseTicks = delay;
			world.spawnEntity(meteor);
			if (!MeteorsMod.instance.isDimensionWhitelisted(world.provider.getDimension())) {
				sender.sendMessage(new TextComponentString("The Meteor isn't techincally allowed in this dimension, but I'll spawn it for you anyway."));
			}
			sender.sendMessage(new TextComponentString("Meteor spawned."));
		} catch (Exception e) {
			sender.sendMessage(new TextComponentString(e.getMessage()));
			sender.sendMessage(new TextComponentString("Usage: " + getUsage(sender)));
		}
	}

}
