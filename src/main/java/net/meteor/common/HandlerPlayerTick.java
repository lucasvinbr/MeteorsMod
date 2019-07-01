package net.meteor.common;

import java.util.List;

import net.meteor.plugin.baubles.Baubles;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HandlerPlayerTick
{
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		InventoryPlayer inv = player.inventory;

		if ((isWearing(MeteorItems.KreknoriteHelmet, inv.armorItemInSlot(3))) && 
				(isWearing(MeteorItems.KreknoriteBody, inv.armorItemInSlot(2))) && 
				(isWearing(MeteorItems.KreknoriteLegs, inv.armorItemInSlot(1))) && 
				(isWearing(MeteorItems.KreknoriteBoots, inv.armorItemInSlot(0)))) {
			ArmorEffectController.setImmuneToFire(player, true);
		} else {
			ArmorEffectController.setImmuneToFire(player, false);
		}

		int totalLevel = EnchantmentHelper.getEnchantmentLevel(MeteorsMod.ColdTouch, inv.armorItemInSlot(1)) + EnchantmentHelper.getEnchantmentLevel(MeteorsMod.ColdTouch, inv.armorItemInSlot(0));
		if (totalLevel > 0 && !player.isInWater())
		{
			if (totalLevel > 1 && player.isSprinting())
			{
				int l = MathHelper.floor(player.posX);
				int j1 = MathHelper.floor(player.posY - 2.0D);
				int k1 = MathHelper.floor(player.posZ);
				for (int x = l - 2; x < l + 2; x++) {
					for (int z = k1 - 2; z < k1 + 2; z++) {
						BlockPos pos = new BlockPos(x, j1, z);
						IBlockState blockState = world.getBlockState(pos);
						if (blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.FLOWING_WATER) {
							world.setBlockState(pos, Blocks.ICE.getDefaultState(), 2);
						}
					}		
				}
			}
			else
			{
				for (int j = 0; j < 4; j++) {
					int l = MathHelper.floor(player.posX + (j % 2 * 2 - 1) * 0.25F);
					int j1 = MathHelper.floor(player.posY - 2.0D);
					int k1 = MathHelper.floor(player.posZ + (j / 2 % 2 * 2 - 1) * 0.25F);
					BlockPos pos = new BlockPos(l, j1, k1);
					IBlockState blockState = world.getBlockState(pos);
					if (blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.FLOWING_WATER) {
						world.setBlockState(pos, Blocks.ICE.getDefaultState(), 2);
					}
				}
			}
		}

		int enchLevel = getMagnetizationLevel(player);
		if (enchLevel > 0) {
			double distance = 8 * enchLevel;
			List entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX - distance, player.posY - distance, player.posZ - distance, player.posX + distance, player.posY + distance, player.posZ + distance));
			for (Object entity : entities)
				if ((entity instanceof EntityItem))
					updateEntityItem((EntityItem) entity, player, distance);
		}
	}

	private static boolean isWearing(Item item, ItemStack itemStack) {
		if ((itemStack != null) && (item == itemStack.getItem())) return true;

		return false;
	}

	public static int getMagnetizationLevel(EntityPlayer player) {
		int enchLevel = Math.max(EnchantmentHelper.getMaxEnchantmentLevel(MeteorsMod.Magnetization, player.getLastActiveItems()), EnchantmentHelper.getEnchantmentLevel(MeteorsMod.Magnetization, player.getHeldItem(EnumHand.MAIN_HAND)));
		if (Baubles.isBaublesLoaded()) {
			return Baubles.canAttractItems(player, enchLevel);
		} else {
			return enchLevel;
		}
	}

	private void updateEntityItem(EntityItem en, EntityPlayer player, double closeness) {
		if (player != null) {
			double var3 = (player.posX - en.posX) / closeness;
			double var5 = (player.posY + player.getEyeHeight() - en.posY) / closeness;
			double var7 = (player.posZ - en.posZ) / closeness;
			double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
			double var11 = 1.0D - var9;

			if (var11 > 0.0D)
			{
				var11 *= var11;
				en.motionX += var3 / var9 * var11 * 0.1D;
				en.motionY += var5 / var9 * var11 * 0.1D;
				en.motionZ += var7 / var9 * var11 * 0.1D;
			}
		}
	}
}