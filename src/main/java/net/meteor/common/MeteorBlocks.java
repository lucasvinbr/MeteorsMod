package net.meteor.common;

import net.meteor.common.block.BlockDecoration;
import net.meteor.common.block.FreezingMachineBlock;
import net.meteor.common.block.FreezariteBlock;
import net.meteor.common.block.HotMeteorBlock;
import net.meteor.common.block.KreknoriteBlock;
import net.meteor.common.block.MeteorBlock;
import net.meteor.common.block.MeteorOreBlock;
import net.meteor.common.block.MeteorShieldBlock;
import net.meteor.common.block.MeteorShieldTorchBlock;
import net.meteor.common.block.MeteorTimerBlock;
import net.meteor.common.block.RareFallenHotMeteorBlock;
import net.meteor.common.block.RareFallenMeteorBlock;
import net.meteor.common.block.RedMeteorGemBlock;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MeteorsMod.MOD_ID)
public class MeteorBlocks {
	
	public static final Block METEOR = new MeteorBlock().setRegistryName("Meteor").setTranslationKey("Meteor");
	public static final Block HOT_METEOR = new HotMeteorBlock().setRegistryName("HotMeteor").setTranslationKey("HotMeteor");
	public static final Block RARE_METEOR = new RareFallenMeteorBlock().setRegistryName("MeteorRare").setCreativeTab(null).setTranslationKey("MeteorRare");
	public static final Block HOT_RARE_METEOR = new RareFallenHotMeteorBlock().setRegistryName("HotRareMeteor").setCreativeTab(null).setTranslationKey("HotRareMeteor");
	public static final Block FREEZARITE = new FreezariteBlock().setRegistryName("Frezarite").setTranslationKey("Frezarite");
	public static final Block KRENKONITE = new KreknoriteBlock().setRegistryName("Kreknorite").setTranslationKey("Kreknorite");
	public static final Block METEOR_ORE = new MeteorOreBlock(MeteorItems.itemMeteorChips).setRegistryName("MeteorOre").setTranslationKey("MeteorOre");
	public static final Block FREEZARITE_ORE = new MeteorOreBlock(MeteorItems.itemFrezaCrystal).setRegistryName("FrezariteOre").setTranslationKey("FrezariteOre");
	public static final Block METEOR_SHIELD = new MeteorShieldBlock().setRegistryName("MeteorShield").setTranslationKey("MeteorShield");
	public static final Block METEOR_SHIELD_TORCH = new MeteorShieldTorchBlock(true).setRegistryName("ProtectedLandTester").setCreativeTab(MeteorsMod.meteorTab).setTranslationKey("ProtectedLandTester");
	public static final Block METEOR_SHIELD_TORCH_INACTIVE = new MeteorShieldTorchBlock(false).setRegistryName("ProtectedLandTester").setCreativeTab(MeteorsMod.meteorTab).setTranslationKey("ProtectedLandTester");
	public static final Block METEOR_TIMER = new MeteorTimerBlock().setRegistryName("MeteorTimer").setTranslationKey("MeteorTimer");
	public static final Block RED_METEOR_GEM = new RedMeteorGemBlock().setRegistryName("blockRedGem").setTranslationKey("blockRedGem");
	public static final Block DECORATOR_METEORITE = new BlockDecoration("meteorite_block").setTranslationKey("meteorite_block");
	public static final Block DECORATOR_FREEZARITE = new BlockDecoration("frezarite_block").setTranslationKey("frezarite_block");
	public static final Block DECORATOR_KREKNORITE = new BlockDecoration("kreknorite_block").setTranslationKey("kreknorite_block");
	public static final Block FREEZER = new FreezingMachineBlock().setRegistryName("freezingMachine").setHardness(3.5F).setTranslationKey("freezingMachine");
	public static final Block blockSlippery				= new BlockSlippery(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyTwo			= new BlockSlippery(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock2").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyThree		= new BlockSlippery(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock3").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyFour			= new BlockSlippery(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock4").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairs		= new BlockSlipperyStairs(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock5").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsTwo	= new BlockSlipperyStairs(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock6").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsThree	= new BlockSlipperyStairs(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock7").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsFour	= new BlockSlipperyStairs(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock8").setTranslationKey("slipperyBlock");


	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(MeteorBlocks.METEOR_ORE);
		event.getRegistry().register(MeteorBlocks.METEOR);
		event.getRegistry().register(MeteorBlocks.HOT_METEOR);
		event.getRegistry().register(MeteorBlocks.HOT_RARE_METEOR);
		event.getRegistry().register(MeteorBlocks.RARE_METEOR);
		event.getRegistry().register(MeteorBlocks.METEOR_SHIELD);
		event.getRegistry().register(MeteorBlocks.METEOR_SHIELD_TORCH);
		event.getRegistry().register(MeteorBlocks.FREEZARITE);
		event.getRegistry().register(MeteorBlocks.KRENKONITE);
		event.getRegistry().register(MeteorBlocks.METEOR_TIMER);
		event.getRegistry().register(MeteorBlocks.RED_METEOR_GEM);
		event.getRegistry().register(MeteorBlocks.DECORATOR_FREEZARITE);
		event.getRegistry().register(MeteorBlocks.DECORATOR_KREKNORITE);
		event.getRegistry().register(MeteorBlocks.DECORATOR_METEORITE);
		event.getRegistry().register(MeteorBlocks.FREEZER);
		event.getRegistry().register(MeteorBlocks.blockSlippery);
		event.getRegistry().register(MeteorBlocks.blockSlipperyTwo);
		event.getRegistry().register(MeteorBlocks.blockSlipperyThree);
		event.getRegistry().register(MeteorBlocks.blockSlipperyFour);
		event.getRegistry().register(MeteorBlocks.blockSlipperyStairs);
		event.getRegistry().register(MeteorBlocks.blockSlipperyStairsTwo);
		event.getRegistry().register(MeteorBlocks.blockSlipperyStairsThree);
		event.getRegistry().register(MeteorBlocks.blockSlipperyStairsFour);
		event.getRegistry().register(MeteorBlocks.FREEZARITE_ORE);
	}

}
