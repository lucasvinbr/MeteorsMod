package net.meteor.common.block.container;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Rewrite to be similar to FurnaceOuputSlot.
 */
public class SlotTakeOnly extends Slot {
	
	private EntityPlayer thePlayer;

	public SlotTakeOnly(IInventory iInventory, int index, int xPosition, int yPosition) {
		super(iInventory, index, xPosition, yPosition);
	}
	
	public SlotTakeOnly(IInventory par1iInventory, int index, int xPosition, int yPosition, EntityPlayer player) {
		this(par1iInventory, index, xPosition, yPosition);
		this.thePlayer = player;
	}
	
	/**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}
	
	@Override
	protected void onCrafting(ItemStack itemStack, int amount) {
		this.onCrafting(itemStack);
		super.onCrafting(itemStack, amount);
    }
	
	@Override
	protected void onCrafting(ItemStack item) {
		if (item != null && this.thePlayer != null) {
			if (!this.thePlayer.world.isRemote) {
				if (item.getItem() == Item.getItemFromBlock(Blocks.ICE)) {
					HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) this.thePlayer, HandlerAchievement.freezeWater, HandlerAchievement.waterFrozen);
				} else if (item.getItem() == Item.getItemFromBlock(Blocks.PACKED_ICE)) {
					HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) this.thePlayer, HandlerAchievement.freezeIce, HandlerAchievement.iceFrozen);
				} else if (Block.getBlockFromItem(item.getItem()) instanceof BlockSlippery || Block.getBlockFromItem(item.getItem()) instanceof BlockSlipperyStairs) {
					HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) this.thePlayer, HandlerAchievement.freezeBlocks, HandlerAchievement.blocksFrozen);
				}
			}
		}
		super.onCrafting(item);
	}
	
	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack itemStack) {
		this.onCrafting(itemStack);
		return super.onTake(player, itemStack);
    }

}
