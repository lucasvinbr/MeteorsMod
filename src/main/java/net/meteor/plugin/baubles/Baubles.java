package net.meteor.plugin.baubles;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.item.ItemMeteorsMod;
import net.meteor.common.util.MeteorCraftingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Baubles {
	
	public static Item MagnetismController;
	public static Item MagneticFieldDisruptor;
	
	public static long renderDisplayTicks;
	public static boolean renderDisplay = false;
	public static boolean enabledMagnetism;
	
	private static boolean baublesLoaded = false;
	private static HandlerKey keyHandler;
	
	public static void setupBaubleItems() {
		baublesLoaded = true;
		MagnetismController = new ItemMagnetismController().setTranslationKey("MagnetizationController");// TODO 1.12.2 .setTextureName("MagnetizationController");
		MagneticFieldDisruptor = new ItemMeteorsMod().setTranslationKey("MagneticFieldDisruptor");// TODO 1.12.2 .setTextureName("MagneticFieldDisruptor");
		//TODO 1.12.2
		//GameRegistry.registerItem(MagnetismController, "MagnetizationController");
		//GameRegistry.registerItem(MagneticFieldDisruptor, "MagneticFieldDisruptor");
		
		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MagneticFieldDisruptor, 1), "oro", "mdm", "omo", 'o', Blocks.OBSIDIAN, 'r', MeteorItems.itemRedMeteorGem,
				'd', "gemDiamond", 'm', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MagnetismController, 1), " s ", "lml", 's', Items.STRING, 'l', Items.LEATHER,
				'm', MagneticFieldDisruptor);
		
		MeteorsMod.log.info("Baubles mod found. Baubles integration enabled.");
	}
	
	public static void setupBaubleClient() {
		keyHandler = new HandlerKey();
		keyHandler.init();
		FMLCommonHandler.instance().bus().register(keyHandler);
	}
	
	public static boolean isBaublesLoaded() {
		return baublesLoaded;
	}
	
	public static int canAttractItems(EntityPlayer player, int enchLevel) {
		return ItemMagnetismController.isMagnetizationEnabled(player, enchLevel);
	}

}
