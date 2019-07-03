package net.meteor.common.tileentity;

import net.meteor.common.FreezerRecipes;
import net.meteor.common.FreezerRecipes.FreezerRecipe;
import net.meteor.common.FreezerRecipes.RecipeType;
import net.meteor.common.MeteorItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// This class is sort of a mirror image of a vanilla furnace,
// but has some new attributes such as fluid handling and other recipes.
public class TileEntityFreezingMachine extends TileEntityNetworkBase implements ISidedInventory, IFluidHandler, ITickable {

	private static int[] acccessibleSlots = {0, 1, 2, 3, 4};

	private NonNullList<ItemStack> inv = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private ItemStack lastKnownItem = null;
	private FluidTank tank = new FluidTank(1000 * 10);//TODO 1.12.2
	private RecipeType acceptedRecipeType = RecipeType.either;

	public int cookTime;
	public int burnTime;
	public int currentItemBurnTime;

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inv)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public void clear()
	{
		this.inv.clear();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.inv.get(slot) != ItemStack.EMPTY)
		{
			ItemStack itemstack;

			if (this.inv.get(slot).getCount() <= amount)
			{
				itemstack = this.inv.get(slot);
				this.inv.set(slot, ItemStack.EMPTY);
				return itemstack;
			}
			else
			{
				itemstack = this.inv.get(slot).splitStack(amount);

				if (this.inv.get(slot).getCount() == 0)
				{
					this.inv.set(slot, ItemStack.EMPTY);
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.inv, index);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack item) {
		
		this.inv.set(slot, item);
		
		if (item != null && item.getCount() > this.getInventoryStackLimit())
		{
			item.setCount(this.getInventoryStackLimit());
		}

		if (slot == 3) {
			checkFluidContainer();
		}
		
	}
	
	private void checkFluidContainer() {
		ItemStack item = inv.get(3);

		if (item != ItemStack.EMPTY && item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			if (!item.isEmpty()) {
				FluidStack fluid = FluidUtil.getFluidContained(item);
				if (fluid != null && (fluid.isFluidEqual(tank.getFluid())) || tank.getFluidAmount() == 0) {
					if (tank.fill(fluid, false) == fluid.amount) {

						// Try to insert it into the bottom slot
						ItemStack emptyContainer = null;
						//TODO 1.12.2
						//FluidContainerData[] containerData = FluidContainerRegistry.getRegisteredFluidContainerData();
						//for (FluidContainerData containerDatum : containerData) {
						//	if (containerDatum.filledContainer.isItemEqual(item)) {
						//		emptyContainer = containerDatum.emptyContainer.copy();
						//	}
						//}

						if (emptyContainer != null) {
							if (inv.get(4) == ItemStack.EMPTY) {
								tank.fill(fluid, true);
								inv.set(4, emptyContainer);
								decrStackSize(3, 1);
								//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
								//this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
							} else if (inv.get(4).isItemEqual(emptyContainer) && inv.get(4).getCount() + 1 <= inv.get(4).getMaxStackSize()) {
								tank.fill(fluid, true);
								inv.get(4).grow(1);
								decrStackSize(3, 1);
								//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
								//this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
							}
						}

					}
				}
			} else {
				FluidStack fluidInTank = tank.getFluid();
				if (fluidInTank != null) {
					FluidActionResult fluidActionResult = FluidUtil.tryFillContainer(item, tank, 1000, null, false);//TODO 1.12.2 should this be false? seems forge changed logic a lot
					if (fluidActionResult.isSuccess()) {
						ItemStack filledContainer = fluidActionResult.getResult();
						if (inv.get(4) == ItemStack.EMPTY) {
							tank.drain(FluidUtil.getFluidContained(filledContainer).amount, true);
							inv.set(4, filledContainer);
							decrStackSize(3, 1);
							//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
							//this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
						} else if (inv.get(4).isItemEqual(filledContainer) && inv.get(4).getCount() + 1 <= inv.get(4).getMaxStackSize()) {
							tank.drain(FluidUtil.getFluidContained(filledContainer).amount, true);
							inv.get(4).grow(1);
							decrStackSize(3, 1);
							//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
							//this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
						}
					}
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Freezer";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.inv.clear();

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inv.size())
			{
				this.inv.set(b0, new ItemStack(nbttagcompound1));
			}
		}

		this.burnTime = nbt.getShort("BurnTime");
		this.cookTime = nbt.getShort("CookTime");
		this.currentItemBurnTime = nbt.getShort("ItemFreezeTime");
		this.acceptedRecipeType = RecipeType.values()[nbt.getShort("acceptedRecipeType")];
		tank.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short)this.burnTime);
		nbt.setShort("CookTime", (short)this.cookTime);
		nbt.setShort("ItemFreezeTime", (short)this.currentItemBurnTime);
		nbt.setShort("acceptedRecipeType", (short)this.acceptedRecipeType.getID());
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inv.size(); ++i)
		{
			if (this.inv.get(i) != ItemStack.EMPTY)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.inv.get(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
		tank.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(super.getUpdateTag());
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) != this ? false : player.getDistanceSq((double)this.getPos().getX() + 0.5D, (double)this.getPos().getY() + 0.5D, (double)this.getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (slot == 3) return item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		return slot == 2 ? false : (slot == 1 ? getItemFreezeTime(item) > 0 : true);
	}

	@Override
	public int getField(int id) {
		switch (id)
		{
			case 0:
				return this.cookTime;
			case 1:
				return this.burnTime;
			case 2:
				return this.currentItemBurnTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				this.cookTime = value;
				break;
			case 1:
				this.burnTime = value;
				break;
			case 2:
				this.currentItemBurnTime = value;
				break;
		}
	}


	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return acccessibleSlots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, EnumFacing direction) {

		return item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ? slot == 3 : slot == 1;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
		return slot == 2 || slot == 4;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i)
	{
		return this.cookTime * i / 200;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int i)
	{
		if (this.currentItemBurnTime == 0)
		{
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * i / this.currentItemBurnTime;
	}
	
	public RecipeType getRecipeMode() {
		return this.acceptedRecipeType;
	}
	
	public void setRecipeMode(RecipeType type) {
		this.acceptedRecipeType = type;
	}

	public boolean isFreezing() {
		return this.burnTime > 0;
	}

	@Override
	public void update() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;

		if (this.burnTime > 0)
		{
			--this.burnTime;
		}

		if (!this.getWorld().isRemote)
		{
			
			if (inv.get(3) != ItemStack.EMPTY) {
				checkFluidContainer();
			}
			
			if (this.burnTime != 0 || this.inv.get(1) != ItemStack.EMPTY)
			{
				if (this.burnTime == 0 && this.canFreeze())
				{
					this.currentItemBurnTime = this.burnTime = getItemFreezeTime(this.inv.get(1));

					if (this.burnTime > 0)
					{
						flag1 = true;

						if (this.inv.get(1) != ItemStack.EMPTY)
						{
							this.inv.get(1).shrink(1);

							if (this.inv.get(1).getCount() == 0)
							{
								this.inv.set(1, inv.get(1).getItem().getContainerItem(inv.get(1)));
							}
						}
					}
				}

				if (this.isFreezing() && this.canFreeze())
				{
					
					
					++this.cookTime;

					if (this.cookTime == 200)
					{
						this.cookTime = 0;
						this.freezeItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
			} else {
				this.cookTime = 0;
			}

			if (flag != this.burnTime > 0)
			{
				flag1 = true;
				//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
				//int meta = getWorld().getBlockMetadata(xCoord, yCoord, zCoord);
				//this.getWorld().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, burnTime > 0 ? meta + 4 : meta - 4, 2);
			}
		}

		if (flag1)
		{
			this.markDirty();
			//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
			//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public static int getItemFreezeTime(ItemStack itemStack) {
		if (itemStack != null) {
			Item item = itemStack.getItem();
			if (item == MeteorItems.itemFrezaCrystal) {
				return 1600;
			}
			if (item == Item.getItemFromBlock(Blocks.ICE)) {
				return 200;
			}
			if (item == Item.getItemFromBlock(Blocks.PACKED_ICE)) {
				return 400;
			}
		}
		return 0;
	}

	private boolean canFreeze() {
		FreezerRecipe recipe = FreezerRecipes.instance().getFreezingResult(this.inv.get(0), tank.getFluid(), this.acceptedRecipeType);
		if (recipe == null) return false;
		ItemStack result = recipe.getResult(inv.get(0));
		if (this.inv.get(2) == ItemStack.EMPTY) {
			if (this.lastKnownItem == null) {
				this.lastKnownItem = result;
			} else if (!result.isItemEqual(this.lastKnownItem)) {
				this.cookTime = 0;
				this.lastKnownItem = result;
				this.markDirty();
				//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
				//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			return true;
		}
		if (!this.inv.get(2).isItemEqual(result)) return false;
		int resultSize = inv.get(2).getCount() + result.getCount();
		if (resultSize <= getInventoryStackLimit() && resultSize <= this.inv.get(2).getMaxStackSize()) {
			if (this.lastKnownItem == null) {
				this.lastKnownItem = result;
			} else if (!result.isItemEqual(this.lastKnownItem)) {
				this.cookTime = 0;
				this.lastKnownItem = result;
				this.markDirty();
				//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
				//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			return true;
		}
		return false;
	}

	public void freezeItem() {
		if (this.canFreeze())
		{
			FreezerRecipe recipe = FreezerRecipes.instance().getFreezingResult(this.inv.get(0), tank.getFluid(), this.acceptedRecipeType);

			if (this.inv.get(2) == ItemStack.EMPTY)
			{
				this.inv.set(2, recipe.getResult(inv.get(0)).copy());
			}
			else if (this.inv.get(2).getItem() == recipe.getResult(inv.get(0)).getItem())
			{
				this.inv.get(2).grow(recipe.getResult(inv.get(0)).getCount());
			}

			if (recipe.requiresItem()) {
				this.inv.get(0).shrink(1);

				if (this.inv.get(0).getCount() <= 0)
				{
					this.inv.set(0, ItemStack.EMPTY);
				}
			}

			if (recipe.requiresFluid()) {
				tank.drain(recipe.getFluidAmount(), true);

				if (tank.getFluidAmount() == 0) {
					//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
					//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
					}
			}

		}
	}

	/* IFluidHandler */
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (tank.getFluidAmount() == 0) {
			//TODO 1.12.2 don't think this is needed anymore
			//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
		{
			//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
			// getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			return null;
		}
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (doDrain && tank.getFluidAmount() - maxDrain <= 0) {
			//TODO 1.12.2 world.notifyBlockUpdate(state, state, 3) ?
			//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return tank.getTankProperties();
	}

	public FluidTankInfo getTankInfo() {
		return tank.getInfo();
	}

	@Override
	public void onButtonPress(int id) {
		if (id == 0) {
			
			int i = this.acceptedRecipeType.getID();
			if (i == 3) {
				i = 0;
			} else {
				i++;
			}
			this.acceptedRecipeType = RecipeType.values()[i];
		}
	}

}
