package net.meteor.common.block.container;

import net.meteor.client.gui.slots.SlotFreezeFuel;
import net.meteor.client.gui.slots.SlotLiquidStorage;
import net.meteor.common.FreezerRecipes;
import net.meteor.common.FreezerRecipes.RecipeType;
import net.meteor.common.MeteorItems;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFreezingMachine extends Container {

	private TileEntityFreezingMachine iceMaker;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;
	private int lastFluidAmount;
	private int lastRecipeModeID;

	public ContainerFreezingMachine(InventoryPlayer inventoryPlayer, TileEntityFreezingMachine iceMaker) {
		this.iceMaker = iceMaker;
		this.addSlotToContainer(new Slot(iceMaker, 0, 73, 17));//Freeze item in
		this.addSlotToContainer(new SlotFreezeFuel(iceMaker, 1, 73, 53));//freeze item fuel
		this.addSlotToContainer(new SlotTakeOnly(iceMaker, 2, 133, 35, inventoryPlayer.player));//freeze item out
		this.addSlotToContainer(new SlotLiquidStorage(iceMaker, 3, 38, 7));//liquid in
		this.addSlotToContainer(new SlotTakeOnly(iceMaker, 4, 38, 60));//liquid out
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return iceMaker.isUsableByPlayer(player);
	}

	@Override
	public void addListener(IContainerListener craft)
	{
		super.addListener(craft);
		/*
		craft.sendWindowProperty(this, 2, this.iceMaker.getField(2));
		craft.sendWindowProperty(this, 0, this.iceMaker.getField(0));
		craft.sendWindowProperty(this, 1, this.iceMaker.getField(1));
		craft.sendWindowProperty(this, 3, this.iceMaker.getField(3));
		FluidTankInfo tankInfo = this.iceMaker.getTankInfo();
		if (tankInfo != null && tankInfo.fluid != null) {
			craft.sendWindowProperty(this, 4, tankInfo.fluid.amount);
		} else {
			craft.sendWindowProperty(this, 4, 0);
		}
		craft.sendWindowProperty(this, 5, this.iceMaker.getRecipeMode().getID());
		 */
		craft.sendAllWindowProperties(this, this.iceMaker);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	 @Override
	 public void detectAndSendChanges()
	{
		 super.detectAndSendChanges();

		 FluidTankInfo tankInfo = this.iceMaker.getTankInfo();

		 for (int i = 0; i < this.listeners.size(); ++i)
		 {
			 IContainerListener icrafting = this.listeners.get(i);

			 if (this.lastCookTime != this.iceMaker.getField(2))
			 {
				 icrafting.sendWindowProperty(this, 2, this.iceMaker.getField(2));
			 }

			 if (this.lastBurnTime != this.iceMaker.getField(0))
			 {
				 icrafting.sendWindowProperty(this, 0, this.iceMaker.getField(0));
			 }

			 if (this.lastItemBurnTime != this.iceMaker.getField(1))
			 {
				 icrafting.sendWindowProperty(this, 1, this.iceMaker.getField(1));
			 }

			 if (tankInfo != null && tankInfo.fluid != null) {
				 if (this.lastFluidAmount != tankInfo.fluid.amount)
				 {
					 icrafting.sendWindowProperty(this, 3, tankInfo.fluid.amount);
					 this.lastFluidAmount = tankInfo.fluid.amount;
				 }
			 } else {
				 if (this.lastFluidAmount != 0) {
					 icrafting.sendWindowProperty(this, 3, 0);
					 this.lastFluidAmount = 0;
				 }
			 }
			 
			 if (this.lastRecipeModeID != this.iceMaker.getRecipeMode().getID()) {
				 icrafting.sendWindowProperty(this, 4, this.iceMaker.getRecipeMode().getID());
			 }

		 }

		 this.lastCookTime = this.iceMaker.getField(2);
		 this.lastBurnTime = this.iceMaker.getField(0);
		 this.lastItemBurnTime = this.iceMaker.getField(1);
		 this.lastRecipeModeID = this.iceMaker.getRecipeMode().getID();
	}

	 @Override
	 @SideOnly(Side.CLIENT)
	 public void updateProgressBar(int id, int value) {
		 if(id >= 0 && id <= 2) {
			 this.iceMaker.setField(id, value);
		 }
		 else if (id == 3 && iceMaker.getTankInfo() != null && iceMaker.getTankInfo().fluid != null) {
			 this.iceMaker.getTankInfo().fluid.amount = value;
		 }
		 else if (id == 4) {
			 this.iceMaker.setRecipeMode(RecipeType.values()[value]);
		 }
	 }

	 /**
	  * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	  */
	 @Override
	 public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	 {
		 ItemStack itemstack = ItemStack.EMPTY;
		 Slot slot = this.inventorySlots.get(slotID);

		 final int start_of_player_inv_slots = 5;
		 final int end_of_player_inv_slots = 32;
		 final int end_of_player_hotbar = 41;

		 if (slot != null && slot.getHasStack())
		 {
			 ItemStack itemstack1 = slot.getStack();
			 itemstack = itemstack1.copy();

			 if (slotID == TileEntityFreezingMachine.FREEZE_ITEM_OUT)
			 {
				 if (!this.mergeItemStack(itemstack1, start_of_player_inv_slots, end_of_player_hotbar, true))
				 {
					 return ItemStack.EMPTY;
				 }

				 slot.onSlotChange(itemstack1, itemstack);
			 }
			 else if (slotID != 1 && slotID != 0 && slotID != 3 && slotID != 4)
			 {
				 if (FreezerRecipes.instance().isRequiredItem(itemstack1) || FreezerRecipes.isFreezableItem(itemstack1))
				 {
					 if (!this.mergeItemStack(itemstack1, 0, 1, false))
					 {
						 return ItemStack.EMPTY;
					 }
				 }
				 else if (itemstack1.getItem() == MeteorItems.itemFrezaCrystal)
				 {
					 if (!this.mergeItemStack(itemstack1, 1, 2, false))
					 {
						 return ItemStack.EMPTY;
					 }
				 }
				 else if (itemstack1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
				 {
					 if (!this.mergeItemStack(itemstack1, 3, 4, false))
					 {
						 return ItemStack.EMPTY;
					 }
				 }
				 else if (slotID >= start_of_player_inv_slots && slotID < end_of_player_inv_slots)
				 {
					 if (!this.mergeItemStack(itemstack1, end_of_player_inv_slots, end_of_player_hotbar, false))
					 {
						 return ItemStack.EMPTY;
					 }
				 }
				 else if (slotID >= 32 && slotID < 41 && !this.mergeItemStack(itemstack1, 5, end_of_player_inv_slots, false))
				 {
					 return ItemStack.EMPTY;
				 }
			 }
			 else if (!this.mergeItemStack(itemstack1, start_of_player_inv_slots, end_of_player_hotbar, false))
			 {
				 return ItemStack.EMPTY;
			 }

			 if (itemstack1.getCount() == 0)
			 {
				 slot.putStack(ItemStack.EMPTY);
			 }
			 else
			 {
				 slot.onSlotChanged();
			 }

			 if (itemstack1.getCount() == itemstack.getCount())
			 {
				 return ItemStack.EMPTY;
			 }

			 slot.onTake(player, itemstack1);
		 }

		 return itemstack;
	 }

}
