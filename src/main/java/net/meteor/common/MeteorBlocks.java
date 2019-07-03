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

public class MeteorBlocks {
	
	public static final Block METEOR = new MeteorBlock().setRegistryName("Meteor");
	public static final Block HOT_METEOR = new HotMeteorBlock().setRegistryName("HotMeteor");
	public static final Block RARE_METEOR = new RareFallenMeteorBlock().setRegistryName("MeteorRare").setCreativeTab(null);
	public static final Block HOT_RARE_METEOR = new RareFallenHotMeteorBlock().setRegistryName("HotRareMeteor").setCreativeTab(null);
	public static final Block FREEZARITE = new FreezariteBlock().setRegistryName("Frezarite");
	public static final Block KRENKONITE = new KreknoriteBlock().setRegistryName("Kreknorite");
	public static final Block METEOR_ORE = new MeteorOreBlock(MeteorItems.itemMeteorChips).setRegistryName("MeteorOre");
	public static final Block FREEZARITE_ORE = new MeteorOreBlock(MeteorItems.itemFrezaCrystal).setRegistryName("FrezariteOre");
	public static final Block METEOR_SHIELD = new MeteorShieldBlock().setRegistryName("MeteorShield");
	public static final Block METEOR_SHIELD_TORCH = new MeteorShieldTorchBlock(true).setRegistryName("ProtectedLandTester").setCreativeTab(MeteorsMod.meteorTab);
	public static final Block METEOR_SHIELD_TORCH_INACTIVE = new MeteorShieldTorchBlock(false).setRegistryName("ProtectedLandTester").setCreativeTab(MeteorsMod.meteorTab);
	public static final Block METEOR_TIMER = new MeteorTimerBlock().setRegistryName("MeteorTimer");
	public static final Block RED_METEOR_GEM = new RedMeteorGemBlock().setRegistryName("blockRedGem");
	public static final Block DECORATOR_METEORITE = new BlockDecoration("meteorite_block");
	public static final Block DECORATOR_FREEZARITE = new BlockDecoration("frezarite_block");
	public static final Block DECORATOR_KREKNORITE = new BlockDecoration("kreknorite_block");
	public static final Block FREEZER = new FreezingMachineBlock().setRegistryName("freezingMachine").setHardness(3.5F);
	public static final Block blockSlippery				= new BlockSlippery(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyTwo			= new BlockSlippery(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyThree		= new BlockSlippery(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyFour			= new BlockSlippery(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyStairs		= new BlockSlipperyStairs(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyStairsTwo	= new BlockSlipperyStairs(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyStairsThree	= new BlockSlipperyStairs(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock");
	public static final Block blockSlipperyStairsFour	= new BlockSlipperyStairs(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock");
	
	public static void registerBlocks() {

		

	}

}
