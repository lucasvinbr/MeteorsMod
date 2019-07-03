package net.meteor.common;

import net.meteor.common.util.MeteorCraftingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class HandlerRecipe implements IFuelHandler {
	
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		Item item = event.crafting.getItem();
		EntityPlayer player = event.player;
		if (item == Item.getItemFromBlock(MeteorBlocks.METEOR_SHIELD)) {
			//TODO 1.12.2
			//player.addStat(HandlerAchievement.shieldCrafted, 1);
		} else if (item == MeteorItems.KreknoriteSword) {
			//TODO 1.12.2
			//player.addStat(HandlerAchievement.craftedKreknoSword, 1);
		} else if (item == MeteorItems.itemMeteorProximityDetector ||
				   item == MeteorItems.itemMeteorTimeDetector ||
				   item == MeteorItems.itemMeteorCrashDetector) {
			//TODO 1.12.2
			//player.addStat(HandlerAchievement.craftedDetector, 1);
		} else if (item == Item.getItemFromBlock(MeteorBlocks.METEOR_TIMER)) {
			//TODO 1.12.2
			//player.addStat(HandlerAchievement.craftedMeteorTimer, 1);
		} else if (item == Item.getItemFromBlock(MeteorBlocks.FREEZER)) {
			//TODO 1.12.2
			//player.addStat(HandlerAchievement.craftedFreezer, 1);
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
		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteHelmet, 1), "mmm", "m m", 'm', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteBody, 1), "m m", "mmm", "mmm", 'm', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteLegs, 1), "mmm", "m m", "m m", 'm', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteBoots, 1), "m m", "m m", 'm', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), "mm", "ms", " s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), "mm", "sm", "s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteSpade, 1), "m", "s", "s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteSword), "m", "m", "s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoritePickaxe, 1), "mmm", " s ", " s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), "mm", " s", " s", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), "mm", "s ", "s ", 'm', MeteorItems.MeteoriteIngot, 's', Items.IRON_INGOT);

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorBlocks.METEOR_SHIELD_TORCH, 4), "m", "s", 'm', MeteorItems.itemMeteorChips, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorBlocks.METEOR_SHIELD, 1), "mmm", "crc", "ccc", 'm', MeteorItems.MeteoriteIngot, 'c', "cobblestone", 'r', Items.REDSTONE);

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(Blocks.ICE, 4), Items.WATER_BUCKET, MeteorItems.itemFrezaCrystal);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.FrezariteHelmet, 1), "ccc", "c c", 'c', MeteorItems.FrozenIron);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.FrezariteBody, 1), "c c", "ccc", "ccc", 'c', MeteorItems.FrozenIron);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.FrezariteLegs, 1), "ccc", "c c", "c c", 'c', MeteorItems.FrozenIron);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.FrezariteBoots, 1), "c c", "c c", 'c', MeteorItems.FrozenIron);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.KreknoriteHelmet, 1), "ccc", "c c", 'c', MeteorItems.KreknoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.KreknoriteBody, 1), "c c", "ccc", "ccc", 'c', MeteorItems.KreknoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.KreknoriteLegs, 1), "ccc", "c c", "c c", 'c', MeteorItems.KreknoriteIngot);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.KreknoriteBoots, 1), "c c", "c c", 'c', MeteorItems.KreknoriteIngot);

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.KreknoriteSword, 1), "c", "c", "s", 'c', MeteorItems.KreknoriteIngot, 's', "stickWood");

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(MeteorItems.itemVanillaIceCream, 4), Items.BOWL, Items.SUGAR, Items.MILK_BUCKET, MeteorItems.itemFrezaCrystal);

		MeteorCraftingHelper.addShapelessOreRecipe(new ItemStack(MeteorItems.itemChocolateIceCream, 4), Items.BOWL, Items.SUGAR, Items.MILK_BUCKET, MeteorItems.itemFrezaCrystal, "dyeBrown");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezaritePickaxe, 1), "ccc", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSpade, 1), "c", "s", "s", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), " cc", " sc", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), "cc ", "cs ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), "cc ", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), " cc", " s ", " s ", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSword, 1), "c", "c", "s", 'c', MeteorItems.FrozenIron, 's', "stickWood");

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.METEOR_TIMER, 1), "mfk", "brb", 'm', MeteorItems.MeteoriteIngot, 'f', MeteorItems.FrozenIron,
				'k', MeteorItems.KreknoriteIngot, 'b', new ItemStack(MeteorBlocks.DECORATOR_METEORITE, 1),
				'r', Items.REDSTONE);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.RED_METEOR_GEM, 1), "rrr", "rrr", "rrr", 'r', MeteorItems.itemRedMeteorGem);

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(MeteorItems.itemRedMeteorGem, 9), MeteorBlocks.RED_METEOR_GEM);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.DECORATOR_METEORITE, 1, 0), "iii", "iii", "iii", 'i', MeteorItems.MeteoriteIngot);

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(MeteorItems.MeteoriteIngot, 9), new ItemStack(MeteorBlocks.DECORATOR_METEORITE, 1, 0));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.DECORATOR_FREEZARITE, 1), "iii", "iii", "iii", 'i', MeteorItems.FrozenIron);

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(MeteorItems.FrozenIron, 9), new ItemStack(MeteorBlocks.DECORATOR_FREEZARITE, 1, 1));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.DECORATOR_KREKNORITE, 1), "iii", "iii", "iii", 'i', MeteorItems.KreknoriteIngot);

		MeteorCraftingHelper.addShapelessRecipe(new ItemStack(MeteorItems.KreknoriteIngot, 9), new ItemStack(MeteorBlocks.DECORATOR_KREKNORITE, 1, 2));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorProximityDetector, 1), " m ", "mrm", " m ", 'm', MeteorItems.MeteoriteIngot, 'r', Items.REDSTONE);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorTimeDetector, 1), " f ", "frf", " f ", 'f', MeteorItems.FrozenIron, 'r', MeteorItems.itemRedMeteorGem);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorCrashDetector, 1), " k ", "krk", " k ", 'k', MeteorItems.KreknoriteIngot, 'r', MeteorItems.itemRedMeteorGem);

		//TODO 1.12.2 kill meta
