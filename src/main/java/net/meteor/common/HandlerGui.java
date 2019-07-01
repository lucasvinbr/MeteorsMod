package net.meteor.common;

import net.meteor.client.gui.GuiFreezingMachine;
import net.meteor.client.gui.GuiMeteorShield;
import net.meteor.common.block.container.ContainerFreezingMachine;
import net.meteor.common.block.container.ContainerMeteorShield;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityMeteorShield) {
			return new ContainerMeteorShield(player.inventory, (TileEntityMeteorShield)tileEntity);
		}
		if (tileEntity instanceof TileEntityFreezingMachine) {
			return new ContainerFreezingMachine(player.inventory, (TileEntityFreezingMachine)tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityMeteorShield) {
			return new GuiMeteorShield(player.inventory, (TileEntityMeteorShield)tileEntity);
		}
		if (tileEntity instanceof TileEntityFreezingMachine) {
			return new GuiFreezingMachine(player.inventory, (TileEntityFreezingMachine)tileEntity);
		}
		return null;
	}

}
