package net.meteor.common.block.container;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
			if (item.getItem() == Item.getItemFromBlock(Blocks.ICE)) {
				//TODO 1.12.2
				//this.thePlayer.addStat(HandlerAchievement.freezeWater, 1);
			} else if (item.getItem() == Item.getItemFromBlock(Blocks.PACKED_ICE)) {
				//TODO 1.12.2
				//this.thePlayer.addStat(HandlerAchievement.freezeIce, 1);
			} else if (Block.getBlockFromItem(item.getItem()) instanceof BlockSlippery || Block.getBlockFromItem(item.getItem()) instanceof BlockSlipperyStairs) {
				//TODO 1.12.2
				//this.thePlayer.addStat(HandlerAchievement.freezeBlocks, 1);
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
