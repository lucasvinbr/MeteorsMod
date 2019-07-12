package net.meteor.common.item;

import net.meteor.common.EnumMeteor;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

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
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote)
		{
			EnumMeteor meteorType = EnumMeteor.getMeteorForItem(this);
			EntitySummoner entitySummoner = new EntitySummoner(worldIn, playerIn, meteorType == null ? HandlerMeteor.getRandomMeteorType() : meteorType, meteorType == null);
			entitySummoner.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0f);
			worldIn.spawnEntity(entitySummoner);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

}