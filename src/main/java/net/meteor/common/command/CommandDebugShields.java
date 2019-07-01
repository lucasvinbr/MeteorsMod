package net.meteor.common.command;

import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class CommandDebugShields extends CommandBase {
	
	@Override
	public String getName()
	{
		return "listShields";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {//TODO why would this ever run on the client 1.12.2
			World world = sender.getEntityWorld();
			HandlerMeteor metH = MeteorsMod.proxy.metHandlers.get(world.provider.getDimension());
			if (metH != null) {
                for (IMeteorShield shield : metH.getShieldManager().meteorShields) {
                    sender.sendMessage(new TextComponentString("x:" + shield.getX() + " y:" + shield.getY() + " z:" + shield.getZ() + " r:" + shield.getRange() + " o:" + shield.getOwner()));
                }
			}
			
		}
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/listShields";
	}

}