/*
		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1), "rmr", "mrm", "rmr", 'm', MeteorItems.MeteoriteIngot, 'r', MeteorItems.itemRedMeteorGem);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 1), "mbm", "bsb", "mbm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 0));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 2), "mbm", "bsb", "mbm", 'm', MeteorItems.FrozenIron, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 1));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 3), "mbm", "bsb", "mbm", 'm', MeteorItems.KreknoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'b', new ItemStack(MeteorBlocks.blockDecorator, 1, 2));

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 4), "rmr", "ksk", "rfr", 'm', new ItemStack(MeteorBlocks.blockDecorator, 1, 0), 's', MeteorItems.itemMeteorSummoner,
				'k', new ItemStack(MeteorBlocks.blockDecorator, 1, 2), 'f', new ItemStack(MeteorBlocks.blockDecorator, 1, 1),
				'r', MeteorItems.itemRedMeteorGem);
*/

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), "mfm", "fsf", "mfm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'f', Items.FISH);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), "mfm", "fsf", "mfm", 'm', MeteorItems.MeteoriteIngot, 's', MeteorItems.itemMeteorSummoner,
				'f', Items.COOKED_FISH);

		MeteorCraftingHelper.addShapedRecipe(new ItemStack(MeteorBlocks.FREEZER, 1), "ifi", "fbf", "ifi", 'i', Items.IRON_INGOT, 'f', MeteorItems.itemFrezaCrystal,
				'b', Items.BUCKET);
		
		// ============= Smelting Recipes =============
		MeteorCraftingHelper.addSmelting(MeteorItems.itemMeteorChips, new ItemStack(MeteorItems.MeteoriteIngot), 0.7F);
		MeteorCraftingHelper.addSmelting(MeteorItems.itemKreknoChip, new ItemStack(MeteorItems.KreknoriteIngot), 0.75F);
	}

}
