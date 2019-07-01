package net.meteor.common.command;

import java.util.List;

import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandSpawnComet extends CommandBase {

	@Override
	public String getName() {
		return "comet";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/comet <x> <y> <z> [type] [delay]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			if (args.length < 3) {
				sender.sendMessage(new TextComponentString("Usage: " + getUsage(sender)));
				return;
			}
			
			World world = sender.getEntityWorld();
			int type = world.rand.nextInt(EnumMeteor.values().length);
			if (args.length >= 4) {
				type = parseInt(args[3], 0, EnumMeteor.values().length - 1);
			}
			int delay = 0;
			if (args.length >= 5) {
				delay = Integer.parseInt(args[4]);
			}
			double x = sender.getPosition().getX();
			double y = sender.getPosition().getY();
			double z = sender.getPosition().getZ();
	        x = parseCoordinate(x, args[0], true).getResult();
	        y = parseCoordinate(y, args[1], true).getResult();
	        z = parseCoordinate(z, args[2], true).getResult();
			EntityComet comet = new EntityComet(world, x, z, EnumMeteor.getTypeFromID(type));
			comet.setPosition(x, y, z);
			comet.prevPosY = comet.posY;
			comet.spawnPauseTicks = delay;
			
			HandlerMeteor meteorHandler = MeteorsMod.proxy.metHandlers.get(world.provider.getDimension());
			List<IMeteorShield> shields = meteorHandler.getShieldManager().getShieldsInRange((int)x, (int)z);
			boolean blocked = false;

			for (IMeteorShield ims : shields) {
				if (ims.getPreventComets()) {
					blocked = true;
					break;
				}
			}
			
			if (!blocked) {
				for (IMeteorShield ims : shields) {
					TileEntityMeteorShield shield = (TileEntityMeteorShield) world.getTileEntity(new BlockPos(ims.getX(), ims.getY(), ims.getZ()));
					if (shield != null) {
						shield.detectComet(comet);
					}
				}
				
				world.spawnEntity(comet);
				sender.sendMessage(new TextComponentString("Comet spawned."));
			} else {
				sender.sendMessage(new TextComponentString("Comet was blocked by a Meteor Shield."));
			}
			
		} catch (Exception e) {
			sender.sendMessage(new TextComponentString(e.getMessage()));
			sender.sendMessage(new TextComponentString("Usage: " + getUsage(sender)));
		}
	}

}
