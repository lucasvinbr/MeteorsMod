package net.meteor.common.item;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorItems;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemSummoner extends ItemMeteorsMod
{

	public ItemSummoner() {
		super();
		this.maxStackSize = 16;
		this.setMaxDamage(0);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if (!playerIn.capabilities.isCreativeMode)
		{
			itemStack.shrink(1);
		}
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, new SoundEvent(new ResourceLocation("minecraft:random.bow")), SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote)
		{
			int i = itemStack.getItemDamage();//TODO 1.12.2, need to remove meta :'(
			EnumMeteor meteorType = getMeteorForItem(this);
			worldIn.spawnEntity(new EntitySummoner(worldIn, playerIn, meteorType == null ? HandlerMeteor.getRandomMeteorType() :  meteorType, i == 0));

		}
		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	@Nullable
	private EnumMeteor getMeteorForItem(ItemSummoner itemSummoner) {
		if(itemSummoner == MeteorItems.itemMeteorSummonerMeteorite) {
			return EnumMeteor.METEORITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerFrezarite) {
			return EnumMeteor.FREZARITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerKreknorite) {
			return EnumMeteor.KREKNORITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerUnknown) {
			return EnumMeteor.UNKNOWN;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerKitty) {
			return EnumMeteor.KITTY;
		}

		return null;
	}

}