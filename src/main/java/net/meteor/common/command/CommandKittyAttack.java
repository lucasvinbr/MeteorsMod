package net.meteor.common.command;

import net.meteor.common.MeteorsMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class CommandKittyAttack extends CommandBase
{
	@Override
	public String getName()
	{
		return "kittyattack";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			World world = sender.getEntityWorld();
			MeteorsMod.proxy.metHandlers.get(world.provider.getDimension()).kittyAttack();
		}
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/kittyattack";
	}

}