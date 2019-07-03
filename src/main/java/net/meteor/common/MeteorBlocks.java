package net.meteor.common;

import net.meteor.common.block.BlockDecoration;
import net.meteor.common.block.BlockFreezingMachine;
import net.meteor.common.block.FreezariteBlock;
import net.meteor.common.block.KreknoriteBlock;
import net.meteor.common.block.MeteorBlock;
import net.meteor.common.block.MeteorOreBlock;
import net.meteor.common.block.MeteorShieldBlock;
import net.meteor.common.block.MeteorShieldTorchBlock;
import net.meteor.common.block.MeteorTimerBlock;
import net.meteor.common.block.RareFallenMeteorBlock;
import net.meteor.common.block.RedMeteorGemBlock;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.item.ItemBlockMeteorsMod;
import net.meteor.common.item.ItemBlockMeteorsModMetadata;
import net.meteor.common.item.ItemBlockSlippery;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MeteorBlocks {
	
	public static final Block blockMeteor 				= new MeteorBlock().setRegistryName("Meteor").setBlockTextureName("Meteor");
	public static final Block blockMeteorOre			= new MeteorOreBlock(MeteorItems.itemMeteorChips).setRegistryName("MeteorOre").setBlockTextureName("MeteorOre");
	public static final Block blockFrezariteOre			= new MeteorOreBlock(MeteorItems.itemFrezaCrystal).setRegistryName("FrezariteOre").setBlockTextureName("frezarite_ore");
	public static final Block blockRareMeteor			= new RareFallenMeteorBlock().setRegistryName("Meteor").setRegistryName("MeteorRare").setCreativeTab(null);
	public static final Block blockMeteorShield			= new MeteorShieldBlock().setRegistryName("MeteorShield").setBlockTextureName("Meteor");
	public static final Block blockFrezarite			= new FreezariteBlock().setRegistryName("Frezarite").setBlockTextureName("Frezarite");
	public static final Block blockKreknorite 			= new KreknoriteBlock().setRegistryName("Kreknorite").setBlockTextureName("Kreknorite");
	public static final Block torchMeteorShieldActive 	= new MeteorShieldTorchBlock().setRegistryName("ProtectedLandTesterActive").setBlockTextureName("ProtectedLandTesterActive").setCreativeTab(MeteorsMod.meteorTab);
	public static final Block blockMeteorTimer			= new MeteorTimerBlock().setRegistryName("MeteorTimer").setBlockTextureName("MeteorTimer");
	public static final Block blockRedMeteorGem			= new RedMeteorGemBlock().setRegistryName("blockRedGem").setBlockTextureName("blockRedGem");
	public static final Block blockDecorator			= new BlockDecoration("meteorite_block", "frezarite_block", "kreknorite_block").setBlockName("meteorDecor").setStepSound(Block.soundTypeMetal).setHardness(5.0F).setResistance(10.0F);
	public static final Block blockFreezer				= new BlockFreezingMachine().setRegistryName("freezingMachine").setHardness(3.5F).setStepSound(Block.soundTypeMetal);
	public static final Block blockSlippery				= new BlockSlippery(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyTwo			= new BlockSlippery(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyThree		= new BlockSlippery(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyFour			= new BlockSlippery(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyStairs		= new BlockSlipperyStairs(0.98F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyStairsTwo	= new BlockSlipperyStairs(1.03F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyStairsThree	= new BlockSlipperyStairs(1.07F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	public static final Block blockSlipperyStairsFour	= new BlockSlipperyStairs(1.10F).setHardness(1.0F).setRegistryName("slipperyBlock").setStepSound(Block.soundTypeGlass);
	
	public static void registerBlocks() {

		

	}

}
