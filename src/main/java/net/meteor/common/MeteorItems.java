package net.meteor.common;

import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.block.MeteorBlock;
import net.meteor.common.item.*;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.Arrays;

@Mod.EventBusSubscriber(modid = MeteorsMod.MOD_ID)
public class MeteorItems {

	public static final ArmorMaterial MeteoriteArmor = EnumHelper.addArmorMaterial("METEORITE", "meteors:meteorite", 36, new int[] { 2, 7, 5, 2 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);
	public static final ArmorMaterial FrezariteArmor = EnumHelper.addArmorMaterial("FREZARITE", "meteors:frezarite", 7, new int[] { 2, 5, 3, 1 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);
	public static final ArmorMaterial KreknoriteArmor = EnumHelper.addArmorMaterial("KREKNORITE", "meteors:kreknorite", 40, new int[] { 3, 8, 6, 3 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);

	public static final ToolMaterial MeteoriteTool = EnumHelper.addToolMaterial("METEORITE", 3, 900, 10.0F, 2, 15);
	public static final ToolMaterial FrezariteTool = EnumHelper.addToolMaterial("FREZARITE", 2, 225, 7.0F, 2, 20);

	//TODO doesn't seem that the enchants are applying 1.12.2
	public static final Item itemMeteorChips 			= new ItemMeteorsMod().setMaxStackSize(64).setRegistryName("MeteorChips").setTranslationKey("MeteorChips");
	public static final Item itemRedMeteorGem 			= new ItemMeteorsMod().setMaxStackSize(64).setRegistryName("RedMeteorGem").setTranslationKey("RedMeteorGem");
	public static final Item itemMeteorSummonerRandom 		= new ItemSummoner().setRegistryName("MeteorSummonerRandom").setTranslationKey("MeteorSummonerRandom");
	public static final Item itemMeteorSummonerMeteorite 		= new ItemSummoner().setRegistryName("MeteorSummonerMeteorite").setTranslationKey("MeteorSummonerMeteorite");
	public static final Item itemMeteorSummonerFrezarite 		= new ItemSummoner().setRegistryName("MeteorSummonerFrezarite").setTranslationKey("MeteorSummonerFrezarite");
	public static final Item itemMeteorSummonerKreknorite 		= new ItemSummoner().setRegistryName("MeteorSummonerKreknorite").setTranslationKey("MeteorSummonerKreknorite");
	public static final Item itemMeteorSummonerUnknown 		= new ItemSummoner().setRegistryName("MeteorSummonerUnknown").setTranslationKey("MeteorSummonerUnknown");
	public static final Item itemMeteorSummonerKitty 		= new ItemSummoner().setRegistryName("MeteorSummonerKitty").setTranslationKey("MeteorSummonerKitty");
	public static final Item itemFrezaCrystal 			= new ItemMeteorsMod().setMaxStackSize(64).setRegistryName("FrezariteCrystal").setTranslationKey("FrezariteCrystal");
	public static final Item itemKreknoChip 			= new ItemMeteorsMod().setMaxStackSize(64).setRegistryName("KreknoriteChip").setTranslationKey("KreknoriteChip");
	public static final Item itemVanillaIceCream 		= new ItemFoodMeteorsMod(4, false).setMaxStackSize(64).setRegistryName("VanillaIceCream").setTranslationKey("VanillaIceCream");
	public static final Item itemChocolateIceCream 		= new ItemFoodMeteorsMod(6, false).setMaxStackSize(64).setRegistryName("ChocolateIceCream").setTranslationKey("ChocolateIceCream");
	public static final Item itemMeteorProximityDetector= new ItemDetector(0).setRegistryName("MeteorDetectorProximity").setTranslationKey("MeteorDetectorProximity");
	public static final Item itemMeteorTimeDetector 	= new ItemDetector(1).setRegistryName("MeteorDetectorTime").setTranslationKey("MeteorDetectorTime");
	public static final Item itemMeteorCrashDetector 	= new ItemDetector(2).setRegistryName("MeteorDetectorCrash").setTranslationKey("MeteorDetectorCrash");
	public static final Item MeteoriteHelmet 			= new ItemEnchArmor(MeteoriteArmor, 3, EntityEquipmentSlot.HEAD).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteHelmet").setTranslationKey("MeteoriteHelmet");
	public static final Item MeteoriteBody 				= new ItemEnchArmor(MeteoriteArmor, 3, EntityEquipmentSlot.CHEST).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteChest").setTranslationKey("MeteoriteChest");
	public static final Item MeteoriteLegs 				= new ItemEnchArmor(MeteoriteArmor, 3, EntityEquipmentSlot.LEGS).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteLegs").setTranslationKey("MeteoriteLegs");
	public static final Item MeteoriteBoots 			= new ItemEnchArmor(MeteoriteArmor, 3, EntityEquipmentSlot.FEET).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteBoots").setTranslationKey("MeteoriteBoots");
	public static final Item MeteoriteAxe 				= new ItemEnchAxe(MeteoriteTool, MeteorConstants.MeteorTools.METEORITE_AXE_DAMAGE, MeteorConstants.MeteorTools.METEORITE_AXE_SPEED).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteAxe").setTranslationKey("MeteoriteAxe");
	public static final Item MeteoriteSpade 			= new ItemEnchSpade(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteSpade").setTranslationKey("MeteoriteSpade");
	public static final Item MeteoriteSword 			= new ItemEnchSword(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteSword").setTranslationKey("MeteoriteSword");
	public static final Item MeteoritePickaxe 			= new ItemEnchPickaxe(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoritePickaxe").setTranslationKey("MeteoritePickaxe");
	public static final Item MeteoriteHoe 				= new ItemEnchHoe(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setRegistryName("MeteoriteHoe").setTranslationKey("MeteoriteHoe");
	public static final Item FrezariteHelmet 			= new ItemEnchArmor(FrezariteArmor, 3, EntityEquipmentSlot.HEAD).setEnch(Enchantments.RESPIRATION, 3).setRegistryName("FrezariteHelmet").setTranslationKey("FrezariteHelmet");
	public static final Item FrezariteBody 				= new ItemEnchArmor(FrezariteArmor, 3, EntityEquipmentSlot.CHEST).setEnch(Enchantments.AQUA_AFFINITY, 1).setRegistryName("FrezariteChest").setTranslationKey("FrezariteChest");
	public static final Item FrezariteLegs 				= new ItemEnchArmor(FrezariteArmor, 3, EntityEquipmentSlot.LEGS).setEnch(MeteorsMod.ColdTouch, 1).setRegistryName("FrezariteLegs").setTranslationKey("FrezariteLegs");
	public static final Item FrezariteBoots 			= new ItemEnchArmor(FrezariteArmor, 3, EntityEquipmentSlot.FEET).setEnch(MeteorsMod.ColdTouch, 1).setRegistryName("FrezariteBoots").setTranslationKey("FrezariteBoots");
	public static final Item FrezaritePickaxe 			= new ItemFrezaritePickaxe(FrezariteTool).setRegistryName("FrezaritePickaxe").setTranslationKey("FrezaritePickaxe");
	public static final Item FrezariteSpade 			= new ItemFrezariteSpade(FrezariteTool).setRegistryName("FrezariteSpade").setTranslationKey("FrezariteSpade");
	public static final Item FrezariteSword 			= new ItemFrezariteSword(FrezariteTool).setRegistryName("FrezariteSword").setTranslationKey("FrezariteSword");
	public static final Item FrezariteAxe 				= new ItemFrezariteAxe(FrezariteTool, MeteorConstants.MeteorTools.FREZARITE_AXE_DAMAGE, MeteorConstants.MeteorTools.FREZARITE_AXE_SPEED).setRegistryName("FrezariteAxe").setTranslationKey("FrezariteAxe");
	public static final Item FrezariteHoe 				= new ItemFrezariteHoe(FrezariteTool).setRegistryName("FrezariteHoe").setTranslationKey("FrezariteHoe");
	public static final Item KreknoriteHelmet 			= new ItemEnchArmor(KreknoriteArmor, 3, EntityEquipmentSlot.HEAD).setEnch(Enchantments.FIRE_PROTECTION, 4).setRegistryName("KreknoriteHelmet").setTranslationKey("KreknoriteHelmet");
	public static final Item KreknoriteBody 			= new ItemEnchArmor(KreknoriteArmor, 3, EntityEquipmentSlot.CHEST).setEnch(Enchantments.FIRE_PROTECTION, 4).setRegistryName("KreknoriteChest").setTranslationKey("KreknoriteChest");
	public static final Item KreknoriteLegs 			= new ItemEnchArmor(KreknoriteArmor, 3, EntityEquipmentSlot.LEGS).setEnch(Enchantments.FIRE_PROTECTION, 4).setRegistryName("KreknoriteLegs").setTranslationKey("KreknoriteLegs");
	public static final Item KreknoriteBoots 			= new ItemEnchArmor(KreknoriteArmor, 3, EntityEquipmentSlot.FEET).setEnch(Enchantments.FIRE_PROTECTION, 4).setRegistryName("KreknoriteBoots").setTranslationKey("KreknoriteBoots");
	public static final Item KreknoriteSword 			= new ItemKreknoSword(MeteoriteTool).setRegistryName("KreknoriteSword").setTranslationKey("KreknoriteSword");

	// Begin industrialization! :D
	public static final Item MeteoriteIngot				= new ItemMeteorsMod().setRegistryName("MeteoriteIngot").setTranslationKey("MeteoriteIngot");
	public static final Item FrozenIronIngot = new ItemMeteorsMod().setRegistryName("FrozenIronIngot").setTranslationKey("FrozenIronIngot");
	public static final Item KreknoriteIngot			= new ItemMeteorsMod().setRegistryName("KreknoriteIngot").setTranslationKey("KreknoriteIngot");
	
	public static void readyItems() {
		MeteoriteAxe.setHarvestLevel("axe", 3);
		MeteoritePickaxe.setHarvestLevel("pickaxe", 3);
		MeteoriteSpade.setHarvestLevel("shovel", 3);
		FrezaritePickaxe.setHarvestLevel("pickaxe", 2);
		FrezariteSpade.setHarvestLevel("shovel", 2);
		FrezariteAxe.setHarvestLevel("axe", 2);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {

		event.getRegistry().register(itemMeteorChips);
		event.getRegistry().register(itemRedMeteorGem);
		event.getRegistry().register(itemFrezaCrystal);
		event.getRegistry().register(itemKreknoChip);
		event.getRegistry().register(itemVanillaIceCream);
		event.getRegistry().register(itemChocolateIceCream);
		event.getRegistry().register(itemMeteorCrashDetector);
		event.getRegistry().register(itemMeteorProximityDetector);
		event.getRegistry().register(itemMeteorTimeDetector);
		event.getRegistry().register(itemMeteorSummonerRandom);
		event.getRegistry().register(itemMeteorSummonerMeteorite);
		event.getRegistry().register(itemMeteorSummonerFrezarite);
		event.getRegistry().register(itemMeteorSummonerKreknorite);
		event.getRegistry().register(itemMeteorSummonerUnknown);
		event.getRegistry().register(itemMeteorSummonerKitty);
		event.getRegistry().register(FrezariteHelmet);
		event.getRegistry().register(FrezariteBody);
		event.getRegistry().register(FrezariteLegs);
		event.getRegistry().register(FrezariteBoots);
		event.getRegistry().register(FrezaritePickaxe);
		event.getRegistry().register(FrezariteSpade);
		event.getRegistry().register(KreknoriteHelmet);
		event.getRegistry().register(KreknoriteBody);
		event.getRegistry().register(KreknoriteLegs);
		event.getRegistry().register(KreknoriteBoots);
		event.getRegistry().register(KreknoriteSword);
		event.getRegistry().register(MeteoriteAxe);
		event.getRegistry().register(MeteoriteBody);
		event.getRegistry().register(MeteoriteBoots);
		event.getRegistry().register(MeteoriteHelmet);
		event.getRegistry().register(MeteoriteHoe);
		event.getRegistry().register(MeteoriteLegs);
		event.getRegistry().register(MeteoritePickaxe);
		event.getRegistry().register(MeteoriteSpade);
		event.getRegistry().register(MeteoriteSword);
		event.getRegistry().register(FrezariteSword);
		event.getRegistry().register(FrezariteAxe);
		event.getRegistry().register(FrezariteHoe);
		event.getRegistry().register(MeteoriteIngot);
		event.getRegistry().register(FrozenIronIngot);
		event.getRegistry().register(KreknoriteIngot);

		Class<MeteorBlocks> meteorBlocksClass = MeteorBlocks.class;
		Field[] fields = meteorBlocksClass.getDeclaredFields();
		Arrays.stream(fields).forEach(field -> {
			try {
				Object o = field.get(null);
				if (o instanceof Block) {
					if(!(o instanceof BlockSlippery) && !(o instanceof BlockSlipperyStairs))
						event.getRegistry().register(new ItemBlock((Block) o).setRegistryName(((Block) o).getRegistryName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlippery).setRegistryName(MeteorBlocks.blockSlippery.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyTwo).setRegistryName(MeteorBlocks.blockSlipperyTwo.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyThree).setRegistryName(MeteorBlocks.blockSlipperyThree.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyFour).setRegistryName(MeteorBlocks.blockSlipperyFour.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyStairs).setRegistryName(MeteorBlocks.blockSlipperyStairs.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyStairsTwo).setRegistryName(MeteorBlocks.blockSlipperyStairsTwo.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyStairsThree).setRegistryName(MeteorBlocks.blockSlipperyStairsThree.getRegistryName()));
		event.getRegistry().register(new ItemBlockSlippery(MeteorBlocks.blockSlipperyStairsFour).setRegistryName(MeteorBlocks.blockSlipperyStairsFour.getRegistryName()));

		// Ore Dictionary
		OreDictionary.registerOre("ingotMeteorite", MeteoriteIngot);
		OreDictionary.registerOre("ingotFrozenIron", FrozenIronIngot);
		OreDictionary.registerOre("ingotKreknorite", KreknoriteIngot);
	}
	
}
