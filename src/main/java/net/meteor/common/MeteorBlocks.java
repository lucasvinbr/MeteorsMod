package net.meteor.common;

import net.meteor.common.block.*;
import net.minecraft.block.Block;

public class MeteorBlocks {
	
	public static final Block blockMeteor 				= new MeteorBlock().setRegistryName("Meteor");
	public static final Block blockMeteorOre			= new MeteorOreBlock(MeteorItems.itemMeteorChips).setRegistryName("MeteorOre");
	public static final Block blockFrezariteOre			= new MeteorOreBlock(MeteorItems.itemFrezaCrystal).setRegistryName("FrezariteOre");
	public static final Block blockRareMeteor			= new RareFallenMeteorBlock().setRegistryName("MeteorRare").setCreativeTab(null);
	public static final Block blockMeteorShield			= new MeteorShieldBlock().setRegistryName("MeteorShield");
	public static final Block blockFrezarite			= new FreezariteBlock().setRegistryName("Frezarite");
	public static final Block blockKreknorite 			= new KreknoriteBlock().setRegistryName("Kreknorite");
	public static final Block torchMeteorShieldActive 	= new MeteorShieldTorchBlock().setRegistryName("ProtectedLandTesterActive").setCreativeTab(MeteorsMod.meteorTab);
	public static final Block blockMeteorTimer			= new MeteorTimerBlock().setRegistryName("MeteorTimer");
	public static final Block blockRedMeteorGem			= new RedMeteorGemBlock().setRegistryName("blockRedGem");
	public static final Block blockDecorator			= new BlockDecoration("meteorite_block", "frezarite_block", "kreknorite_block").setRegistryName("meteorDecor").setHardness(5.0F).setResistance(10.0F);
	public static final Block blockFreezer				= new BlockFreezingMachine().setRegistryName("freezingMachine").setHardness(3.5F);
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
