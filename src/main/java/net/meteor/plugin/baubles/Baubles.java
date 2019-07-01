package net.meteor.plugin.baubles;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.item.ItemMeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

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
		MagnetismController = new ItemMagnetismController().setTranslationKey("MagnetizationController").setTextureName("MagnetizationController");
		MagneticFieldDisruptor = new ItemMeteorsMod().setTranslationKey("MagneticFieldDisruptor").setTextureName("MagneticFieldDisruptor");
		GameRegistry.registerItem(MagnetismController, "MagnetizationController");
		GameRegistry.registerItem(MagneticFieldDisruptor, "MagneticFieldDisruptor");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MagneticFieldDisruptor, 1), new Object[] {
			"oro", "mdm", "omo", 'o', Blocks.obsidian, 'r', MeteorItems.itemRedMeteorGem,
                'd', "gemDiamond", 'm', MeteorItems.MeteoriteIngot
		}));
		
		GameRegistry.addRecipe(new ItemStack(MagnetismController, 1), new Object[] {
			" s ", "lml", 's', Items.string, 'l', Items.leather,
                'm', MagneticFieldDisruptor
		});
		
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
