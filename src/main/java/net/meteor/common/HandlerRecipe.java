package net.meteor.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class HandlerRecipe implements IFuelHandler {
	
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		Item item = event.crafting.getItem();
		EntityPlayer player = event.player;
		if (item == Item.getItemFromBlock(MeteorBlocks.blockMeteorShield)) {
			player.addStat(HandlerAchievement.shieldCrafted, 1);
		} else if (item == MeteorItems.KreknoriteSword) {
			player.addStat(HandlerAchievement.craftedKreknoSword, 1);
		} else if (item == MeteorItems.itemMeteorProximityDetector ||
				   item == MeteorItems.itemMeteorTimeDetector ||
				   item == MeteorItems.itemMeteorCrashDetector) {
			player.addStat(HandlerAchievement.craftedDetector, 1);
		} else if (item == Item.getItemFromBlock(MeteorBlocks.blockMeteorTimer)) {
			player.addStat(HandlerAchievement.craftedMeteorTimer, 1);
		} else if (item == Item.getItemFromBlock(MeteorBlocks.blockFreezer)) {
			player.addStat(HandlerAchievement.craftedFreezer, 1);
		}
			
	}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel.getItem() == MeteorItems.itemKreknoChip) {
			return 3300;
		}
		return 0;
	}

	public void addRecipes() {
		// ============= Crafting Recipes =============
		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHelmet, 1), new Object[] {
			"mmm", "m m", 'm', MeteorItems.MeteoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBody, 1), new Object[] { 
			"m m", "mmm", "mmm", 'm', MeteorItems.MeteoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteLegs, 1), new Object[] { 
			"mmm", "m m", "m m", 'm', MeteorItems.MeteoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBoots, 1), new Object[] { 
			"m m", "m m", 'm', MeteorItems.MeteoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "ms", " s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "sm", "s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSpade, 1), new Object[] { 
			"m", "s", "s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSword), new Object[] { 
			"m", "m", "s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoritePickaxe, 1), new Object[] { 
			"mmm", " s ", " s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", " s", " s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", "s ", "s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.torchMeteorShieldActive, 4), new Object[] { 
			"m", "s", 'm', MeteorItems.itemMeteorChips, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.blockMeteorShield, 1), new Object[] { 
			"mmm", "crc", "ccc", 'm', MeteorItems.MeteoriteIngot, 'c', "cobblestone", 'r', Items.REDSTONE
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.ice, 4), new Object[] { 
			Items.water_bucket, MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteHelmet, 1), new Object[] { 
			"ccc", "c c", 'c', MeteorItems.FrozenIron
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", 'c', MeteorItems.FrozenIron
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", 'c', MeteorItems.FrozenIron
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBoots, 1), new Object[] { 
			"c c", "c c", 'c', MeteorItems.FrozenIron
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteHelmet, 1), new Object[] { 
			"ccc", "c c", 'c', MeteorItems.KreknoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", 'c', MeteorItems.KreknoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", 'c', MeteorItems.KreknoriteIngot
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBoots, 1), new Object[] { 
			"c c", "c c", 'c', MeteorItems.KreknoriteIngot
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.KreknoriteSword, 1), new Object[] { 
			"c", "c", "s", 'c', MeteorItems.KreknoriteIngot, 's', "stickWood"
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemVanillaIceCream, 4), new Object[] { 
			Items.BOWL, Items.SUGAR, Items.MILK_BUCKET, MeteorItems.itemFrezaCrystal
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MeteorItems.itemChocolateIceCream, 4), new Object[] { 
			Items.BOWL, Items.SUGAR, Items.MILK_BUCKET, MeteorItems.itemFrezaCrystal, "dyeBrown"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezaritePickaxe, 1), new Object[] { 
			"ccc", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSpade, 1), new Object[] { 
			"c", "s", "s", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			" cc", " sc", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			"cc ", "cs ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			"cc ", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			" cc", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSword, 1), new Object[] { 
			"c", "c", "s", 'c', MeteorItems.FrozenIron, 's', "stickWood"
		}));

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", 'm', MeteorItems.MeteoriteIngot, 'f', MeteorItems.FrozenIron,
				'k', MeteorItems.KreknoriteIngot, 'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 0),
				'r', Items.REDSTONE
		});

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockRedMeteorGem, 1), new Object[] {
			"rrr", "rrr", "rrr", 'r', MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemRedMeteorGem, 9), new Object[] {
			MeteorBlocks.blockRedMeteorGem
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 0), new Object[] {
			"iii", "iii", "iii", 'i', MeteorItems.MeteoriteIngot
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.MeteoriteIngot, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 0)
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 1), new Object[] {
			"iii", "iii", "iii", 'i', MeteorItems.FrozenIron
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.FrozenIron, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 1)
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 2), new Object[] {
			"iii", "iii", "iii", 'i', MeteorItems.KreknoriteIngot
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.KreknoriteIngot, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 2)
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorProximityDetector, 1), new Object[] { 
			" m ", "mrm", " m ", 'm', MeteorItems.MeteoriteIngot, 'r', Items.REDSTONE
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorTimeDetector, 1), new Object[] { 
			" f ", "frf", " f ", 'f', MeteorItems.FrozenIron, 'r', MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorCrashDetector, 1), new Object[] { 
			" k ", "krk", " k ", 'k', MeteorItems.KreknoriteIngot, 'r', MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1), new Object[] { 
			"rmr", "mrm", "rmr", 'm', MeteorItems.MeteoriteIngot, 'r', MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 1), new Object[] { 
			"mbm", "bsb", "mbm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 0)
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 2), new Object[] { 
			"mbm", "bsb", "mbm", 'm', MeteorItems.FrozenIron, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 1)
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 3), new Object[] { 
			"mbm", "bsb", "mbm", 'm', MeteorItems.KreknoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 2)
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 4), new Object[] { 
			"rmr", "ksk", "rfr", 'm', new ItemStack(MeteorBlocks.blockDecorator, 1, 0), 's', MeteorItems.itemMeteorSummoner,
				'k', new ItemStack(MeteorBlocks.blockDecorator, 1, 2), 'f', new ItemStack(MeteorBlocks.blockDecorator, 1, 1),
				'r', MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'f', Items.FISH
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'f', Items.COOKED_FISH
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockFreezer, 1), new Object[] {
			"ifi", "fbf", "ifi", 'i', Items.IRON_INGOT, 'f', MeteorItems.itemFrezaCrystal,
				'b', Items.BUCKET
		}); 
		
		// ============= Smelting Recipes =============
		GameRegistry.addSmelting(MeteorItems.itemMeteorChips, new ItemStack(MeteorItems.MeteoriteIngot), 0.7F);
		GameRegistry.addSmelting(MeteorItems.itemKreknoChip, new ItemStack(MeteorItems.KreknoriteIngot), 0.75F);
	}

}
