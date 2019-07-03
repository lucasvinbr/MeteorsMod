package net.meteor.common.item;

import java.util.List;

import net.meteor.client.TextureDetector;
import net.meteor.common.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemDetector extends ItemMeteorsMod {
	
	private int type;
	private String dots = ".";
	private long lastDot = 0L;

	public ItemDetector(int t) {
		super();
		this.type = t;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		long worldTime = worldIn.getTotalWorldTime();
		if (worldTime - lastDot > 10L) {
			lastDot = worldTime;
			dots = dots.length() > 5 ? "." : dots + ".";
		}
		
		if (type == 1) {
			tooltip.add(TextFormatting.AQUA + I18n.translateToLocal("Detector.time"));
			if (ClientHandler.nearestTimeLocation == null) {
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("Detector.detected"));
			}
		} else if (type == 0) {
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("Detector.proximity"));
			if (ClientHandler.getClosestIncomingMeteor(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posZ) == null) {//TODO confirm for 1.12.2
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("Detector.detected"));
			}
		} else {
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("Detector.crash"));
			if (ClientHandler.lastCrashLocation == null) {
				tooltip.add(I18n.translateToLocal("CrashDetector.noActivity"));
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				if (ClientHandler.lastCrashLocation.inOrbit) {
					tooltip.add(TextFormatting.GREEN + "Orbital Entrance Detected!");
				} else {
					tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("CrashDetector.zoneLocated"));
				}
			}
		}
		
	}

	//TODO 1.12.2
	//@SideOnly(Side.CLIENT)
	//@Override
    //public void registerIcons(IIconRegister par1IconRegister) {
	//	TextureMap map = (TextureMap)par1IconRegister;
	//	map.setTextureEntry(this.iconString, new TextureDetector(this.iconString, type));
	//	this.itemIcon = map.getTextureExtry(this.iconString);
	//}

}
