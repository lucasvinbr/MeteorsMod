package net.meteor.common;

import net.meteor.common.block.DecorationBlock;
import net.meteor.common.block.FreezariteGasBlock;
import net.meteor.common.block.FreezingMachineBlock;
import net.meteor.common.block.FreezariteBlock;
import net.meteor.common.block.HotMeteorBlock;
import net.meteor.common.block.KreknoriteBlock;
import net.meteor.common.block.LiquidFreezariteBlock;
import net.meteor.common.block.MeteorBlock;
import net.meteor.common.block.MeteorOreBlock;
import net.meteor.common.block.MeteorShieldBlock;
import net.meteor.common.block.MeteorShieldTorchBlock;
import net.meteor.common.block.MeteorTimerBlock;
import net.meteor.common.block.RareFallenHotMeteorBlock;
import net.meteor.common.block.RareFallenMeteorBlock;
import net.meteor.common.block.RedMeteorGemBlock;
import net.meteor.common.block.SlipperyBlock;
import net.meteor.common.block.BlockSlipperyStairs;
import net.minecraft.block.Block;

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
	public static final Block DECORATOR_METEORITE = new DecorationBlock("meteorite_block").setTranslationKey("meteorite_block");
	public static final Block DECORATOR_FREEZARITE = new DecorationBlock("frezarite_block").setTranslationKey("frezarite_block");
	public static final Block DECORATOR_KREKNORITE = new DecorationBlock("kreknorite_block").setTranslationKey("kreknorite_block");
	public static final Block FREEZER = new FreezingMachineBlock().setRegistryName("freezingMachine").setHardness(3.5F).setTranslationKey("freezingMachine");
	public static final Block blockSlippery				= new SlipperyBlock(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyTwo			= new SlipperyBlock(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyThree		= new SlipperyBlock(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyFour			= new SlipperyBlock(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairs		= new BlockSlipperyStairs(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsTwo	= new BlockSlipperyStairs(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsThree	= new BlockSlipperyStairs(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block blockSlipperyStairsFour	= new BlockSlipperyStairs(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock").setTranslationKey("slipperyBlock");
	public static final Block LIQUID_FREEZARITE = new LiquidFreezariteBlock();
	public static final Block FREEZARITE_GAS = new FreezariteGasBlock();

    public static void registerBlocks() {

		

	}

}
